package com.example.groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper_movie extends SQLiteOpenHelper {
    public static final String MOVIE = "TheMovieDatabase";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "Movies";
    public static final String ID_COL = "_id";
    public static final String TITLE_COL = "MovieTitle";
    public static final String YEAR_COL = "Year";
    public static final String RATING_COL = "Rating";
    public static final String RUNTIME_COL = "Runtime";
    public static final String ACTOR_COL = "Actors";
    public static final String PLOT_COL = "Plot";
    public static final String IMAGE_COL = "ImageURL";

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