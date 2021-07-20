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

        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);



        recyclerAdapter = new RecyclerAdapter(itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setAdapter(recyclerAdapter);

        Intent fromPrePage = getIntent();

        itemList.add("Aricle 1");
        itemList.add("Aricle 2");
        itemList.add("Aricle 3");
        itemList.add("Aricle 4");
        itemList.add("Aricle 5");
        itemList.add("Aricle 6");
        itemList.add("Aricle 7");
        itemList.add("Aricle 8");
        itemList.add("Aricle 9");
        itemList.add("Aricle 10");
        itemList.add("Aricle 11");
        itemList.add("Aricle 12");
        itemList.add("Aricle 13");
        itemList.add("Aricle 14");
        itemList.add("Aricle 15");
        itemList.add("Aricle 16");



    }
}