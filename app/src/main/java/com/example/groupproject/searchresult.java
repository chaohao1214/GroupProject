package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class searchresult extends AppCompatActivity {

    TextView receivedLatitude;
    TextView receivedLongitude;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        getLatitudeInput();
        getLongitudeInput();
    }

    /**
     * This method is receiving the latitude input value from CarCharging page
     */
    public void getLatitudeInput(){
        receivedLatitude = findViewById(R.id.receivedLatitude);
        input=getIntent().getExtras().getString("Latitude");
        receivedLatitude.setText(input);
    }

    /**
     * This method is receiving the longitude input value from CarCharging page
     */
    public void getLongitudeInput(){
        receivedLongitude = findViewById(R.id.receivedLongitude);
        input=getIntent().getExtras().getString("Longitude");
        receivedLongitude.setText(input);
    }


}