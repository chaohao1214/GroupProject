package com.example.groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SearchStation extends AppCompatActivity {

    Toolbar main_menu;
    private RecyclerView carRecyclerView;
    private RecyclerView carRecyclerView1;
    private StationAdapter carStationAdapter;
    private ArrayList<StationObject> carStationList;
    private EditText longitude;
    private SharedPreferences sharedPref;
    private Button searchStationBtn;
    boolean isTablet;
    private String serverURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_search);

        isTablet = findViewById(R.id.detailsFragment) != null;


        carRecyclerView = findViewById(R.id.recycler_view);
        carRecyclerView.setHasFixedSize(true);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        carStationList = new ArrayList<>();

        carRecyclerView1 = findViewById(R.id.recycler_view);
        carRecyclerView1.setHasFixedSize(true);
        carRecyclerView1.setLayoutManager(new LinearLayoutManager(this));




        // Toolbar
        main_menu = findViewById(R.id.carMenu);
        setSupportActionBar(main_menu);

        //receive info from previous page
        Intent fromPreOC = getIntent();

        //fragment

        //Shared preference
        sharedPref = getSharedPreferences("SharedPreferencesCarChargingStation", MODE_PRIVATE);
        String searchLatiitudeInfo = sharedPref.getString("Latitude","");
        String searchLongitudeInfo = sharedPref.getString("Longitude","");
        EditText latitudeInput = findViewById(R.id.latitudeInput);
        EditText longtudeInput = findViewById(R.id.longitudeInput);
        latitudeInput.setText(searchLatiitudeInfo);
        longtudeInput.setText(searchLongitudeInfo);


        // search button
        searchStationBtn = (Button)findViewById(R.id.searchStationButton);

        EditText latitude = findViewById(R.id.latitudeInput);
        EditText longitude = findViewById(R.id.longitudeInput);

        searchStationBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(SearchStation.this)
                        .setTitle("Getting Station List")
                        .setMessage("Retrieving station list...")
                        .setView(new ProgressBar(SearchStation.this))
                        .show();


                String key = "39b2b98f-7541-45b5-98eb-ac20b05f3362";
                    serverURL = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude="
                            + latitude.getText().toString() + "&longitude=" + longitude.getText().toString() + "&maxresults=10" + "&key="+key;

                    // still on GUI thread, cannot connect server here.
                    Executor newThread = Executors.newSingleThreadExecutor();
                    newThread.execute(( ) ->{
                        // on other CPU
                        URL url = null;
                        try {
                            url = new URL(serverURL);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                            // This converts to String
                            String text = (new BufferedReader(
                                    new InputStreamReader(in, StandardCharsets.UTF_8)))
                                    .lines()
                                    .collect(Collectors.joining("\n"));

                            // convert string to JSON object:

                          //  JSONObject theDocument = new JSONObject( text);
                            JSONArray jsonArray = new JSONArray(text);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject stationJSON = jsonArray.getJSONObject(i);
                                JSONObject addressJSON = stationJSON.getJSONObject("AddressInfo");

                                String title = addressJSON.getString("Title");
//                                double latitude = addressJSON.getDouble("Latitude");
//                                double longitude = addressJSON.getDouble("Longitude");
//                                String contactNo = addressJSON.getString("ContactTelephone1");


 //                               long id =0;
 //                               carStationList.add(new StationObject(id, title, latitude,longitude,contactNo));
                                carStationList.add(new StationObject(title));
                            }
                            carStationAdapter = new StationAdapter(SearchStation.this, carStationList, true);

                           runOnUiThread(()->{ carRecyclerView.setAdapter(carStationAdapter);
                               dialog.hide();
                           });
                        }
                        catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    } );

                SharedPreferences.Editor editor = sharedPref.edit();
                EditText stationLatitude = findViewById(R.id.latitudeInput);
                EditText stationLongitude = findViewById(R.id.longitudeInput);
                editor.putString("Latitude", stationLatitude.getText().toString());
                editor.putString("Longitude", stationLongitude.getText().toString());
                latitudeInput.setText("");
                longtudeInput.setText("");
                editor.apply();
                Toast.makeText(getApplicationContext(),  "Station list is loading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.show_help:
                AlertDialog.Builder helpAlertBuilder = new AlertDialog.Builder(SearchStation.this);
                helpAlertBuilder.setTitle("Help");
                helpAlertBuilder.setMessage("Author: Shakib Ahmed\nVersion 1.0\nInstructions on how to use:\n1.Insert latitude and longitude and click search.\n2. List of near by stations will be displayed in a list.");
                helpAlertBuilder.show();
                break;
        }
        return true;
    }

}