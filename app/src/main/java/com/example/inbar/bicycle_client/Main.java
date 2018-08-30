package com.example.inbar.bicycle_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        }

    public void buttonStart_onClick(View view) {
        Intent intent = new Intent(this, OptionMenu.class);//// here load the layer on map of the locations ( tel ofan)
        startActivity(intent);
    }

}
