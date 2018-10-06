package com.example.inbar.bicycle_client;

import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


//All the utils functions that load and save data off the app
public class FilesService {

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaTutorial";



    public void SaveRoutes( String routesStr)
    {
        File dir = new File(path);
        dir.mkdirs();
        File file = new File (path + "/savedRoute.txt");
        SaveRoutesHelper (file, routesStr);
        //String data[]=Load(file);
    }
    public static void SaveRoutesHelper(File file, String data) {//save_routes
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                if (fos != null) {
                    // String p=data.get(i).toStringForSaveInFile();
                    fos.write(data.getBytes());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SavePlaces(ArrayList<Place> resultList)
    {
        File dir = new File(path);
        dir.mkdirs();
        File file = new File (path + "/savedPlacesFile.txt");
        Save (file, resultList);
    }


    public static void Save(File file, ArrayList<Place> data) {//save places
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {

                for (int i = 0; i < data.size(); i++) {
                    if (fos != null) {
                        String p=data.get(i).toStringForSaveInFile();
                        fos.write(p.getBytes());

                    }
                    if (i < data.size() - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//*** fuctions that load routes and places from files

    public ArrayList<Place> LoadPlacesList(){
        File file = new File (path + "/savedPlacesFile.txt");
        String [] loadText = Load(file);

        String PlaceString = "";
        ArrayList<Place> resultList= new ArrayList<>();

        for (int i = 0; i < loadText.length; i++)
        {
            PlaceString = loadText[i];
            String [] Place = PlaceString.split("!");
            //category+","+name+","+Strlat+","+Strlng+","+rating+","+"true"

            Place placeObj=new Place();
            placeObj.category=Place[0];
            placeObj.name=Place[1];
            placeObj.Strlat=Place[2];
            placeObj.Strlng=Place[3];
            placeObj.rating= Double.parseDouble(Place[4]);
            if(Place[5].equals("true")) {
                placeObj.isInWayPoint = true;
            }
            else
                placeObj.isInWayPoint= false;
            placeObj.lat= Double.parseDouble(Place[2]);
            placeObj.lng= Double.parseDouble(Place[3]);

            resultList.add(placeObj);


        }
        return resultList;



    }

    public  String[] LoadRoutes( ){

        File file = new File (path + "/savedRoute.txt");
        String [] loadText = Load(file);
         return loadText;



    }

    public static String[] Load(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl = 0;
        try {
            while ((test = br.readLine()) != null) {
                anzahl++;
            }
        } catch (IOException e) {
            return null;
        }

        try {
            fis.getChannel().position(0);
        } catch (IOException e) {
            return null;
        }

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try {
            while ((line = br.readLine()) != null) {
                array[i] = line;
                i++;
            }
        } catch (IOException e) {
            return null;
        }
        return array;
    }
}