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
import android.widget.ProgressBar;
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

/**
 * This class is created for the third page of Soccer api games
 * It includes nested classes, private variables and abstract, parent classes
 * In the oncreate method ,
 */
public class SoccerActivityThird extends AppCompatActivity {
    /** This holds recyclerView */
    private RecyclerView recyclerView;
    /** This holds recyclerAdapter */
    private RecyclerAdapter recyclerAdapter;
    /** This holds the list of the item listed on the left side of the screen */
    private List<String> itemList;
    /** This holds the list of the article news that are retrieved from XML file */
    private List<Article> articles ;
    /** This holds the progressbar. It is used when loading data from XML  */
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_activity_third);

        itemList = new LinkedList<>();

        recyclerView = findViewById(R.id.recycler);

        pb = findViewById(R.id.progressBar);


        recyclerAdapter = new RecyclerAdapter(itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerAdapter);

        itemList.add("Football News, Live Scores, Results Transfers | Goal.com");
        itemList.add("Transfer news and rumours LIVE: Inter eye Martial as Lukaku replacement");
        itemList.add("Roma join Arsenal and Atalanta in race to sign Chelsea's Abraham");
        itemList.add("Pique admits ‘Barcelona are a bit broken’ despite 3-0 Juventus victory");
        itemList.add("Hearts of Oak beat Ashanti Gold in Ghana FA Cup final to seal season double");
        itemList.add("Koeman: There’s no Messi but Barcelona are excited about this season");
        itemList.add("LIVE: Barcelona vs Juventus");
        itemList.add("Klopp hints at Liverpool transfers before summer window ends");
        itemList.add("How Benni McCarthy helped improve Bafana Bafana hopeful Makhaula's game");
        itemList.add("Reyna: Sancho told me to take the No. 7 shirt before Man Utd move");
        itemList.add("Football News, Live Scores, Results Transfers | Goal.com");
        itemList.add("Transfer news and rumours LIVE: Inter eye Martial as Lukaku replacement");
        itemList.add("Roma join Arsenal and Atalanta in race to sign Chelsea's Abraham");
        itemList.add("Pique admits ‘Barcelona are a bit broken’ despite 3-0 Juventus victory");
        itemList.add("Hearts of Oak beat Ashanti Gold in Ghana FA Cup final to seal season double");
        itemList.add("Koeman: There’s no Messi but Barcelona are excited about this season");
        itemList.add("LIVE: Barcelona vs Juventus");
        itemList.add("Klopp hints at Liverpool transfers before summer window ends");
        itemList.add("How Benni McCarthy helped improve Bafana Bafana hopeful Makhaula's game");
        itemList.add("Reyna: Sancho told me to take the No. 7 shirt before Man Utd move");


        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {

            String stringUrl = "http://www.goal.com/en/feeds/news?fmt=rss";

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");

                String title = null;
                String date = null;
                String link = null;
                String description = null;
                String imgURL = null;

                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("title")) {
                                xpp.next();

                                title = xpp.getText();

                            } else if (xpp.getName().equals("pubDate")) {
                                xpp.next();

                                date = xpp.getText();
                            } else if (xpp.getName().equals("link")) {
                                xpp.next();
                                link = xpp.getText();
                            } else if (xpp.getName().equals("description")) {
                                xpp.next();
                                description = xpp.getText();
                            }
                            break;

                        case XmlPullParser.END_TAG: {
                                if (title != null && date != null && link != null && description != null) {
                                    articles.add(new Article(title, link, date, description, imgURL));
                                }
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

    /**
     *
     */
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


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

        /**
         * This class shows an item description
         * inside the recyclerView
         */
        class ViewHolder extends RecyclerView.ViewHolder{


            TextView title ;


            /**
             * This method has a event role
             * whenever user click on the item
             * alert dialog will pop up
             * Also inside the method AlertDialog object
             * created.
             * @param itemView
             */
            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.titleText);
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

    /**
     * This class stores variable, constructors and getters for the
     * article news.
     */
     private class Article {

         private String title;
         private String url;
         private String date;
         private String description;
         private String imgLink;

         public Article(String title, String url, String date, String description, String imgLink) {
             this.title = title;
             this.url = url;
             this.date = date;
             this.description = description;
             this.imgLink = imgLink;
         }

         public String getTitle() {
             return title;
         }

         public String getUrl() {
             return url;
         }

         public String getDate() {
             return date;
         }

         public String getDescription() {
             return description;
         }

         public String getImgLink() {
             return imgLink;
         }
     }

}

