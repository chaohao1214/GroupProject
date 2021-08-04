package com.example.groupproject;

import android.content.ContentValues;
import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

/**
 * @author Chaohao
 * @version 1.0
 */
public class BusListFragment extends Fragment {


    // The strings represents the address of the server
    private String busStringURL;
    private String busDetailURL;
    RecyclerView searchListView;
    Button helpBtn;
    Button searchBusButton;
    MyBusAdapter busAdt = new MyBusAdapter();
    SharedPreferences busPref;
    SQLiteDatabase db;
    Button favoriteButton;

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    ArrayList<BusMessage> searchMessage = new ArrayList<>();
    EditText searchBar;
    TextView information;

    ArrayList<BusRoute> busList = new ArrayList<>();
    ArrayList<BusRoute> favoriteList = new ArrayList<>();
    FavoriteAdapter favorAdt = new FavoriteAdapter();

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View BusLayout = inflater.inflate(R.layout.bus_search, container, false);
        searchBar = BusLayout.findViewById(R.id.searchBox);
// create database
        BusOpenHelper opener = new BusOpenHelper(getContext());
        db = opener.getWritableDatabase();

        // set data attributes
        searchListView = BusLayout.findViewById(R.id.searchView);
        information = BusLayout.findViewById(R.id.info);
        helpBtn = BusLayout.findViewById(R.id.Bus_help);
        helpBtn.setOnClickListener(click -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

            alertDialogBuilder.setTitle(R.string.help_title_bus)
                    .setMessage(R.string.help_content_bus)
                    .setPositiveButton("close", (c, arg) -> {
                    })
                    .create().show();
        });
        favoriteButton = BusLayout.findViewById(R.id.favoriteButton);
        busPref = getContext().getSharedPreferences("BusData", Context.MODE_PRIVATE);
        favoriteButton.setOnClickListener(clk ->{
            favoriteList.clear();
            Cursor results = db.rawQuery("Select * from " + BusOpenHelper.Bus_TABLE_NAME + ";", null);

            //meta data
            int _idCol = results.getColumnIndex("_id");
            int routeCol = results.getColumnIndex(BusOpenHelper.col_routes);
            int busDesoCol = results.getColumnIndex(BusOpenHelper.col_heading);
            int dirCol = results.getColumnIndex(BusOpenHelper.col_direction);
            int directionIdCol = results.getColumnIndex(BusOpenHelper.col_directionID);

            while(results.moveToNext()){
                long id = results.getInt(_idCol);
                String routeNumber = results.getString(routeCol);
                String dest = results.getString(busDesoCol);
                String direc = results.getString(dirCol);
                String direcID = results.getString(directionIdCol);

                favoriteList.add(new BusRoute(routeNumber, dest, direc, direcID,id));

            }

            getActivity().runOnUiThread(()->{

                information.setText("Favorite list:");
            });
            searchListView.setAdapter(favorAdt);
        });


        searchBusButton = BusLayout.findViewById(R.id.searchBusButton);

        searchListView.setAdapter(busAdt);
        TextView info = BusLayout.findViewById(R.id.info);

        searchBar.setText(busPref.getString("BusNumber", ""));


        searchListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        searchBusButton.setOnClickListener(clk ->{
            Toast.makeText(getContext().getApplicationContext(),  "Bus route is loading...", Toast.LENGTH_SHORT).show();
            BusMessage thisMessage = new BusMessage(searchBar.getText().toString(),1, sdf.format(new Date()));
            searchMessage.add(thisMessage);
            busAdt.notifyItemInserted(searchMessage.size()-1);
            SharedPreferences.Editor editor = busPref.edit();

            editor.putString("BusNumber", searchBar.getText().toString());
            editor.apply();

            // when the app is getting data from API, show this message
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Getting bus stop information")
                    .setMessage("We'are getting information for the bus stop " + searchBar.getText().toString() + "to check the route information for this stop")
                    .setView(new ProgressBar(getContext()))
                    .show();


            // new thread to connect to server
            Executor busThread = Executors.newSingleThreadExecutor();
            busThread.execute(() ->{
                // this is runs on another thread
                try {
                    String busNumber = searchBar.getText().toString();
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
                    JSONObject busRouteSummary = theDocument.optJSONObject("GetRouteSummaryForStopResult");

                        JSONObject busRoutes = busRouteSummary.getJSONObject("Routes");
                        String stopNo = busRouteSummary.getString("StopNo");
                        String stopDescription = busRouteSummary.getString("StopDescription");

                        JSONArray routeArray = busRoutes.getJSONArray("Route");
                        //use loop to get all the routes info from the bus stop and save it in the list.
                        for (int i = 0; i < routeArray.length(); i++) {
                            // save the routes info in the list
                            JSONObject routeObject = routeArray.getJSONObject(i);

                            BusRoute routMsg = new BusRoute();
                            routMsg.setBusNumber("Route:" + routeObject.getString("RouteNo"));
                            routMsg.setDestination("Heading" + routeObject.getString("RouteHeading"));
                            routMsg.setDirection(routeObject.getString("Direction"));
                            routMsg.setDirectionID(routeObject.getString("DirectionID"));
                            busList.add(routMsg);
                            // put the bus number info into database
                            // get and insert data
                            ContentValues newRow = new ContentValues();
                            newRow.put(BusOpenHelper.col_routes, routeObject.getString("RouteNo"));
                            newRow.put(BusOpenHelper.col_heading, routeObject.getString("RouteHeading"));
                            newRow.put(BusOpenHelper.col_direction, routeObject.getString("Direction"));
                            newRow.put(BusOpenHelper.col_directionID, routeObject.getString("DirectionID"));
                            long id = db.insert(BusOpenHelper.Bus_TABLE_NAME, BusOpenHelper.col_routes, newRow);
                            routMsg.setId(id);
                        }

                        //call the following functions on the main GUI thread
                    getActivity().runOnUiThread(()->{
                        info.setText("Bus Stop " + stopNo + " " +stopDescription );

                        busAdt.notifyItemInserted(busList.size()-1);
                        dialog.hide();
                    });
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
        TextView busDestination;
        int position = -1;

        public RowViews(View itemView) {
            super(itemView);

            busInfo = itemView.findViewById(R.id.busInfo);
            busDestination = itemView.findViewById(R.id.destinationView);

            itemView.setOnClickListener(click ->{
                //start a new thread to search form the server
                Executor newThread2 = Executors.newSingleThreadExecutor();
                newThread2.execute(()->{
                    // this is the 2nd thread
                    try {
                        String stopNumber = searchBar.getText().toString();
                        String  routeNo= busInfo.getText().toString();
                        busDetailURL = "https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                                + URLEncoder.encode(stopNumber, "UTF-8")
                                + "&routeNo="
                                + routeNo.substring(7)
                                + "&format=json";

                        // create a URL object
                        URL url = new URL(busDetailURL);
                        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        String textDetails = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        //JSON object
                        JSONObject documentDetails = new JSONObject(textDetails);
                        JSONObject nextTripResults = documentDetails.getJSONObject("GetNextTripsForStopResult");
                        JSONObject routeInfo = nextTripResults.getJSONObject("Route");
                        JSONObject routeDirection = null;

                        try {
                            routeDirection = routeInfo.getJSONArray("RouteDirection").getJSONObject(0);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try{
                            routeDirection = routeInfo.getJSONObject("RouteDirection");
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                            BusRoute routeDetails = busList.get(position);
                            routeDetails.setBusNumber(routeDirection.getString("RouteNo"));
                            routeDetails.setDestination(routeDirection.getString("RouteLabel"));
                            JSONObject trips = routeDirection.getJSONObject("Trips");
                            JSONArray trip = trips.getJSONArray("Trip");
                            JSONObject firstElement = trip.getJSONObject(0);
                            routeDetails.setLongitude(firstElement.getString("Longitude"));
                            routeDetails.setLatitude(firstElement.getString("Latitude"));
                            routeDetails.setGpsSpeed(firstElement.getString("GPSSpeed"));
                            routeDetails.setStartTime(firstElement.getString("TripStartTime"));
                            routeDetails.setAdjustedTime(firstElement.getString("AdjustedScheduleTime"));



                        getActivity().runOnUiThread(() ->{

                            busAdt.notifyItemInserted(busList.size()-1);
                        });
                        BusSearch parentAcitivty = (BusSearch)getContext();
                        parentAcitivty.runOnUiThread(()->{
                            parentAcitivty.userClickedRoute(routeDetails,position);
                        });

                    } catch (IOException | JSONException ioe) {
                        Log.e("Connection error:", ioe.getMessage());
                    }
                });

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
            busRowLayout.busInfo.setText(busList.get(position).getBusNumber());
            busRowLayout.busDestination.setText(busList.get(position).getDirection());
            busRowLayout.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return busList.size();
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

    /**
     * This class is an internal class represents an adapter object
     */
    private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteRowViews>{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public FavoriteRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedView = inflater.inflate(R.layout.search_view_bus,parent,false);
            FavoriteRowViews initRow = new FavoriteRowViews(loadedView);
            return initRow;
        }
        @Override
        public void onBindViewHolder(FavoriteRowViews holder, int position) {

            holder.specificRoute.setText(favoriteList.get(position).getBusNumber());
            holder.headingDestination.setText(favoriteList.get(position).getDestination());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return favoriteList.size();
        }
    }

    /**
     * This class is an internal class represents each row in the favorite list
     */
    private class FavoriteRowViews extends RecyclerView.ViewHolder{

        TextView specificRoute;
        TextView headingDestination;
        int position = -1;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public FavoriteRowViews(View itemView) {
            super(itemView);
            specificRoute = itemView.findViewById(R.id.destinationView);
            headingDestination = itemView.findViewById(R.id.busInfo);

            // When click the specific bus route, go to the detailed information
            itemView.setOnClickListener(click ->{

            });

        }
        public void setPosition(int position) {
            this.position = position;
        }

    }
}
