package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SoccerActivityThird extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_activity_third);
        Intent fromPrePage = getIntent();

        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);

        recyclerAdapter = new RecyclerAdapter(itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerAdapter);

        itemList.add("Football News, Live Scores, Results Transfers | Goal.com");

        for (int i =1; i<50 ; i++){
            itemList.add("Article"+(i));
        }

    }
}