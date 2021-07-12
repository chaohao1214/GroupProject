package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




public class CarCharging extends AppCompatActivity {

    EditText latitude;
    EditText longitude;
    String latitudeInput;
    String longitudeInput;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_charging);

        /*recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter();*/

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(recyclerAdapter);
        Intent fromPreCar = getIntent();

        Button search = findViewById(R.id.searchButton);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchResult();
            }
        });
    }

   /* public void getLatitudeInput(){
        Intent intent = new Intent(CarCharging.this, searchresult.class);
        String latitudeInput = latitude.getText().toString();
        intent.putExtra("Latitude: ",latitudeInput );
    }

    public void getLongitudeinput(){
        Intent intent = new Intent(CarCharging.this, searchresult.class);
        String longitudeInput = longitude.getText().toString();
        intent.putExtra("Longitude: ",longitudeInput );
    }*/

    public void openSearchResult() {
        Intent intent = new Intent(CarCharging.this, SearchResultRecyclerview.class);

        String latitudeInput = latitude.getText().toString();
        intent.putExtra("Latitude",latitudeInput );

        String longitudeInput = longitude.getText().toString();
        intent.putExtra("Longitude",longitudeInput );

        startActivity(intent);
    }

}