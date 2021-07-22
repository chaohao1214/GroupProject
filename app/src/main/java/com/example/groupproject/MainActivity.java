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

        Button carCharge = findViewById(R.id.CarCharge);

        carCharge.setOnClickListener(clk -> {
            // go to next page
            Intent nextPageCar = new Intent(MainActivity.this,SearchStation.class);
            startActivity(nextPageCar);
        });
    }

}
