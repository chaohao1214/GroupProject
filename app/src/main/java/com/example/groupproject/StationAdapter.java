package com.example.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * This Class is the adapter for the ListView
 */
public class StationAdapter extends BaseAdapter {
    /**
     * list to hold the car charging station objects.
     */
    private ArrayList<StationObject> StationList;
    /**
     * context of the current state of an application
     */
    private Context mContext;
    /**
     * marker of a full or partial information of a car charging station displayed
     * in an item view (needed for getView() method)
     */
    private Boolean fullInfo;

    /**
     * Constructor for an adapter
     * @param context current state of an application
     * @param chargingStationList list of car charging stations objects
     * @param fullInfo marker of a full details for an item view displayed on screen
     */
    public StationAdapter(Context context, ArrayList<StationObject> chargingStationList, Boolean fullInfo) {
        super();
        this.mContext = context;
        this.StationList = chargingStationList;
        this.fullInfo = fullInfo;
    }

    /**
     * Methods counts how many car charging stations are in a list
     * @return number of stations in a list
     */
    @Override
    public int getCount() {
        return StationList.size();
    }

    /**
     * Methods gets a car charging station from a list
     * @param i position of a station in a list
     * @return ChargingStationObject car charging station
     */
    @Override
    public StationObject getItem(int i) {
        return StationList.get(i);
    }

    /**
     * Methods gets a station's id from a database
     * @param i position of a station in a list
     * @return id of a station
     */
    @Override
    public long getItemId(int i) {
        return (getItem(i)).getId();
    }

    /**
     * Method returns a view for a car charging station
     * @param i position of a station in a list
     * @param view recycled view
     * @param viewGroup view that can contain other views
     * @return view of a car charging station (row to the ListView)
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View newView = inflater.inflate(R.layout.activity_car_charging_station_row, viewGroup, false);

        StationObject row = getItem(i);
        TextView rowDetails = (TextView) newView.findViewById(R.id.row_title);

        if(fullInfo) {
            rowDetails.setText(String.format("Station: %s\n Latitude: %.2f\n Longitude: %.2f\n Phone: %s", row.getTitle(), row.getLatitude(),
                    row.getLongitude(), row.getPhone()));
        }
        else{
            rowDetails.setText(row.getTitle());
        }
        return newView;
    }
}

