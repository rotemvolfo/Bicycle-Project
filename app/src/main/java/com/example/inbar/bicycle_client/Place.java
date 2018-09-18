package com.example.inbar.bicycle_client;

import android.support.annotation.NonNull;

public class Place implements Comparable<Place> {

    String category;
    String name;
    double lat;
    double lng;
    String Strlat;
    String Strlng ;
    double rating;
    boolean isInWayPoint=false;
    @Override
    public int compareTo(@NonNull Place o) {
        return Double.compare(o.rating, this.rating);

    }

    @Override
    public String toString() {
        String data;
        if(isInWayPoint)
          data=category+","+name+","+Strlat+","+Strlng+","+rating+"true";
        else
            data=category+","+name+","+Strlat+","+Strlng+","+rating+"false";
        return data;

    }
}
