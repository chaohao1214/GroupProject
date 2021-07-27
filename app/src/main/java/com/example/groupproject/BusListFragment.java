package com.example.groupproject;

import android.content.ContentValues;
import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BusListFragment extends Fragment {

    private SharedPreferences busPref;
    Button searchBusButton;
    MyBusAdapter busAdt = new MyBusAdapter();
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    ArrayList<BusMessage> searchMessage = new ArrayList<>();
    SQLiteDatabase db;

    // The strings represents the address of the server
    private String busStringURL;
    private String busDetailURL;

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View BusLayout = inflater.inflate(R.layout.bus_search, container, false);
        EditText busMsg = BusLayout.findViewById(R.id.searchBox);
// create database
        BusOpenHelper opener = new BusOpenHelper(getContext());
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + BusOpenHelper.Bus_TABLE_NAME + ";", null);

        //meta data
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(BusOpenHelper.col_message);
        int searchButtonInfoCol = results.getColumnIndex(BusOpenHelper.search_button_info);
        int timeCol = results.getColumnIndex(BusOpenHelper.col_time_sent);

        // set data attributes
        while(results.moveToNext()){
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int searchButtonInfo = results.getInt(searchButtonInfoCol);
            searchMessage.add(new BusMessage(message,searchButtonInfo,time,id));

        }
        EditText busMsgText = BusLayout.findViewById(R.id.searchBox);

        busPref = getContext().getSharedPreferences("BusData", Context.MODE_PRIVATE);

        RecyclerView searchListView;
        searchBusButton = BusLayout.findViewById(R.id.searchBusButton);
        searchListView = BusLayout.findViewById(R.id.searchView);
        searchListView.setAdapter(busAdt);

        busMsgText.setText(busPref.getString("BusNumber", ""));


        searchListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        searchBusButton.setOnClickListener(clk ->{
            Toast.makeText(getContext().getApplicationContext(),  "Bus route is loading...", Toast.LENGTH_SHORT).show();
            BusMessage thisMessage = new BusMessage(busMsg.getText().toString(),1, sdf.format(new Date()));
            searchMessage.add(thisMessage);
            busAdt.notifyItemInserted(searchMessage.size()-1);
            SharedPreferences.Editor editor = busPref.edit();

            editor.putString("BusNumber", busMsgText.getText().toString());
            busMsg.setText("");
            editor.apply();


            // get and insert data
            ContentValues newRow = new ContentValues();
            newRow.put(BusOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(BusOpenHelper.search_button_info, thisMessage.getBusMesg());
            newRow.put(BusOpenHelper.col_time_sent, thisMessage.getTimeSearch());
            long id = db.insert(BusOpenHelper.Bus_TABLE_NAME, BusOpenHelper.col_message, newRow);
            thisMessage.setId(id);

            // new thread to connect to server
            Executor busThread = Executors.newSingleThreadExecutor();
            busThread.execute(() ->{
                // this is runs on another thread
                try {
                    String busNumber = busMsgText.getText().toString();
                    busStringURL = "https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                            + URLEncoder.encode(busNumber, "UTF-8") + "&format=json";
                    //create URL object
                    URL url = new URL(busStringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject theDocument = new JSONObject(text);
                    JSONObject busRouteSummary = theDocument.getJSONObject("RouteSummaryForStop");
                    JSONObject busRoutes = busRouteSummary.getJSONObject("Routes");
                    String stopNo = busRouteSummary.getString("StopNo");
                    String stopDescription = busRouteSummary.getString("StopDescription");

                    JSONArray routeArray = busRoutes.getJSONArray("Route");
                    //use loop to get all the routes info from the bus stop and save it in the list.
                    for (int i =0; i<routeArray.length(); i++){
                        // save the routes info in the list
                        JSONObject routeObject = routeArray.getJSONObject(i);
                        // stop here!!!!!!!!!!!!!!
                        BusRoute routMsg = new BusRoute();
                    }

                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        return BusLayout;
    }

    // delete the message in detail frgament
    public void notifyMessageDeleted(BusMessage busMessage, int busChosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete this message: " + busMessage.getMessage())
                .setTitle("Questions:")
                .setPositiveButton("Yes",(dialog, cl)->{
                    BusListFragment.BusMessage removeMessage = searchMessage.get(busChosenPosition);
                    searchMessage.remove(busChosenPosition);
                    busAdt.notifyItemRemoved(busChosenPosition);

                    //set to delete data in database
                    db.delete(BusOpenHelper.Bus_TABLE_NAME, "_id=?", new String[]
                            { Long.toString(removeMessage.getId())});

                    Snackbar.make(searchBusButton,"You deleted message #" + busChosenPosition, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk ->{
                                searchMessage.add(busChosenPosition,removeMessage);
                                busAdt.notifyItemInserted(busChosenPosition);
                                // delete action for data
                                db.execSQL("Insert into " + BusOpenHelper.Bus_TABLE_NAME + " values('" +
                                        removeMessage.getId() + "','" + removeMessage.getMessage() + "','"
                                        +removeMessage.getBusMesg() + "','" + removeMessage.getTimeSearch() +"');");
                            })
                            .show();
                })
                .setNegativeButton("No",(dialog, cl) ->{

                })
                .create().show();
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
                        SearchBus parentAcitivty = (SearchBus)getContext();
                        int position = getAdapterPosition();
                        parentAcitivty.busUserClickedMsg(searchMessage.get(position), position);
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }


    private class MyBusAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.search_view_bus, parent,false);//row in a recyclerview, bus
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


     class BusMessage{
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
