package com.example.groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

public class BusDetailsFragment extends Fragment {
    BusListFragment.BusMessage busMessage;
    int busChosenPosition;

    public BusDetailsFragment(BusListFragment.BusMessage busMessage, int position) {
        this.busMessage = busMessage;
        busChosenPosition = position;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View busDetailsView = inflater.inflate(R.layout.bus_detail_layout, container, false);

        TextView busNumView = busDetailsView.findViewById(R.id.busNumView);
        TextView timeView = busDetailsView.findViewById(R.id.timeView);
        TextView idView = busDetailsView.findViewById(R.id.busTripView);
        TextView busSearchView = busDetailsView.findViewById(R.id.busGPSView);

        busNumView.setText("Bus number is: " + busMessage.getMessage());
        timeView.setText("Time send: " + busMessage.getTimeSearch());
        idView.setText("Database id is: " + busMessage.getId());
        busSearchView.setText("Bus search: " + busMessage.getBusMesg() );

        Button closeButton = busDetailsView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(closeClicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        Button deleteButton = busDetailsView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteClicked -> {
            SearchBus parentActivity = (SearchBus) getContext();
            parentActivity.notifyMessageDeleted(busMessage,busChosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return busDetailsView;
    }
}
