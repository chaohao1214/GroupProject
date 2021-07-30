package com.example.groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchStation extends AppCompatActivity {

    Toolbar main_menu;


    private EditText longitude;
    private SharedPreferences sharedPref;

    private Button searchStationBtn;
    ArrayList<StationInfo> searchResult = new ArrayList<>();
    MyStationAdapter stationAdt = new MyStationAdapter();




    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_search);

        // Toolbar
        main_menu = findViewById(R.id.carMenu);
        setSupportActionBar(main_menu);


        //receive info from previous page
        Intent fromPreOC = getIntent();

        //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);



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

        final EditText latitude = findViewById(R.id.latitudeInput);
        longitude = (EditText)findViewById(R.id.longitudeInput);

        searchStationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "39b2b98f-7541-45b5-98eb-ac20b05f3362";
                String url = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude="
                        + latitude.getText().toString() + "&longitude=" + longitude.getText().toString() + "&maxresults=10" + "&key="+key;

                DownloadFilesTask downloadFileTask = new DownloadFilesTask();
                downloadFileTask.execute(url);

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

    /**
     * Class connects to the server, reads and process the data
     */
    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        /**
         * dialog to show a progression of downloading to the user
         */

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        /**
         * Methods connects to the server, retrieves the data about car charging stations
         *
         * @param urls link to the server
         * @return data about car charging stations
         */
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL link = new URL(urls[0]);
                urlConnection = (HttpURLConnection) link.openConnection();
                publishProgress(25);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                publishProgress(50);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                publishProgress(70);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }
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
                helpAlertBuilder.setMessage("Author: Shakib Ahmed\nVersion 1.0");
                helpAlertBuilder.show();
                break;
        }
        return true;
    }

    private class RowViews extends RecyclerView.ViewHolder {

        TextView stationTitle;
        TextView stationLatitude;
        TextView stationLongitude;
        TextView stationContactNo;
        int position = -1;

        public RowViews(View itemView) {
            super(itemView);

            stationTitle = itemView.findViewById(R.id.stationTitle);
            stationLatitude = itemView.findViewById(R.id.stationLatitude);
            stationLongitude = itemView.findViewById(R.id.stationLongitude);
            stationContactNo = itemView.findViewById(R.id.stationContactNo);


            itemView.setOnClickListener(click ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchStation.this);
                builder.setMessage("Do you want to delete this message: " + stationTitle.getText())
                        .setTitle("Questions:")
                        .setPositiveButton("Yes",(dialog, cl)->{

                            position = getAdapterPosition();
                            StationInfo removeMessage = searchResult.get(position);
                            searchResult.remove(position);
                            stationAdt.notifyItemRemoved(position);

//                            //set to delete data in database
//                            db.delete(MyOpenHelperCar.TABLE_NAME, "_id=?", new String[]
//                                    { Long.toString(removeMessage.getId())});

                            Snackbar.make(stationTitle,"You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk ->{
                                        searchResult.add(position,removeMessage);
                                        stationAdt.notifyItemInserted(position);
                                        // delete action for data
//                                        db.execSQL("Insert into " + MyOpenHelperCar.TABLE_NAME + " values('" +
//                                                removeMessage.getId() + "','" + removeMessage.getMessage() + "','"
//                                                +removeMessage.getStationMesg() + "','" + removeMessage.getTimeSearch() +"');");
                                    })
                                    .show();
                        })
                        .setNegativeButton("No",(dialog, cl) ->{

                        })
                        .create().show();
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private class MyStationAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.search_view_car, parent,false);
            return new RowViews(v);
        }

        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
            RowViews stationRowLayout = (RowViews) holder;
            stationRowLayout.stationTitle.setText(searchResult.get(position).getLocationTitle());
            stationRowLayout.stationLatitude.setText(searchResult.get(position).getLatitudeValue());
            stationRowLayout.stationLongitude.setText(searchResult.get(position).getLongitudeValue());
            stationRowLayout.stationContactNo.setText(searchResult.get(position).getContactNo());
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }


    private class StationInfo {
        String locationTitle;
        String latitudeValue;
        String longitudeValue;
        String contactNo;

        public StationInfo(String locationTitle, String latitude, String longitude, String contactNo) {
            this.locationTitle = locationTitle;
            this.latitudeValue = latitude;
            this.longitudeValue = longitude;
            this.contactNo = contactNo;
        }

        public String getLocationTitle(){
            return  locationTitle;
        }
        public String getLatitudeValue(){
            return latitudeValue;
        }

        public String getLongitudeValue(){
            return longitudeValue;
        }

        public String getContactNo() {
            return contactNo;
        }
    }
}