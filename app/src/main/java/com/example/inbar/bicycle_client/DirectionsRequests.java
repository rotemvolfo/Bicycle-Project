package com.example.inbar.bicycle_client;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    URIBuilder _builderReturnRoute=new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/directions/json");
    HttpClient _client = new DefaultHttpClient();
    ArrayList<Place> _resultList;
    GoogleMap _mMap;
    double _Userlat ,_Userlng;
    String _retSrc;
    LatLng _stationPosition1;

    public DirectionsRequests(ArrayList<Place> resultList, double lat, double lng, GoogleMap mMap, LatLng stationPosition1) {

        _resultList=resultList;
        _Userlat=lat;
        _Userlng=lng;
        _mMap=mMap;
        _stationPosition1=stationPosition1;

    }

    @Override
    protected String doInBackground(String... strings) {
        String retSrc = null;
        UpdateApiUrl();

        HttpUriRequest request = null;
        //HttpUriRequest request2 = null;
        try {
            request = new HttpGet(_builder.build());
            //request2 = new HttpGet(_builderReturnRoute.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpResponse execute = null;
       // HttpResponse execute2 = null;
        try {
            execute = _client.execute(request);// do the api request
            // do the api request
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity response = execute.getEntity();

        //    execute2 = _client.execute(request);
      //  } catch (IOException e) {
       //     e.printStackTrace();
       // }
       // HttpEntity response2 = execute.getEntity();
        if (response != null) {

           // String retSrc2 = null;
            try {
                _retSrc=retSrc = EntityUtils.toString(response);
                //retSrc2 = EntityUtils.toString(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
        return null;
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(_mMap);

        // Invokes the thread for parsing the JSON data
        parserTask.execute(_retSrc);
        _retSrc="";

    }

    private void UpdateApiUrl() {
          _builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/directions/json");
        int[] indexArr;
        List<Integer> soutePlaces = new ArrayList<Integer>();
        List<Integer> NorthePlaces = new ArrayList<Integer>();
        indexArr= SortThePlacesToSouthOrNorth(soutePlaces,NorthePlaces);

        _builder.addParameter("origin",_Userlat + "," + _Userlng);
        _builder.addParameter("destination",_resultList.get(indexArr[0]).lat+","+_resultList.get(indexArr[0]).lng);// the destination is the most souhe Place
        _builder.addParameter("mode", "WALKING");
        _builder.addParameter("waypoints",_resultList.get(0).name);
       _builder.addParameter("optimize","true");
       double x=_stationPosition1.latitude;
       //_builder.addParameter("via",_stationPosition1.latitude+","+_stationPosition1.longitude);
        //_builder.addParameter("via",_resultList.get(soutePlaces.get(0)).lat+","+_resultList.get(soutePlaces.get(0)).lng);
        //_builder.addParameter("avoid","highways");
        _builder.addParameter("key",this._Key);

        // UpdateApiUrlSecoundPart
       // _builderReturnRoute.addParameter("origin",_resultList.get(indexArr[0]).lat+","+_resultList.get(indexArr[0]).lng);
       // _builderReturnRoute.addParameter("destination",_resultList.get(indexArr[1]).lat+","+_resultList.get(indexArr[1]).lng );// the destination is the most souhe Place
       // _builderReturnRoute.addParameter("mode", "WALKING");
       // _builderReturnRoute.addParameter("optimize","true");


        //_builder.addParameter("via",_resultList.get(soutePlaces.get(1)).lat+","+_resultList.get(soutePlaces.get(1)).lng);
        //_builder.addParameter("via",_resultList.get(NorthePlaces.get(0)).lat+","+_resultList.get(NorthePlaces.get(0)).lng);



        //_builder.addParameter("avoid","highways");
        _builderReturnRoute.addParameter("key",this._Key);



    }

    private int[] SortThePlacesToSouthOrNorth(List<Integer> soutePlaces, List<Integer> northePlaces) {
        int southestIndex=0,northestIndex=0;
        double maxValue=_Userlng;
        double minValue=_Userlng;
        int[] indexArr=new int[2];

        for(int i=0;i<_resultList.size();i++){

            if(_resultList.get(i).lng<_Userlng){
                soutePlaces.add(i);
                if(_resultList.get(i).lng<minValue){
                    minValue=_resultList.get(i).lng;
                    southestIndex=i;
                }
            }

            else if(_resultList.get(i).lng>_Userlng){
                northePlaces.add(i) ;

                if(_resultList.get(i).lng>maxValue) {
                    maxValue = _resultList.get(i).lng;
                    northestIndex = i;
                }

            }
        }

        indexArr[0]=southestIndex;
        indexArr[1]=northestIndex;
        return indexArr;
    }
}
