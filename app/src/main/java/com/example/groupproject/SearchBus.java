package com.example.groupproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchBus extends AppCompatActivity {

    BusListFragment busChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_empty_layout);

        busChatFragment = new BusListFragment();
        FragmentManager busMgr = getSupportFragmentManager();
        FragmentTransaction busTX = busMgr.beginTransaction();
        busTX.add(R.id.fragmentBusRoom,busChatFragment);
        busTX.commit();
    }
}