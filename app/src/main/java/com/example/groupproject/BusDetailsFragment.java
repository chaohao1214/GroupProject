package com.example.groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * This class is a fragment to display the detailed information of the routes
 * @author Chaohao
 * @version 1.0
 */
public class BusDetailsFragment extends Fragment {
    BusRoute chosenRoute;
    int busChosenPosition;

    /**
     * constructor for the detail fragment
     * @param route
     * @param position
     */
    public BusDetailsFragment(BusRoute route, int position) {
        chosenRoute = route;
        busChosenPosition = position;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View busDetailsView = inflater.inflate(R.layout.bus_detail_layout, container, false);

        TextView busNumView = busDetailsView.findViewById(R.id.tvRouteNo);
        TextView destinationView = busDetailsView.findViewById(R.id.tvDestination);
        TextView latitudeView = busDetailsView.findViewById(R.id.tvLatitude);
        TextView longitudeView = busDetailsView.findViewById(R.id.tvLongitude);
        TextView gpsView = busDetailsView.findViewById(R.id.tvGps);
        TextView startTimeView = busDetailsView.findViewById(R.id.tvStartTime);
        TextView adjustedView = busDetailsView.findViewById(R.id.tvAdjustedTime);

        busNumView.setText("Bus route is: " + chosenRoute.getBusNumber());
        destinationView.setText("Destination: " + chosenRoute.getDestination());
        latitudeView.setText("Latitude: " + chosenRoute.getLatitude());
        longitudeView.setText("Longitude: " + chosenRoute.getLongitude());
        gpsView.setText("GPS: " + chosenRoute.getGpsSpeed());
        startTimeView.setText("Start time: " + chosenRoute.getStartTime());
        adjustedView.setText("Adjusted time: " + chosenRoute.getAdjustedTime());




        Button closeButton = busDetailsView.findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(closeClicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });


        Button favoriteBtn = busDetailsView.findViewById(R.id.saveButton);
        favoriteBtn.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to save this route?")
                    .setTitle("Questions: ")
                    .setPositiveButton("Yes", (dialog,cl) -> {
                        BusSearch parent=(BusSearch) getContext();
                        parent.notifyFavorite(chosenRoute);
                    })
                    .setNegativeButton("No",(dialog,cl) ->{})
                    .create().show();
        });


        return busDetailsView;
    }
}
