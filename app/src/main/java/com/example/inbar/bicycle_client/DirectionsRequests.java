package com.example.inbar.bicycle_client;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class DirectionsRequests extends AsyncTask<String, Object, String> {

    private String _Key = "AIzaSyAK4YCms8YltWWh_beAVXDylQw-GCW55_s";
    URIBuilder _builder;// = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/directions/json");
    HttpClient _client = new DefaultHttpClient();
    ArrayList<Place> _resultList;
    GoogleMap _mMap;
    double _Userlat, _Userlng;//user potion x;y
    String _retSrc;
    LatLng _stationPosition1;
    Map<String, List<Place>> _placesDictionary;
    int _placesTotalNumber = 0;
    List<Place> _choosenPlacesList = new ArrayList<Place>();
    boolean _isLoadFromfile;

    public DirectionsRequests(ArrayList<Place> resultList, double lat, double lng, GoogleMap mMap, LatLng stationPosition1, boolean isLoadFromFile) {

        _placesTotalNumber = resultList.size();
        _resultList = resultList;
        _Userlat = lat;
        _Userlng = lng;
        _mMap = mMap;
        _stationPosition1 = stationPosition1;
        _isLoadFromfile = isLoadFromFile;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... strings) {
        if (!_isLoadFromfile) {
            UpdateApiUrl();

            HttpUriRequest request = null;

            try {
                request = new HttpGet(_builder.build());

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            HttpResponse execute = null;

            try {
                execute = _client.execute(request);// do the api request

            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpEntity response = execute.getEntity();


            if (response != null) {


                try {
                    _retSrc = EntityUtils.toString(response);
                     SaveUserRoute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {//load the direction from file and convert the json to string instead of string [] .
            //then we send the json file to the parser in order to present the route on the map
            String routesJson[] = null;

            FilesService file = new FilesService();
            routesJson = file.LoadRoutes();//load from file

            StringBuffer result = new StringBuffer();
            for (int i = 0; i < routesJson.length; i++) {
                result.append(routesJson[i]);

            }
            String convertjsonToStr = result.toString();
            _retSrc = convertjsonToStr;

        }
        return null;
    }

    private void SaveUserRoute() throws IOException {
        FilesService file = new FilesService();
        file.SaveRoutes(_retSrc);

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(_mMap);

        // Invokes the thread for parsing the JSON data
        parserTask.execute(_retSrc);
        _retSrc = "";

    }

    private void SortLocationDictionaryByCategory() {

        _placesDictionary = new HashMap<>();
        for (Place p : _resultList) {
            if (_placesDictionary.containsKey(p.category)) {
                List<Place> places_list = _placesDictionary.get(p.category);
                places_list.add(p);

            } else {
                List<Place> places = new ArrayList<Place>();
                places.add(p);
                _placesDictionary.put(p.category, places);
            }
        }
    }

    private String BuildWayPointsAtrribute() {

        StringBuilder wayPointStr = new StringBuilder();

        SortLocationDictionaryByCategory();
        int categoryNmber = _placesDictionary.size();

        int placesInRoute = 0;
        int attemps = 0;
        while (placesInRoute < 5 && attemps < 5) {

            for (Map.Entry<String, List<Place>> entry : _placesDictionary.entrySet()) {
                List<Place> places = entry.getValue();
                Collections.sort(places, Place::compareTo);//sort by rating
                if (placesInRoute < 5) {


                    if (places.size() > 0) {

                        wayPointStr = wayPointStr.append("|" + places.get(0).lat + "," + places.get(0).lng);
                        _choosenPlacesList.add(places.get(0));
                        places.get(0).isInWayPoint = true;
                        placesInRoute++;
                        places.remove(0);
                    }
                    if (places.size() == 0) {

                    }

                }
            }
            attemps++;
        }
        return wayPointStr.toString();
    }

    private void UpdateApiUrl() {


        _builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/directions/json");


        _builder.addParameter("origin", _Userlat + "," + _Userlng);
        _builder.addParameter("destination", _Userlat + "," + _Userlng);// the destination is the most souhe Place

        _builder.addParameter("mode", "walking");
        _builder.addParameter("waypoints", _stationPosition1.latitude + "," + _stationPosition1.longitude + "|" + BuildWayPointsAtrribute());
        _builder.addParameter("optimize", "true");

        _builder.addParameter("avoid", "highways");
        _builder.addParameter("key", this._Key);

    }

}
