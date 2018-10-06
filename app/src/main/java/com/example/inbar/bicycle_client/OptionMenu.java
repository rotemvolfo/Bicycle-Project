package com.example.inbar.bicycle_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OptionMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);
    }

    public void Start_Clicked(View view) {
        String userOptions = new String();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_Museums);
        if(checkBox.isChecked()){
            userOptions += "museum,art_gallery,";
        }
          checkBox = (CheckBox) findViewById(R.id.checkBox_Museums);
        if(checkBox.isChecked()){
            userOptions += "Spa,";
        }

        checkBox = (CheckBox) findViewById(R.id.checkBox_restaurant);
        if(checkBox.isChecked()){
            userOptions += "restaurant,";
        }

        checkBox = (CheckBox) findViewById(R.id.checkBox_bar);
        if(checkBox.isChecked()){
            userOptions += "bar,";
        }

        checkBox = (CheckBox) findViewById(R.id.checkBox_movie);
        if(checkBox.isChecked()){
            userOptions += "movie_theater,";
        }

        checkBox = (CheckBox) findViewById(R.id.checkBox_shopping);
        if(checkBox.isChecked()){
            userOptions += "shopping_mall,";
        }


        Intent intent = new Intent(this, MapsActivity.class);//// here load the layer on map of the locations ( tel ofan)
        intent.putExtra("userOptions", userOptions);
        finish();
        startActivity(intent);
    }

    public void Previous_track_clicked(View view) {
        //String userOptions = new String();

        Intent intent = new Intent(this, MapsActivity.class);//// here load the layer on map of the locations ( tel ofan)
        intent.putExtra("userOptions","Previous_track");
        finish();
        startActivity(intent);




    }
}
