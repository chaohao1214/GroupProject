package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton movie = findViewById(R.id.movie_info);

        movie.setOnClickListener(clk -> {
            Intent movieFinder = new Intent(MainActivity.this, MovieInfo.class);
            startActivity(movieFinder);

        Button OCTranspo = findViewById(R.id.OCTranspo);

        OCTranspo.setOnClickListener(clk ->{
            //button to next page
            Intent nextPageOC = new Intent(MainActivity.this,SearchBus.class);
            startActivity(nextPageOC);

        });
    }
}
