package com.example.inbar.bicycle_client;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.ksoap2.*;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StationsActivity extends AppCompatActivity{

    ArrayList <SpecificStation> stationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
    }

    public ArrayList<SpecificStation> getStationsList(String longitude , String langitude) throws ExecutionException, InterruptedException {
        String status = new SoapCall().execute(langitude,longitude).get();
        return stationsList;
    }

    private class SoapCall extends AsyncTask<String,Object,String> {//reqwst to the api tel ofan
            String SOAP_ACTION = "http://tempuri.org/GetNearestStations";
            String METHOD_NAME = "GetNearestStations";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://www.tel-o-fun.co.il:2470/ExternalWS/Geo.asmx?op=GetNearestStations";
            int TimeOut=30000;



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... location) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo propertyLong =new PropertyInfo();
            propertyLong.setName("longitude");
            propertyLong.setValue(location[0]);
            propertyLong.setType(String.class);
            request.addProperty(propertyLong);

            PropertyInfo propertylang =new PropertyInfo();
            propertylang.setName("langitude");
            propertylang.setValue(location[1]);
            propertylang.setType(String.class);
            request.addProperty(propertylang);

            PropertyInfo propertyRadius =new PropertyInfo();// radius to look show
            propertyRadius.setName("radius");
            propertyRadius.setValue(10000000);// need to be large number
            propertyRadius.setType(int.class);
            request.addProperty(propertyRadius);

            PropertyInfo propertyMaxRadius =new PropertyInfo();
            propertyMaxRadius.setName("maxResults");
            propertyMaxRadius.setValue(1); ///return closest station
            propertyMaxRadius.setType(int.class);
            request.addProperty(propertyMaxRadius);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);// tha reqwst
                SoapObject soapPrimitive =(SoapObject) envelope.getResponse();// the respons from the api
                SoapObject result = (SoapObject) soapPrimitive.getProperty(0);
                findNearestStations(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //the function creates list of nearest stations and displays them to the user
    private void findNearestStations(SoapObject soapObjectWithInfoStations) {

        stationsList = new ArrayList<SpecificStation>();
        stationsList.add(new SpecificStation((SoapObject) soapObjectWithInfoStations.getProperty(0)));
//        stationsList.add(new SpecificStation((SoapObject) soapObjectWithInfoStations.getProperty(1)));
//        stationsList.add(new SpecificStation((SoapObject) soapObjectWithInfoStations.getProperty(2)));
    }
}
