package com.example.groupproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MovieInfo extends AppCompatActivity {

    Button searchBtn;
    EditText movieSearchText;
    ImageView favorite;
    ImageView homePage;
    ImageView help;
    private SharedPreferences prefs;
    private String stringURL;
    MovieFragment_WPG movieFragment;
    FavoriteFragment_WPG favoriteFragment;

    FragmentTransaction tx;

    FragmentManager fMnger;

//    RecyclerView movieList;

//    MovieAdapter mvAdapter = new MovieAdapter();
//    ArrayList<MovieMessage> movieArray = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wpg, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch(item.getItemId()){
//            case R.id.busApp_movie:
//                nextPage = new Intent(this,OCTranspo.class);
//                startActivity(nextPage);
//                break;
//            case R.id.chargerApp_movie:
//                nextPage = new Intent(this, CarStationSearch.class);
//                startActivity(nextPage);
//                break;
            case R.id.movieApp_movie:
                nextPage = new Intent( this, MovieInfo.class);
                startActivity(nextPage );
                break;
//            case R.id.soccerApp_movie:
//                nextPage = new Intent(this, SoccerNewsFeedActivity.class);
//                startActivity(nextPage);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_movie );

        Toolbar toolbar_wpg = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_wpg);

        DrawerLayout drawer_wpg = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_wpg, toolbar_wpg, R.string.open, R.string.close);
        drawer_wpg.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView_wpg = findViewById(R.id.popout_menu);
        navigationView_wpg.setNavigationItemSelectedListener(( item) -> {
            onOptionsItemSelected(item);//call the function for the other toolbar
            drawer_wpg.closeDrawer(GravityCompat.START);
            return false;
        });

      //  searchBtn = (Button) findViewById(R.id.searchButton);
       // movieSearchText  = (EditText) findViewById(R.id.movieTextField);
        favorite = (ImageView) findViewById(R.id.saved);
        homePage = (ImageView)findViewById(R.id.homePage);
        help = (ImageView) findViewById(R.id.help);

        homePage.setOnClickListener(goBackClicked -> {
            finish();
        });

        help.setOnClickListener(goBackClicked -> {
            AlertDialog dialog = new AlertDialog.Builder(MovieInfo.this)
                    .setTitle(R.string.helpTitle_movie)
                    .setMessage(R.string.helpMsg_movie)
                    .setPositiveButton("OK", (click, arg) ->{
                    })
                    .show();
        });

        favorite.setOnClickListener(favoriteClicked -> {
            favoriteFragment = new FavoriteFragment_WPG();
            FragmentManager fMnger = getSupportFragmentManager();
            FragmentTransaction tx = fMnger.beginTransaction();
            tx.replace(R.id.results_Movie, favoriteFragment);
            tx.commit();
        });

       prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       String titleSearch = prefs.getString("MovieTitle", "");
       movieSearchText = (EditText)findViewById(R.id.searchTitle);
       movieSearchText.setText(titleSearch);

       searchBtn = (Button)findViewById(R.id.searchButton);
//       movieList = findViewById(R.id.movieList);
//       LinearLayoutManager llm = new LinearLayoutManager(this);
//       llm.setStackFromEnd(true);
//       llm.setReverseLayout(true);
//
//       movieList.setLayoutManager(llm);
//       movieList.setAdapter(mvAdapter);

       searchBtn.setOnClickListener(clk -> {
           String movieTitle = movieSearchText.getText().toString();
           AlertDialog alertDialog = new AlertDialog.Builder(MovieInfo.this)
                   .setTitle("Searching result")
                   .setMessage("Finding your movie: "+movieTitle)
                   .setView(new ProgressBar(MovieInfo.this))
                   .show();

           SharedPreferences.Editor editor = prefs.edit();
           editor.putString("MovieTitle", movieTitle);
           editor.apply();


           Executor newThread = Executors.newSingleThreadExecutor();
           newThread.execute( () -> {
               try{
                   MovieData searchResult;
                   Bitmap image = null;
                   int detailType = 1;
                   stringURL = "http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t="
                           + URLEncoder.encode(movieTitle, "UTF-8");

                   URL url = new URL(stringURL);
                   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                   InputStream response = new BufferedInputStream(urlConnection.getInputStream());

                   XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                   factory.setNamespaceAware(false);
                   XmlPullParser xpp = factory.newPullParser();
                   xpp.setInput( response  , "UTF-8");

                   String title = null;
                   String year = null;
                   String runtime = null;
                   String actors = null;
                   String plot = null;
                   String poster = null;
                   String rating_imd = null;
                   String rating_meta = null;

                   while(xpp.next() != XmlPullParser.END_DOCUMENT){
                       switch(xpp.getEventType())
                       {
                           case XmlPullParser.START_TAG:
                               if(xpp.getName().equals("movie")){
                                   title = xpp.getAttributeValue(null, "title");
                                   year = xpp.getAttributeValue(null, "year");
                                   runtime = xpp.getAttributeValue(null, "runtime");
                                   actors = xpp.getAttributeValue(null, "actors");
                                   plot = xpp.getAttributeValue(null, "plot");
                                   poster = xpp.getAttributeValue(null, "poster");
                                   rating_meta = xpp.getAttributeValue(null, "metascore");
                                   rating_imd = xpp.getAttributeValue(null, "imdbRating");
                                   //*****************************change order************************
                                   searchResult = new MovieData(title, year, rating_imd, runtime, actors, plot, poster);

                                   File file = new File(getFilesDir(), title+".png");
                                   if(file.exists()){
                                       image = BitmapFactory.decodeFile(getFilesDir() + "/" + title + ".png");
                                   }
                                   else{
                                       URL imgUrl = new URL( poster);
                                       HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                                       connection.connect();
                                       int responseCode = connection.getResponseCode();
                                       if (responseCode == 200) {
                                           image = BitmapFactory.decodeStream(connection.getInputStream());
                                           image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(title+".png", Activity.MODE_PRIVATE));
                                       }

                                       FileOutputStream fOut = null;
                                       try {
                                           fOut = openFileOutput( title + ".png", Context.MODE_PRIVATE);
                                           image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                           fOut.flush();
                                           fOut.close();
                                       } catch (FileNotFoundException e) {
                                           e.printStackTrace();
                                       }
                                   }
                                   MovieData finalSearchResult = searchResult;
                                   Bitmap finalImage = image;
                                   runOnUiThread(()->{
                                       //*******************************************change arguments**************
                                       movieFragment = new MovieFragment_WPG(finalSearchResult, finalImage, detailType);
                                 //      FragmentManager fMgr = getSupportFragmentManager();
                                  //     FragmentTransaction tx = fMnger.beginTransaction();
                                       tx.replace(R.id.results_Movie, favoriteFragment);
                                       tx.commit();

                                       alertDialog.hide();
                                   });


                               }else if(xpp.getName().equals("error")){
                                   runOnUiThread(()->{
                                       Toast.makeText(getApplicationContext(), R.string.noresult_movie, Toast.LENGTH_LONG).show();
                                       alertDialog.hide();
                                   });

                               }
                               break;
                           case XmlPullParser.END_TAG:
                               break;
                           case XmlPullParser.TEXT:
                               break;
                       }
                   }
               }
               catch(IOException | XmlPullParserException ioe){
                   Log.e("Connection error: ", ioe.getMessage());
               }
           } );
       });
    }
    public void userClickedMessage(MovieData searchResult, Bitmap image, int detailType) {
        movieFragment = new MovieFragment_WPG(searchResult,image,detailType);
        fMnger = getSupportFragmentManager();
        tx = fMnger.beginTransaction();
        tx.replace(R.id.results_Movie, favoriteFragment);
        tx.commit();
    }
}

//   private class RowViews extends RecyclerView.ViewHolder {
//       TextView mvInfo;
//   //  TextView timeInfo;
//       int position = -1;
//
//       RowViews(View itemView) {
//           super(itemView);
//       // timeInfo = itemView.findViewById(R.id.timeInfo);
//           mvInfo = itemView.findViewById(R.id.movieInfo);
//           itemView.setOnClickListener(clk -> {
//               AlertDialog.Builder builder = new AlertDialog.Builder(MovieInfo.this);
//               builder.setMessage("Do you want to delete this movie: " + mvInfo.getText())
//                           .setPositiveButton("Yes", (dialog, cl) -> {
//                           position = getAdapterPosition();
//                           MovieMessage removeMsg = movieArray.get(position);
//                               movieArray.remove(position);
//                           mvAdapter.notifyItemRemoved(position);
//                           Snackbar.make(mvInfo, "You deleted movie #" + position, Snackbar.LENGTH_LONG)
//                                   .setAction("Undo", click -> {
//                                       movieArray.add(position, removeMsg);
//                                       mvAdapter.notifyItemInserted(position);
//                                   })
//                                   .show();
//                 })
//                       .setNegativeButton("No", (dialog, cl) -> {
//               })
//                       .create().show();
//
//           });
//       }
//
//       public void setPosition(int position) {
//           this.position = position;
//       }
//
//   }
//
//       private class MovieAdapter extends RecyclerView.Adapter {
//
//           @Override
//           public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//               View vw = getLayoutInflater().inflate(R.layout.search_view_movie, parent, false);
//               return new RowViews(vw);
//           }
//
//           @Override
//           public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//               RowViews movieLayout = (RowViews) holder;
//               movieLayout.mvInfo.setText(movieArray.get(position).getMessage());
//       //       movieLayout.timeInfo.setText(sdf.format(movieMsgs.get(position).getTime()));
//               movieLayout.setPosition(position);
//           }
//
//           @Override
//           public int getItemCount() {
//               return movieArray.size();
//           }
//
//       }
//
//       private class MovieMessage {
//           String msg;
//           int order;
//           Date searchTime;
//
//           public MovieMessage(String msg, int order, Date searchTime) {
//               this.msg = msg;
//               this.order = order;
//               this.searchTime = searchTime;
//           }
//
//           public String getMessage() {
//               return msg;
//           }
//
//           public int getOrder() {
//               return order;
//           }
//
//           public Date getTime() {
//               return searchTime;
//           }
//
//       }
//   }



