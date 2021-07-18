package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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

        itemList = new LinkedList<>();

        //recyclerView = findViewById(R.id(R.id.recyclerView);

        Intent fromPrePage = getIntent();

        itemList.add("list1 ");

    }
}