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

        Button Soccer = findViewById(R.id.Soccer);
//        Soccer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSoccerGamesApiApp();
//            }
//        });
//
//
//        public void openSoccerGamesApiApp() {
//            Intent intent = new Intent(this, SoccerActivitySecond.class);
//            startActivity(intent);
//        }

        Soccer.setOnClickListener(clk -> {
            Intent intent = new Intent(this, SoccerActivitySecond.class);
            startActivity(intent);
        });


        Button carCharge = findViewById(R.id.CarCharge);

        carCharge.setOnClickListener(clk -> {
            // go to next page
            Intent nextPageCar = new Intent(MainActivity.this, SearchStation.class);
            startActivity(nextPageCar);
        });


        ImageButton movie = findViewById(R.id.movie_info);

        movie.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, MovieInfo.class);
            startActivity(nextPage);
        });


        Button OCTranspo = findViewById(R.id.OCTranspo);

        OCTranspo.setOnClickListener(clk -> {
            //button to next page
            Intent nextPageOC = new Intent(MainActivity.this, SearchBus.class);
            startActivity(nextPageOC);

        });

    }
}


