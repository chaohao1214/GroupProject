package com.example.groupproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mainSoccerImageButton:

                Intent intent = new Intent(this, SoccerActivitySecond.class);
                startActivity(intent);


        }

        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu m) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, m);

        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbarMain = findViewById(R.id.toolbarMain);
        setSupportActionBar(myToolbarMain);

        DrawerLayout drawerMain = findViewById(R.id.main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerMain, myToolbarMain, R.string.openMain, R.string.closeMain);
        drawerMain.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_popout_menu);
        navigationView.setNavigationItemSelectedListener( (item)-> {

            onOptionsItemSelected(item);
            drawerMain.closeDrawer(GravityCompat.START);

            return false;
        });

        Button Soccer = findViewById(R.id.Soccer);
        Soccer.setOnClickListener(clk -> {


        });
    }


}