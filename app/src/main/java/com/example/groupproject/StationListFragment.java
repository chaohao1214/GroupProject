package com.example.groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StationListFragment extends Fragment {
    RecyclerView stationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View theList = inflater.inflate(R.layout.station_fragment_layout, container, false);

        stationList = theList.findViewById(R.id.recycler_view);
       


        return theList;




    }
}
