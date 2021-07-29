package com.example.groupproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SoccerActivitySecond extends AppCompatActivity {
    RatingBar ratingBar ;
    AlertDialog.Builder popDialog;
   // AlertDialog.Builder buildera = new AlertDialog.Builder(SoccerActivitySecond.this);

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clickHelp:

                AlertDialog.Builder builder = new AlertDialog.Builder(SoccerActivitySecond.this);

                    builder.setMessage(R.string.instruction)
                            .setTitle("HELP MENU")
                            .setIcon(R.drawable.yardim)
                            .setPositiveButton("OK", (dialog, cl) -> {


                            }).create().show();
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
        setContentView(R.layout.soccer_activity_second);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
//
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener( (item)-> {

            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);

            return false;
        });












        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        // GRABBING
        EditText editName = findViewById(R.id.editName);
        TextView welcome = findViewById(R.id.welcome);
        Button editYourNameButton = findViewById(R.id.editYourNameButton);

        Button toNextPage = findViewById(R.id.toNextPage);


        Intent fromPrePage = getIntent();

        //called methods
        showToastMessage();
        ratingBarDialogBox();
        editYourName();

        editName.setText(prefs.getString("Name", ""));
        editYourNameButton.setOnClickListener(clk->{
            String greeting = getString(R.string.greeting);
            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("Name", editName.getText().toString());
            editor.apply();
            welcome.setText(greeting.concat(" ")+editName.getText().toString().toUpperCase());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(SoccerActivitySecond.this);
        toNextPage.setOnClickListener( clk ->{
            builder.setMessage("Are you sure you want to go to the next page ? \n" )
                    .setTitle("Question")
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .setPositiveButton("Yes", (dialog, cl) -> {

                        openSoccerGamesApiApp();

                    }).create().show();

        });

    }
    public void thankyouToast(){
        Context context = getApplicationContext();
        CharSequence message = "Thank you";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void openSoccerGamesApiApp(){
        Intent intent = new Intent(this, SoccerActivityThird.class);
        startActivity(intent);
    }
    public void showToastMessage( ){
        Context context = getApplicationContext();
        CharSequence message = "Hello, Welcome to Salih's App!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @SuppressLint("ResourceType")
    public void ratingBarDialogBox () {

        popDialog = new AlertDialog.Builder(this);
        ratingBar = new RatingBar(this);
        popDialog.setView(ratingBar);
        //ratingBar.setMax(6);
        //ratingBar.setNumStars(5);
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Did you like the app? ");



        popDialog.setPositiveButton("Yes", (dialog, cl) ->{
                        thankyouToast();
                        dialog.dismiss();

                        })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        thankyouToast();
                        dialog.cancel();

                    }
                });
        popDialog.create();
        popDialog.show();

    }
    public void editYourName ( ){


    }

}
