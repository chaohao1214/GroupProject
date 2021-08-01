package com.example.groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;


public class MovieFragment_WPG extends Fragment {

    MovieData searchResult;
    Bitmap image;
    int type;

    MyOpenHelper_movie opener;
    SQLiteDatabase db;

    ImageView saveBtn;
    ImageView returnBtn;

    public MovieFragment_WPG(MovieData searchResult, Bitmap image, int type){
        this.searchResult = searchResult;
        this.image = image;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View movieView = inflater.inflate(R.layout.activity_frag_movie, container, false);

        ImageView poster = movieView.findViewById(R.id.poster);
        TextView title = movieView.findViewById(R.id.movieTitle);
        TextView year = movieView.findViewById(R.id.year);
        TextView rating = movieView.findViewById(R.id.rating);
        TextView runtime = movieView.findViewById(R.id.runtime);
        TextView actors = movieView.findViewById(R.id.actors);
        TextView plot = movieView.findViewById(R.id.plot);

        poster.setImageBitmap(image);
        title.setText("Movie Title: " + searchResult.getTitle());
        year.setText("Year: " + searchResult.getYear());
        rating.setText("Rating: " + searchResult.getRating());
        runtime.setText("Run time: " + searchResult.getRuntime());
        actors.setText("Main actors: " + searchResult.getActor());
        plot.setText("Plot: " + searchResult.getPlot());

        returnBtn = movieView.findViewById(R.id.homePage);

        if(type == 2){
            returnBtn.setOnClickListener(clicked -> {
                getParentFragmentManager().beginTransaction().remove(this).commit();
                MovieInfo parentActivity = (MovieInfo) getContext();
                FavoriteFragment_WPG favoriteFragment = new FavoriteFragment_WPG();
                FragmentManager fMnger = parentActivity.getSupportFragmentManager();
                FragmentTransaction tx = fMnger.beginTransaction();
                tx.replace(R.id.result_Movie, favoriteFragment);
                tx.commit();

            });
        }
        returnBtn.setOnClickListener(clicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        saveBtn = movieView.findViewById(R.id.add);
        if(type == 2){
            saveBtn.setVisibility(View.INVISIBLE);
        }
        saveBtn.setOnClickListener(addClicked -> {
            saveMovies(searchResult);
        });

        return movieView;
    }

    public void saveMovies(MovieData searchResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("Would you like to save this to your saved list: "+ searchResult.getTitle())
                .setTitle(R.string.addto_msg_movie)
                .setNegativeButton(R.string.cancel_movie, (dialog, cl) ->{ })
                .setPositiveButton(R.string.add_movie, (dialog, cl) ->{
                    opener = new MyOpenHelper_movie(getContext());
                    db = opener.getWritableDatabase();

                    Cursor result=db.query(MyOpenHelper_movie.TABLE_NAME,new String[]{MyOpenHelper_movie.TITLE_COL},MyOpenHelper_movie.TITLE_COL+"=?",new String[]{searchResult.getTitle()},null,null,null);
                    if(result.moveToNext()){
                        Toast.makeText(getContext(), R.string.alreadyAdded_movie, Toast.LENGTH_LONG).show();
                    }else {
                        ContentValues newMovieRow = new ContentValues();
                        newMovieRow.put(MyOpenHelper_movie.TITLE_COL, searchResult.getTitle());
                        newMovieRow.put(MyOpenHelper_movie.YEAR_COL, searchResult.getYear());
                        newMovieRow.put(MyOpenHelper_movie.RATING_COL, searchResult.getRating());
                        newMovieRow.put(MyOpenHelper_movie.RUNTIME_COL, searchResult.getRuntime());
                        newMovieRow.put(MyOpenHelper_movie.ACTOR_COL, searchResult.getActor());
                        newMovieRow.put(MyOpenHelper_movie.PLOT_COL, searchResult.getPlot());
                        newMovieRow.put(MyOpenHelper_movie.IMAGE_COL, searchResult.getImageURL());

                        long newIdMovie = db.insert(MyOpenHelper_movie.TABLE_NAME, MyOpenHelper_movie.TITLE_COL, newMovieRow);

                        Snackbar.make(saveBtn, "You saved movie " + searchResult.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo_movie, clk -> {
                                    db.delete(MyOpenHelper_movie.TABLE_NAME, "_id=?", new String[]{Long.toString(searchResult.getId())});
                                })
                                .show();
                    }})
                .create().show();
    }
}

