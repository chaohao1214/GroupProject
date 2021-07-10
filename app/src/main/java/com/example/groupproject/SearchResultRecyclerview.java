package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultRecyclerview extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_recyclerview);

        searchResultList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(searchResultList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        searchResultList.add("Station 1");
        searchResultList.add("Station 2");
        searchResultList.add("Station 3");
        searchResultList.add("Station 4");
        searchResultList.add("Station 5");
        searchResultList.add("Station 6");
        searchResultList.add("Station 7");
        searchResultList.add("Station 8");
        searchResultList.add("Station 9");
        searchResultList.add("Station 10");
        searchResultList.add("Station 11");
        searchResultList.add("Station 12");
        searchResultList.add("Station 13");
        searchResultList.add("Station 14");
        searchResultList.add("Station 15");
        searchResultList.add("Station 16");
        searchResultList.add("Station 17");
        searchResultList.add("Station 18");
        searchResultList.add("Station 19");
        searchResultList.add("Station 20");
    }


}