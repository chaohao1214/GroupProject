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

public class MainActivity extends AppCompatActivity {

    Toolbar main_menu;
    String description = "Final Project\nAuthors:\nShakib Ahmed\nWeiping Guo\nChaohao Zhu\nSalih Ensarioglu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button carCharge = findViewById(R.id.CarCharge);

        main_menu = findViewById(R.id.carMenu);
        setSupportActionBar(main_menu);

        carCharge.setOnClickListener(clk -> {
            // go to next page
            Intent nextPageCar = new Intent(MainActivity.this, CarChargingStation.class);
            startActivity(nextPageCar);
        });
    }

    public void startCarChargingActivity() {
        Intent chargingActivity = new Intent(this, CarChargingStation.class);
        startActivity(chargingActivity);
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
                startCarChargingActivity();
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
