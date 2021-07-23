package com.example.groupproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SearchStation extends AppCompatActivity {
    RecyclerView searchView;
    MyStationAdapter stationAdt = new MyStationAdapter();
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    ArrayList<StationMessage> searchMessage = new ArrayList<>();
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_search);

        // create database
        MyOpenHelperCar opener = new MyOpenHelperCar(this);
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + MyOpenHelperCar.TABLE_NAME + ";", null);

        //meta data
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelperCar.col_message);
        int searchButtonInfoCol = results.getColumnIndex(MyOpenHelperCar.search_button_info);
        int timeCol = results.getColumnIndex(MyOpenHelperCar.col_time_sent);

        // set data attributes
        while(results.moveToNext()){
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int searchButtonInfo = results.getInt(searchButtonInfoCol);
            searchMessage.add(new StationMessage(message,searchButtonInfo,time,id));

        }

        //receive info from previous page
        Intent fromPreOC = getIntent();
        // save the content that entered before
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchLongitudeInfo = prefs.getString("Longitude","");
        String searchLatiitudeInfo = prefs.getString("Latitude","");
        EditText stationLongitudeMsg = findViewById(R.id.searchBox);
        //EditText stationLatitudeMsg = findViewById(R.id.searchBoxLatitude);
        stationLongitudeMsg.setText(searchLongitudeInfo);
       // stationLatitudeMsg.setText(searchLatiitudeInfo);


        Button searchStationBtn = findViewById(R.id.searchStationButton);
        searchView = findViewById(R.id.searchView);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setStackFromEnd(true);
        mgr.setReverseLayout(true);

        searchView.setLayoutManager(mgr);

        searchView.setAdapter(stationAdt);

        searchStationBtn.setOnClickListener(clk ->{
            StationMessage thisMessage = new StationMessage(stationLongitudeMsg.getText().toString(),1, sdf.format(new Date()));
            searchMessage.add(thisMessage);
            stationAdt.notifyItemInserted(searchMessage.size()-1);


            SharedPreferences.Editor editor = prefs.edit();
            EditText stationMsgText = findViewById(R.id.searchBox);
            editor.putString("Longitude", stationMsgText.getText().toString());
            stationLongitudeMsg.setText("");
            editor.apply();
            Toast.makeText(getApplicationContext(),  "Station list is loading...", Toast.LENGTH_SHORT).show();

            // get and insert data
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelperCar.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelperCar.search_button_info, thisMessage.getStationMesg());
            newRow.put(MyOpenHelperCar.col_time_sent, thisMessage.getTimeSearch());
            long id = db.insert(MyOpenHelperCar.TABLE_NAME, MyOpenHelperCar.col_message, newRow);
            thisMessage.setId(id);

        });

    }

    private class RowViews extends RecyclerView.ViewHolder {

        TextView stationInfo;
        TextView timeInfo;
        int position = -1;

        public RowViews(View itemView) {
            super(itemView);

            stationInfo = itemView.findViewById(R.id.stationInfo);
            timeInfo = itemView.findViewById(R.id.timeView);

            itemView.setOnClickListener(click ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchStation.this);
                builder.setMessage("Do you want to delete this message: " + stationInfo.getText())
                        .setTitle("Queestions:")
                        .setPositiveButton("Yes",(dialog, cl)->{
                            //position = getAbsoluteAdapterPosition(); always turn red ???
                            position = getAdapterPosition();
                            StationMessage removeMessage = searchMessage.get(position);
                            searchMessage.remove(position);
                            stationAdt.notifyItemRemoved(position);

                            //set to delete data in database
                            db.delete(MyOpenHelperCar.TABLE_NAME, "_id=?", new String[]
                                    { Long.toString(removeMessage.getId())});

                            Snackbar.make(stationInfo,"You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk ->{
                                        searchMessage.add(position,removeMessage);
                                        stationAdt.notifyItemInserted(position);
                                        // delete action for data
                                        db.execSQL("Insert into " + MyOpenHelperCar.TABLE_NAME + " values('" +
                                                removeMessage.getId() + "','" + removeMessage.getMessage() + "','"
                                                +removeMessage.getStationMesg() + "','" + removeMessage.getTimeSearch() +"');");
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
            View v = getLayoutInflater().inflate(R.layout.search_view_car, parent,false);//row in a recyclerview, bus
            return new RowViews(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RowViews busRowLayout = (RowViews) holder;
            busRowLayout.stationInfo.setText(searchMessage.get(position).getMessage());
            busRowLayout.timeInfo.setText(searchMessage.get(position).getTimeSearch());
            busRowLayout.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return searchMessage.size();
        }
    }


    private class StationMessage {
        String message;
        int stationMesg;
        String timeSearch;
        long id;

        public void setId(long l){
            id = l;
        }

        public long getId(){
            return id;
        }

        public StationMessage(String message, int busMsg, String timeSearch){
            this.message = message;
            this.stationMesg = busMsg;
            this.timeSearch = timeSearch;
        }

        public StationMessage(String message, int searchButtonInfo, String time, long id) {
            this.message = message;
            this.stationMesg = searchButtonInfo;
            this.timeSearch = time;
            setId(id);

        }

        public String getMessage(){
            return  message;
        }
        public int getStationMesg(){
            return stationMesg;
        }

        public String getTimeSearch() {
            return timeSearch;
        }
    }
}