package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.CarCharge);

        btn.setOnClickListener(clk -> {
            openCarChargingStationLocator();
        });
    }

    public void openCarChargingStationLocator(){
        Intent nextPageCar = new Intent(this,CarCharging.class);
        startActivity(nextPageCar);
    }



}
