package com.example.groupproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.os.Build;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Context;
import android.content.SharedPreferences;

import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView OCTranspo = findViewById(R.id.busImage);

        OCTranspo.setOnClickListener(clk -> {
            //button to next page
            Intent nextPageOC = new Intent(MainActivity.this, SearchBus.class);
            startActivity(nextPageOC);
        });

        ImageView Soccer = findViewById(R.id.Soccer);

        Soccer.setOnClickListener(clk -> {
            Intent intent = new Intent(this, SoccerActivitySecond.class);
            startActivity(intent);
        });


        ImageView carCharge = findViewById(R.id.CarCharge);

        carCharge.setOnClickListener(clk -> {

            Intent nextPageCar = new Intent(MainActivity.this, SearchStation.class);
            startActivity(nextPageCar);
        });

        //access to movie finder app by clicking the image
        ImageView movie = findViewById(R.id.movie_info);

        movie.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, MovieInfo.class);
            startActivity(nextPage);
        });


    }

    }



