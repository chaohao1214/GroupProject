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
    Toolbar myToolbar;
    SharedPreferences prefs;
    EditText editName ;
    TextView welcome;
    Button editYourNameButton;
    Button toNextPage ;
    float savedRating;
    RatingBar rateBar ;
    // AlertDialog.Builder buildera = new AlertDialog.Builder(SoccerActivitySecond.this);


    // HELP MENU SOCCER SECOND ACTIVITY
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clickHelpbtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(SoccerActivitySecond.this);
                    builder.setMessage(R.string.instruction)
                            .setTitle(R.string.helpMenu)
                            .setIcon(R.drawable.yardim)
                            .setPositiveButton(R.string.ok, (dialog, cl) -> {
                            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu me) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_main_activity_actions, me);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_activity_second);




         myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.popout_menu);
//        navigationView.setNavigationItemSelectedListener( (item)-> {
//
//            onOptionsItemSelected(item);
//            drawer.closeDrawer(GravityCompat.START);
//
//            return false;
//        });
        rateBar = findViewById(R.id.ratingBar);
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        savedRating = prefs.getFloat("rating", -1);
        rateBar.setRating(savedRating);
        rateBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser)->{
            SharedPreferences.Editor edit = prefs.edit();
            edit.putFloat("rating", rating);
            edit.commit();
        });

        // GRABBING
        editName = findViewById(R.id.editName);
        welcome = findViewById(R.id.welcome);
        editYourNameButton = findViewById(R.id.editYourNameButton);

        toNextPage = findViewById(R.id.toNextPage);

        //called methods
        showToastMessage();
        ratingBarDialogBox();

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
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle(R.string.likeApp);
        popDialog.setPositiveButton(R.string.yes, (dialog, cl) ->{

                        dialog.dismiss();
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        popDialog.create();
        popDialog.show();
    }

}
