package com.example.groupproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            String title = null;
            String pubDate = null;
            String link = null;
            String description = null;
            String stringUrl = "http://www.goal.com/en/feeds/news?fmt=rss";

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");


                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("title")) {
                                xpp.next();

                                title = xpp.getText();

                            } else if (xpp.getName().equals("pubDate")) {
                                xpp.next();

                                pubDate = xpp.getText();
                            } else if (xpp.getName().equals("link")) {
                                xpp.next();
                                link = xpp.getText();
                            } else if (xpp.getName().equals("description")) {
                                xpp.next();
                                description = xpp.getText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (xpp.getName().equals("item")) {
                                //add article:
                                //articles.add(new Article(title, pubDate, link, description));
                            }
                            break;
                        case XmlPullParser.TEXT:

                            break;

                    }
                }
            } catch (IOException | XmlPullParserException ioe) {
                Log.e("Connection error:", ioe.getMessage());

            }

        });

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

     private class article {

    }

}

