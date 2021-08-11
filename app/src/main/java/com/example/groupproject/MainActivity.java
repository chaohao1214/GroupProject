package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

/**
 * This class has the menu and buttons for starting the activities.
 */
public class MainActivity extends AppCompatActivity {

    Toolbar main_menu;
    String description = "Final Project\nAuthors:\nShakib Ahmed\nWeiping Guo\nChaohao Zhu\nSalih Ensarioglu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button stationLocator = findViewById(R.id.stationLocatorButton);

        main_menu = findViewById(R.id.stationMenu);
        setSupportActionBar(main_menu);

        stationLocator.setOnClickListener(clk -> {
            // go to next page
            Intent nextPageStation = new Intent(MainActivity.this, StationSearch.class);
            startActivity(nextPageStation);
        });
    }

    /**
     * This method starts the Electric Car Charging Station Locator Activity
     */

    public void startStationLocatorActivity() {
        Intent stationLocatorActivity = new Intent(this, StationSearch.class);
        startActivity(stationLocatorActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.car_charge_activity_opener:
                startStationLocatorActivity();
                break;

            case R.id.show_help:
                AlertDialog.Builder helpAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                helpAlertBuilder.setTitle("Help");
                helpAlertBuilder.setMessage(description);
                helpAlertBuilder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
