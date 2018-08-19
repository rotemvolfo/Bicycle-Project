package com.example.inbar.bicycle_client;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class SpecificStation {// present station from the Api

    private String name;
    private String englishName;
    private String address;
    private double latitude;
    private double longitude;
    private int numOfBicyclesAvailable;
    private int numOfAvailableDocks;


    public SpecificStation(SoapObject infoStation){
        this.name = (String) infoStation.getAttribute("Station_Name");
        this.englishName = (String) infoStation.getAttribute("Eng_Station_Name");
        this.address = (String) infoStation.getAttribute("Description");
        this.latitude = Double.parseDouble((String)infoStation.getAttribute("Latitude"));
        this.longitude = Double.parseDouble((String) infoStation.getAttribute("Longitude"));
        this.numOfBicyclesAvailable = Integer.parseInt((String) infoStation.getAttribute("NumOfAvailableBikes"));
        this.numOfAvailableDocks = Integer.parseInt((String)infoStation.getAttribute("NumOfAvailableDocks"));



    }




    public String getName() { return name; }
    public String getNameEng() { return englishName; }
    public String getAddress() { return address; }
    public int getNumOfBicyclesAvailable() { return numOfBicyclesAvailable; }
    public int getNumOfPolesAvailable() { return numOfAvailableDocks; }
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}

}
