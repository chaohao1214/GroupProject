package com.example.groupproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Date;


public class MovieInfo extends AppCompatActivity {
    RecyclerView movieList;
    private SharedPreferences prefs;
    MovieAdapter mvAdapter = new MovieAdapter();
    ArrayList<MovieMessage> movieArray = new ArrayList<>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_movie);

       prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       String movieInfo = prefs.getString("MovieTitle", "");
       EditText movieMsg = findViewById(R.id.searchTitle);
       movieMsg.setText(movieInfo);

       Button searchBtn = findViewById(R.id.searchButton);
       movieList = findViewById(R.id.movieList);
       LinearLayoutManager llm = new LinearLayoutManager(this);
       llm.setStackFromEnd(true);
       llm.setReverseLayout(true);

       movieList.setLayoutManager(llm);
       movieList.setAdapter(mvAdapter);

       searchBtn.setOnClickListener(clk -> {

           MovieMessage thisMessage = new MovieMessage(movieMsg.getText().toString(), 1, new Date());
           movieArray.add(thisMessage);
           mvAdapter.notifyItemInserted(movieArray.size() - 1);

           SharedPreferences.Editor editor = prefs.edit();
           editor.putString("MovieTitle", movieMsg.getText().toString());
           movieMsg.setText("");
           editor.apply();
           Toast.makeText(getApplicationContext(), "Finding your movie...", Toast.LENGTH_SHORT).show();
       });
   }

   private class RowViews extends RecyclerView.ViewHolder {
       TextView mvInfo;
   //  TextView timeInfo;
       int position = -1;

       RowViews(View itemView) {
           super(itemView);
       // timeInfo = itemView.findViewById(R.id.timeInfo);
           mvInfo = itemView.findViewById(R.id.movieInfo);
           itemView.setOnClickListener(clk -> {
               AlertDialog.Builder builder = new AlertDialog.Builder(MovieInfo.this);
               builder.setMessage("Do you want to delete this movie: " + mvInfo.getText())
                           .setPositiveButton("Yes", (dialog, cl) -> {
                           position = getAdapterPosition();
                           MovieMessage removeMsg = movieArray.get(position);
                               movieArray.remove(position);
                           mvAdapter.notifyItemRemoved(position);
                           Snackbar.make(mvInfo, "You deleted movie #" + position, Snackbar.LENGTH_LONG)
                                   .setAction("Undo", click -> {
                                       movieArray.add(position, removeMsg);
                                       mvAdapter.notifyItemInserted(position);
                                   })
                                   .show();
                 })
                       .setNegativeButton("No", (dialog, cl) -> {
               })
                       .create().show();

           });
       }

       public void setPosition(int position) {
           this.position = position;
       }

   }

       private class MovieAdapter extends RecyclerView.Adapter {

           @Override
           public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View vw = getLayoutInflater().inflate(R.layout.search_view_movie, parent, false);
               return new RowViews(vw);
           }

           @Override
           public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
               RowViews movieLayout = (RowViews) holder;
               movieLayout.mvInfo.setText(movieArray.get(position).getMessage());
       //       movieLayout.timeInfo.setText(sdf.format(movieMsgs.get(position).getTime()));
               movieLayout.setPosition(position);
           }

           @Override
           public int getItemCount() {
               return movieArray.size();
           }

       }

       private class MovieMessage {
           String msg;
           int order;
           Date searchTime;

           public MovieMessage(String msg, int order, Date searchTime) {
               this.msg = msg;
               this.order = order;
               this.searchTime = searchTime;
           }

           public String getMessage() {
               return msg;
           }

           public int getOrder() {
               return order;
           }

           public Date getTime() {
               return searchTime;
           }

       }
   }



