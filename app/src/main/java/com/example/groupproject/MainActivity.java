package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        Button Soccer = findViewById(R.id.Soccer);
        Soccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoccerGamesApiApp();
            }
        });
    }

    public void openSoccerGamesApiApp(){
        Intent intent = new Intent(this, SoccerActivitySecond.class);
        startActivity(intent);
    }


}