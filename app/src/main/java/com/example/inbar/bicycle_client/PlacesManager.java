package com.example.inbar.bicycle_client;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.inbar.bicycle_client.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class PlacesManager {

    private String Key = "AIzaSyAK4YCms8YltWWh_beAVXDylQw-GCW55_s";
    URIBuilder builder;
    HttpClient client = new DefaultHttpClient();
    ArrayList<Place> resultList;


    public ArrayList<Place> LoadLocations(String types, double lng, double lat, GoogleMap mMap, LatLng stationPosition1) throws ParseException, IOException, URISyntaxException, ExecutionException, InterruptedException {

        resultList = new ArrayList();
        String [] allTypes = types.split(",");
        for ( String type: allTypes) {
            builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/place/nearbysearch/json");
            builder.addParameter("location", lat + "," + lng);
            builder.addParameter("radius", "3000");
            builder.addParameter("rating", "4");
            builder.addParameter("opennow", "true");
            builder.addParameter("types", type);
            builder.addParameter("key", this.Key);

            String status = new PlacesManagerHttpHendler().execute().get();
        }
        Collections.sort(resultList,Place::compareTo);
        ChoosePlacesForTheUserRoute(resultList,lat,lng,mMap, stationPosition1);
        return resultList;
    }

    private void ChoosePlacesForTheUserRoute(ArrayList<Place> resultList, double lat, double lng, GoogleMap mMap, LatLng stationPosition1) throws ExecutionException, InterruptedException {


        try {
            String status = new DirectionsRequests(resultList, lat, lng,mMap,stationPosition1).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    class PlacesManagerHttpHendler extends AsyncTask<String, Object, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpUriRequest request = new HttpGet(builder.build());

                HttpResponse execute = client.execute(request);

                HttpEntity response = execute.getEntity();

                if (response != null) {
                    String retSrc = EntityUtils.toString(response);
                    // parsing JSON
                    JSONObject jsonObj = new JSONObject(retSrc);
                    JSONArray predsJsonArray = jsonObj.getJSONArray("results");

                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        Place place = new Place();
                        place.rating = predsJsonArray.getJSONObject(i).getDouble("rating");
                        place.name = predsJsonArray.getJSONObject(i).getString("name");
                        // save lat and lan parameters  twice as double and as string
                        place.lat = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        place.Strlat = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                        place.lng = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        place.Strlng=predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                        place.category = predsJsonArray.getJSONObject(i).getString("types");

                        resultList.add(place);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

