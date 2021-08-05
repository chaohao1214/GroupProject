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

    /**The strings represents the address of the server we will connect to */

    private String stringURL;
    private String detailURL;

    /**
     *  Declare search bar, search button, help button and search view
     */

    RecyclerView searchView;
    TextView information;
    EditText searchBar;
    Button searchBtn;
    Button helpBtn;
    Button favoriteBtn;
    MyAdapter adt = new MyAdapter();
    FavoriteAdapter favorAdt = new FavoriteAdapter();
    SQLiteDatabase db;

    /**
     * create a list to hold the searching result
     */
    ArrayList<BusRoute> routeList = new ArrayList<>();
    /**
     * create a list to hold the favorite routes
     */
    ArrayList<BusRoute> favoriteList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View routeLayout = inflater.inflate(R.layout.bus_search, container, false);

        //Find the widgets
        searchView = routeLayout.findViewById(R.id.searchView);
        information = routeLayout.findViewById(R.id.info);
        searchBar = routeLayout.findViewById(R.id.searchBox);
        searchBtn = routeLayout.findViewById(R.id.searchBusButton);
        favoriteBtn = routeLayout.findViewById(R.id.favoriteButton);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        searchView.setLayoutManager(manager);

        //Create an SQLiteOpenHelper object
        BusOpenHelper opener = new BusOpenHelper(getContext());

        //Return a SQLiteDatabase object that allows inserting,updating and deleting
        db = opener.getWritableDatabase();


        /**
         * Create sharedPreferences object to save the bus route number that is typed in the search bar
         */

        SharedPreferences preferences = getContext().getSharedPreferences("MyBusStopData", Context.MODE_PRIVATE);
        String busInfo = preferences.getString("StopNumber","");
        searchBar.setText(busInfo);


        // When click the search button, pull the data from the server and put the date in the arrray list and database
        searchBtn.setOnClickListener(click->{
            searchView.setAdapter(adt);
            String busStop = searchBar.getText().toString();

            //Toast to show the application message
            Toast.makeText(getContext(), "Ottawa bus route app, written by Chaohao Zhu", Toast.LENGTH_LONG).show();

            // Create a progress bar when the app is getting data from the server
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Bus Stop Information")
                    .setMessage("We'are getting information for the bus stop  " + busStop + "to check the route information for this stop")
                    .setView(new ProgressBar(getContext()))
                    .show();

            //Start a new thread to search from the server
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(()->{
                //This runs on another thread
                try{
                    String stopNumber = searchBar.getText().toString();
                    stringURL = "https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                            + URLEncoder.encode(stopNumber, "UTF-8")
                            + "&format=json";
                    //Create a URL object
                    URL url = new URL(stringURL);
                    //Connect to the server
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //Wait for a response from the server
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    //Convert to a string
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    //Convert string to JSON object
                    JSONObject theDocument = new JSONObject(text);
                    JSONObject routeSummary = theDocument.getJSONObject("GetRouteSummaryForStopResult");
                    JSONObject routes = routeSummary.getJSONObject("Routes");
                    String stopNo = routeSummary.getString("StopNo");
                    String stopDescription = routeSummary.getString("StopDescription");
                    //Get the array for the routes information
                    JSONArray routeArray = routes.getJSONArray("Route");


                    for(int i = 0; i < routeArray.length(); i++){
                        //put the routes information into the list
                        JSONObject routeObject = routeArray.getJSONObject(i);
                        BusRoute routeMsg = new BusRoute();
                        routeMsg.setBusNumber("Route: " + routeObject.getString("RouteNo"));
                        routeMsg.setDestination("Heading " + routeObject.getString("RouteHeading"));
                        routeMsg.setDirection(routeObject.getString("Direction"));
                        routeMsg.setDirectionID(routeObject.getString("DirectionID"));
                        routeList.add(routeMsg);
                    }


                    //Call the following functions on the main GUI thread
                    getActivity().runOnUiThread(()->{

                        information.setText( stopNo + " " + stopDescription + " has the following routes. "
                                + "Please click the routes for detailed information");

                        adt.notifyItemInserted(routeList.size()-1);

                        dialog.hide();
                    });

                }
                catch (IOException | JSONException ioe) {
                    Log.e("Connection error:", ioe.getMessage());
                }

            });

            //Save the inputed bus route to the String "BusNumber".
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("StopNumber",searchBar.getText().toString());
            editor.apply();

        });

        favoriteBtn.setOnClickListener(cl->{
            //Put the information in the database to the favorite list
            favoriteList.clear();
            Cursor results = db.rawQuery("Select * from " + BusOpenHelper.Bus_TABLE_NAME + ";", null);
            int _idCol = results.getColumnIndex("_id");
            int routeNumber = results.getColumnIndex(BusOpenHelper.col_routes);
            int destination = results.getColumnIndex(BusOpenHelper.col_heading);
            int direction = results.getColumnIndex(BusOpenHelper.col_direction);
            int directionId = results.getColumnIndex(BusOpenHelper.col_directionID);

            while(results.moveToNext()) {
                long id = results.getInt(_idCol);
                String rtNumber = results.getString(routeNumber);
                String dest = results.getString(destination);
                String direc = results.getString(direction);
                String direcID = results.getString(directionId);

                favoriteList.add(new BusRoute(rtNumber, dest, direc, direcID,id));
            }

            //Call the following functions on the main GUI thread
            getActivity().runOnUiThread(()->{

                information.setText("My Favorite: ");
            });
            searchView.setAdapter(favorAdt);
        });


        // Show the hlep button information
        helpBtn = routeLayout.findViewById(R.id.Bus_help);
        helpBtn.setOnClickListener(click -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

            alertDialogBuilder.setTitle(R.string.bus_help)
                    .setMessage(R.string.help_content_bus)
                    .setPositiveButton(R.string.close, (c, arg) -> {
                    })
                    .create().show();
        });

        return routeLayout;
    }

    /**
     * This function is to save the chosen route to the favorite list
     * @param chosenRoute the route that the user choose
     */
    public void insertFavorite(BusRoute chosenRoute) {

        //put the routes information into the database
        ContentValues newRow = new ContentValues();
        newRow.put(BusOpenHelper.col_routes,chosenRoute.getBusNumber());
        newRow.put(BusOpenHelper.col_heading,chosenRoute.getDestination());
        newRow.put(BusOpenHelper.col_direction,chosenRoute.getDirection());
        newRow.put(BusOpenHelper.col_directionID,chosenRoute.getDirectionID());
        Long id=db.insert(BusOpenHelper.Bus_TABLE_NAME,BusOpenHelper.col_routes,newRow);
        String[] arr={id.toString()};
        Snackbar.make(searchView, "You have saved this route", Snackbar.LENGTH_LONG)
                .setAction("Undo", clk ->{
                    db.delete(BusOpenHelper.Bus_TABLE_NAME,"_id=?",arr);
                    Toast.makeText(getContext(), "You have deleted this route", Toast.LENGTH_LONG).show();
                })
                .show();
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
            specificRoute = itemView.findViewById(R.id.specificRoute);
            headingDestination = itemView.findViewById(R.id.heading);

        }
        public void setPosition(int position) {
            this.position = position;
        }

    }

    /**
     * This class is an internal class represents an adapter object
     */
    private class MyAdapter extends RecyclerView.Adapter<MyRowViews>{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedView = inflater.inflate(R.layout.search_view_bus,parent,false);
            MyRowViews initRow = new MyRowViews(loadedView);
            return initRow;
        }
        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {

            holder.specificRoute.setText(routeList.get(position).getBusNumber());
            holder.headingDestination.setText(routeList.get(position).getDestination());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return routeList.size();
        }
    }


    /**
     * This class is an internal class represents each row in the list
     */
    private class MyRowViews extends RecyclerView.ViewHolder{

        TextView specificRoute;
        TextView headingDestination;
        int position = -1;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public MyRowViews(View itemView) {
            super(itemView);
            specificRoute = itemView.findViewById(R.id.specificRoute);
            headingDestination = itemView.findViewById(R.id.heading);

            // When click the specific bus route, go to the detailed information
            itemView.setOnClickListener(click ->{

                //Start a new thread to search from the server
                Executor newThread1 = Executors.newSingleThreadExecutor();
                int position = getAdapterPosition();
                newThread1.execute(()->{
                    //This runs on another thread
                    try{
                        String stopNumber = searchBar.getText().toString();
                        String routeNo = specificRoute.getText().toString();
                        detailURL = "https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                                + URLEncoder.encode(stopNumber, "UTF-8")
                                + "&routeNo="
                                + routeNo.substring(7)
                                + "&format=json";

                        URL url1 = new URL(detailURL);
                        HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
                        InputStream in1 = new BufferedInputStream(urlConnection1.getInputStream());
                        String textDetails = (new BufferedReader(
                                new InputStreamReader(in1, StandardCharsets.UTF_8)))
                                .lines()
                                .collect(Collectors.joining("\n"));
                        JSONObject theDocumentDetails = new JSONObject(textDetails);
                        JSONObject nextTripResults = theDocumentDetails.getJSONObject("GetNextTripsForStopResult");
                        JSONObject routeInfo = nextTripResults.getJSONObject("Route");
                        JSONObject routeDirection =null;
                        try{
                            routeDirection = routeInfo.getJSONArray("RouteDirection").getJSONObject(0);
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        try{
                            routeDirection = routeInfo.getJSONObject("RouteDirection");
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                        BusRoute routeDetails = routeList.get(position);
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


                        //Call the following function on the main GUI thread
                        getActivity().runOnUiThread(()->{

                            adt.notifyItemInserted(routeList.size()-1);

                        });
                        BusSearch parentActivity = (BusSearch) getContext();
                        parentActivity.runOnUiThread(()->{
                            parentActivity.userClickedRoute(routeDetails, position);
                        });

                    }
                    catch (IOException | JSONException ioe) {
                        Log.e("Connection error:", ioe.getMessage());
                    }

                });
            });

        }
        public void setPosition(int position) {
            this.position = position;
        }

    }
}
