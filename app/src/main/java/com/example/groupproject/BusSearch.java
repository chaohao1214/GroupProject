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

/**
 * final project: search bus route information by bus stop number.
 * Users can search bus route information, which includes the route number, trip destination, latitude,
 * longitude, gps speed, start time and adjusted schedule, by inputing bus stop number, and put the search
 * result in the favorite list.
 * @author Chaohao
 * @version 1.0
 *
 */
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

    /**
     * this function uses to create menu bar on the top of the app page
     * @param menu the menu bar can connect to menu xml
     * @return initialize the content and allow us to put items to the menu bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * in the menu there are 4 icons: bus, station, movie, soccer. Those icons
     * can connect to each app's function
     * @param item item can be 4 class files
     * @return when item is selected, it will jump to another function
     */
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

    /**
     *  This function is to insert the chosen route in the favorite list.
     * @param chosenRoute the route that the user choose to put into the favorite list
     */
    public void notifyFavorite(BusRoute chosenRoute) {
        busChatFragment.insertFavorite(chosenRoute);
    }

    /**
     * This function is to display different layout depending on if the emulator is a phone or tablet.
     * @param busRoute the bus route that displayed on the screen
     * @param position the position of the bus route in the list
     */
    public void userClickedRoute(BusRoute busRoute, int position) {
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
}