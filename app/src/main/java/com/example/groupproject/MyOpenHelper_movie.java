package com.example.groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * This class inherits from SQLiteOpenHelper
 * it allows to create and update database
 * @author Weiping Guo
 * @version 1.0
 */
public class MyOpenHelper_movie extends SQLiteOpenHelper {
    /** This holds the name of database */
    public static final String MOVIE = "TheMovieDatabase";
    /** This holds the version of creating database */
    public static final int VERSION = 1;
    /** This holds the name of the table */
    public static final String TABLE_NAME = "Movies";
    /** This holds the column name of the table */
    public static final String ID_COL = "_id";
    /** This holds the column name of the movie title */
    public static final String TITLE_COL = "MovieTitle";
    /** This holds the column name of the year of the movie */
    public static final String YEAR_COL = "Year";
    /** This holds the column name of the rating of the movie */
    public static final String RATING_COL = "Rating";
    /** This holds the column name of the runtime of the movie */
    public static final String RUNTIME_COL = "Runtime";
    /** This holds the column name of the actors of the movie */
    public static final String ACTOR_COL = "Actors";
    /** This holds the column name of the plot of the movie */
    public static final String PLOT_COL = "Plot";
    /** This holds the column name of image of the movie */
    public static final String IMAGE_COL = "ImageURL";

    /**
     * Constructor with context as parameter
     * @param context Context object to get info from MovieFragment activities
     */
    public MyOpenHelper_movie(Context context) {
        super(context, MOVIE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT, "
                + YEAR_COL + " INTEGER, "
                + RATING_COL + " TEXT, "
                + RUNTIME_COL + " TEXT, "
                + ACTOR_COL + " TEXT, "
                + PLOT_COL + " TEXT, "
                + IMAGE_COL + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME + ";") ;
        onCreate(db);
    }

}