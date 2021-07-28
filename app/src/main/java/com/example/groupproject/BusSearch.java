package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class BusSearch extends AppCompatActivity {

    BusListFragment busChatFragment;
    boolean busTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_empty_layout);

        // toolbar
        Toolbar myToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolBar, R.string.open, R.string.close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener(item -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
        // switch between tablet and phone
        busTablet = findViewById(R.id.busDetailsRoom) !=null;
        busChatFragment = new BusListFragment();
        FragmentManager busMgr = getSupportFragmentManager();
        FragmentTransaction busTX = busMgr.beginTransaction();
        busTX.add(R.id.fragmentRoomBus,busChatFragment);
        busTX.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch (item.getItemId()){
            case R.id.access_busApp:
                nextPage = new Intent(this, BusSearch.class);
                startActivity(nextPage);
                break;
            case R.id.access_carApp:
                // still need to add teammates class
                nextPage = new Intent();
                startActivity(nextPage);
                break;
            case R.id.access_movieApp:
                // still need to add teammates class
                nextPage = new Intent();
                startActivity(nextPage);
                break;
            case R.id.access_soccerApp:
                // still need to add teammates class
                nextPage = new Intent();
                startActivity(nextPage);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void busUserClickedMsg(BusRoute busRoute, int position){
        BusDetailsFragment busDetailsFragment = new BusDetailsFragment(busRoute,position);

        if (busTablet)
        {
            //table has a second fragmeLayout with id detailsBusRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().add(R.id.busDetailsRoom, busDetailsFragment).commit();
        } else // on a phone
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoomBus,busDetailsFragment).commit();
        }
    }

    public void notifyMessageDeleted(BusListFragment.BusMessage busMessage, int busChosenPosition) {
        busChatFragment.notifyMessageDeleted(busMessage, busChosenPosition);
    }
}