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

/**
 * This class is a welcome page.it includes help instruction in a toolbar,
 * edit text, and a rating bar, alert dialog and toast message.
 */
public class SoccerActivitySecond extends AppCompatActivity {
    /** this variable holds the ratingbar*/
    RatingBar ratingBar ;
    /** This holds alert Dialog for pop dialog*/
    AlertDialog.Builder popDialog;
    /** This hold a toolbar which is at the top of screen*/
    AlertDialog.Builder builder ;
    /** This hold a builder of AlertDialog class*/
    Toolbar myToolbar;
    /** This holds prefs to save values in the file*/
    SharedPreferences prefs;
    /** this holds edit name variable so that people can edit */
    EditText editName ;
    /** This is welcome variable starting of the app */
    TextView welcome;
    /** this holds the button to edit the name*/
    Button editYourNameButton;
    /** this holds the button when clicking it goes to the next page*/
    Button toNextPage ;
    /** This is used to save the rating */
    float savedRating;
    /** This holds the ratingbar when the app started*/
    RatingBar rateBar ;




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

        builder = new AlertDialog.Builder(SoccerActivitySecond.this);
        toNextPage.setOnClickListener( clk ->{
            builder.setMessage("Are you sure you want to go to the next page ? \n" )
                    .setTitle("Question")
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        openSoccerGamesApiApp();
                    }).create().show();
        });

    }

    /**
     *This function uses intent object to help the user to get the next
     * page. User can go from second activity to the third activity
     */
    public void openSoccerGamesApiApp(){
        Intent intent = new Intent(this, SoccerActivityThird.class);
        startActivity(intent);
    }

    /**
     * This function shows a toast message when the app opened
     */
    public void showToastMessage( ){
        Context context = getApplicationContext();
        CharSequence message = "Hello, Welcome to Salih's App!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * This functions shows a dialogbox when user start the app
     * The alert dialog asks the user if he or she liked the app
     */
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
