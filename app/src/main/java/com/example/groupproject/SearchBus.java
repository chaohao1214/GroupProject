package com.example.groupproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchBus extends AppCompatActivity {

    BusListFragment busChatFragment;
    boolean busTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_empty_layout);

        busTablet = findViewById(R.id.busDetailsRoom) !=null;

        busChatFragment = new BusListFragment();
        FragmentManager busMgr = getSupportFragmentManager();
        FragmentTransaction busTX = busMgr.beginTransaction();
        busTX.add(R.id.fragmentBusRoom,busChatFragment);
        busTX.commit();
    }

    public void busUserClickedMsg(BusListFragment.BusMessage busMessage, int position){
        BusDetailsFragment busDetailsFragment = new BusDetailsFragment(busMessage,position);

        if (busTablet)
        {
            //table has a second fragmeLayout with id detailsBusRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().add(R.id.busDetailsRoom, busDetailsFragment).commit();
        } else // on a phone
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentBusRoom,busDetailsFragment).commit();
        }
    }

    public void notifyMessageDeleted(BusListFragment.BusMessage busMessage, int busChosenPosition) {
        busChatFragment.notifyMessageDeleted(busMessage, busChosenPosition);
    }
}