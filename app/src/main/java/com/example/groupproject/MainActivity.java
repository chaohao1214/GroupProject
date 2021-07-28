package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView OCTranspo = findViewById(R.id.busImage);

        OCTranspo.setOnClickListener(clk ->{
            //button to next page
            Intent nextPageOC = new Intent(MainActivity.this, BusSearch.class);
            startActivity(nextPageOC);
        });
    }
}