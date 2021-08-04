package com.example.groupproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StationDetailContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_fragment_layout);

        Bundle bundle = getIntent().getExtras();

        StationDetailsFragment detailFragment = new StationDetailsFragment();
        detailFragment.setArguments(bundle); //pass data to the the fragment


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, detailFragment)
                .commit();
    }
}