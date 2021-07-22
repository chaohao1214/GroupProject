package com.example.groupproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

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
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private static final String TAG = "RecyclerAdapter";
        List<String> itemList;
        public RecyclerAdapter(List<String> itemList) {
            this.itemList = itemList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.soccer_row_item, parent,false);
            ViewHolder viewholder = new ViewHolder(view);


            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
            holder.title.setText(itemList.get(position));
            //  holder.itemTitle.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{


            TextView title ;

            public ViewHolder(View itemView) {
                super(itemView);

                // imageView = itemView.findViewById(R.id.imageView);
                title = itemView.findViewById(R.id.titleText);
                //value = itemView.findViewById(R.id.valueText);
                itemView.setOnClickListener(click -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SoccerActivityThird.this);

                    builder.setMessage("Do you want to delete this message ? \n" )
                            .setTitle("Question")
                            .setNegativeButton("No", (dialog, cl) -> {})
                            .setPositiveButton("Yes", (dialog, cl) -> {

                            }).create().show();

                    Snackbar.make(click,"You deleted message #",Snackbar.LENGTH_LONG).setAction("Undo", clk ->{



                    }).show();

                });

            }
        }

    }

}

