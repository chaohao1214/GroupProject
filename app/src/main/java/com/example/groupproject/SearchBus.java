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

public class SearchBus extends AppCompatActivity {
    RecyclerView searchView;
    MyBusAdapter busAdt = new MyBusAdapter();
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    ArrayList<BusMessage> searchMessage = new ArrayList<>();
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_search);

        // create database
        MyOpenHelper opener = new MyOpenHelper(this);
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

        //meta data
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int searchButtonInfoCol = results.getColumnIndex(MyOpenHelper.search_button_info);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        // set data attributes
        while(results.moveToNext()){
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int searchButtonInfo = results.getInt(searchButtonInfoCol);
            searchMessage.add(new BusMessage(message,searchButtonInfo,time,id));

        }

        //receive info from previous page
        Intent fromPreOC = getIntent();
        // save the content that entered before
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchBusInfo = prefs.getString("BusNumber","");
        EditText busMsg = findViewById(R.id.searchBox);
        busMsg.setText(searchBusInfo);


        Button searchBusBtn = findViewById(R.id.searchBusButton);
        searchView = findViewById(R.id.searchView);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setStackFromEnd(true);
        mgr.setReverseLayout(true);

        searchView.setLayoutManager(mgr);

        searchView.setAdapter(busAdt);

        searchBusBtn.setOnClickListener(clk ->{
            BusMessage thisMessage = new BusMessage(busMsg.getText().toString(),1, sdf.format(new Date()));
            searchMessage.add(thisMessage);
            busAdt.notifyItemInserted(searchMessage.size()-1);


            SharedPreferences.Editor editor = prefs.edit();
            EditText busMsgText = findViewById(R.id.searchBox);
            editor.putString("BusNumber", busMsgText.getText().toString());
            busMsg.setText("");
            editor.apply();
            Toast.makeText(getApplicationContext(),  "Bus route is loading...", Toast.LENGTH_SHORT).show();

            // get and insert data
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.search_button_info, thisMessage.getBusMesg());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSearch());
            long id = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(id);

        });

    }

    private class RowViews extends RecyclerView.ViewHolder {

        TextView busInfo;
        TextView timeInfo;
        int position = -1;

        public RowViews(View itemView) {
            super(itemView);

            busInfo = itemView.findViewById(R.id.busInfo);
            timeInfo = itemView.findViewById(R.id.timeView);

            itemView.setOnClickListener(click ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchBus.this);
                builder.setMessage("Do you want to delete this message: " + busInfo.getText())
                        .setTitle("Queestions:")
                        .setPositiveButton("Yes",(dialog, cl)->{
                            //position = getAbsoluteAdapterPosition(); always turn red ???
                            position = getAdapterPosition();
                            BusMessage removeMessage = searchMessage.get(position);
                            searchMessage.remove(position);
                            busAdt.notifyItemRemoved(position);

                            //set to delete data in database
                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]
                                    { Long.toString(removeMessage.getId())});

                            Snackbar.make(busInfo,"You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk ->{
                                        searchMessage.add(position,removeMessage);
                                        busAdt.notifyItemInserted(position);
                                        // delete action for data
                                        db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" +
                                                removeMessage.getId() + "','" + removeMessage.getMessage() + "','"
                                        +removeMessage.getBusMesg() + "','" + removeMessage.getTimeSearch() +"');");
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



    private class MyBusAdapter extends RecyclerView.Adapter{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.search_view, parent,false);//row in a recyclerview, bus
        return new RowViews(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowViews busRowLayout = (RowViews) holder;
        busRowLayout.busInfo.setText(searchMessage.get(position).getMessage());
        busRowLayout.timeInfo.setText(searchMessage.get(position).getTimeSearch());
        busRowLayout.setPosition(position);
    }

        @Override
        public int getItemCount() {
            return searchMessage.size();
        }
    }
    
    
    private class BusMessage{
        String message;
        int busMesg;
        String timeSearch;
        long id;

        public void setId(long l){
            id = l;
        }

        public long getId(){
            return id;
        }

        public BusMessage(String message, int busMsg, String timeSearch){
            this.message = message;
            this.busMesg = busMsg;
            this.timeSearch = timeSearch;
        }

        public BusMessage(String message, int searchButtonInfo, String time, long id) {
            this.message = message;
            this.busMesg = searchButtonInfo;
            this.timeSearch = time;
            setId(id);

        }

        public String getMessage(){
            return  message;
        }
        public int getBusMesg(){
            return busMesg;
        }

        public String getTimeSearch() {
            return timeSearch;
        }
    }
}