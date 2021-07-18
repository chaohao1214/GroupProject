package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class SoccerActivitySecond extends AppCompatActivity {
    RatingBar ratingBar ;
    AlertDialog.Builder popDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_activity_second);

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

        editYourNameButton.setOnClickListener(clk->{
            String greeting = getString(R.string.greeting);
            welcome.setText(greeting.concat(" ")+editName.getText().toString().toUpperCase());
        });
        toNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoccerGamesApiApp();
            }
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
        ratingBar.setNumStars(5);
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
