package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button OCTranspo = findViewById(R.id.OCTranspo);

        OCTranspo.setOnClickListener(clk ->{
            //button to next page
            Intent nextPageOC = new Intent(MainActivity.this,SearchBus.class);
            startActivity(nextPageOC);
        });
    }
}