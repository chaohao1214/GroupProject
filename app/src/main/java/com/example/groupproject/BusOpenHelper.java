package com.example.groupproject;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BusOpenHelper extends SQLiteOpenHelper {
    public static final String name ="BusDatabase";
    public static final int version =5;
    public static final String Bus_TABLE_NAME = "Messages";
    public static final String col_routes = "RouteNumber";
    public static final String col_heading = "RouteHeading";
    public static final String col_direction = "Direction";
    public static final String col_directionID = "DirectionID";


    public BusOpenHelper(Context context) {
        super(context, name, null, version);
    }

    // DDL  create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Bus_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_routes + " TEXT, " + col_heading + " Text, "+ col_direction + " Text," +
                col_directionID + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + Bus_TABLE_NAME);
            onCreate(db);
    }
}
