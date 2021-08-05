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

/**
 * This class displays all the major function buttons on movie app homepage.
 * Users can search a movie by title, check for help and menu option.
 * @author Weiping Guo
 * @version 1.0
 */

public class MovieInfo extends AppCompatActivity {
/** This holds search button*/
    Button searchBtn;
    /** This holds search editText*/
    EditText movieSearchText;
    /** This hold the image of saved list of movies*/
    ImageView saved;
    /** This holds the image of homepage*/
    ImageView homePage;
    /** This holds the image of help menu*/
    ImageView help;
    /** This holds the SharedPreferences object*/
    private SharedPreferences prefs;
    /** This string represents the address of the server to be connected */
    private String stringURL;
    /** This holds the fragment to display the details of a searched movie */
    MovieFragment_WPG movieFragment;
    /** This holds the fragment to display the list of the saved movies */
    FavoriteFragment_WPG favoriteFragment;
    FragmentTransaction tx;
    FragmentManager fMnger;

    /**
     * This method is called to select an item.
     * @param item The item selected from the menu.
     * @return boolean Return true to access the selected app,
     *         false to proceed to next step.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch(item.getItemId()){
            case R.id.busApp_movie:
                nextPage = new Intent(this,SearchBus.class);
                startActivity(nextPage);
                break;
            case R.id.chargerApp_movie:
                nextPage = new Intent(this, SearchStation.class);
                startActivity(nextPage);
                break;
            case R.id.movieApp_movie:
                nextPage = new Intent( this, MovieInfo.class);
                startActivity(nextPage );
                break;
            case R.id.soccerApp_movie:
                nextPage = new Intent(this, SoccerActivitySecond.class);
                startActivity(nextPage);
                break;
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

        saved =  findViewById(R.id.saved);
        homePage = findViewById(R.id.homePage);
        help =  findViewById(R.id.help);

        homePage.setOnClickListener(clicked -> {
            finish();
        });

        help.setOnClickListener(clicked -> {
            AlertDialog dialog = new AlertDialog.Builder(MovieInfo.this)
                    .setTitle(R.string.helpTitle_movie)
                    .setMessage(R.string.helpMsg_movie)
                    .setPositiveButton("OK", (click, arg) ->{
                    })
                    .show();
        });

        saved.setOnClickListener(clicked -> {
            favoriteFragment = new FavoriteFragment_WPG();
            FragmentManager fMnger = getSupportFragmentManager();
            FragmentTransaction tx = fMnger.beginTransaction();
            tx.replace(R.id.result_Movie, favoriteFragment);
            tx.commit();
        });

       prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       String titleSearch = prefs.getString("MovieTitle", "");

       movieSearchText = findViewById(R.id.searchTitle);
       movieSearchText.setText(titleSearch);

       searchBtn = findViewById(R.id.searchButton);

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

                   String title;
                   String year;
                   String runtime;
                   String actors;
                   String plot ;
                   String poster;
                   String rating_imd;


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
                                   rating_imd = xpp.getAttributeValue(null, "imdbRating");

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

                                       movieFragment = new MovieFragment_WPG(finalSearchResult, finalImage, detailType);
                                       fMnger = getSupportFragmentManager();
                                       tx = fMnger.beginTransaction();
                                       tx.replace(R.id.result_Movie,movieFragment);
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
               catch(IOException | XmlPullParserException ex){
                   Log.e("Connection error: ", ex.getMessage());
               }
           } );
       });
    }

    /**
     * This method displays the searched movie's details.
     *
     * @param searchResult searched movie's info
     * @param image searched movie's image
     * @param type type 1 for searched movie info, 2 for searched movie info from saved list
     */
    public void clickedMessage(MovieData searchResult, Bitmap image, int type) {
        movieFragment = new MovieFragment_WPG(searchResult,image,type);
        fMnger = getSupportFragmentManager();
        tx = fMnger.beginTransaction();
        tx.replace(R.id.result_Movie, movieFragment);
        tx.commit();
    }


}



