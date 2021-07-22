package com.example.groupproject;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpenHelper  extends SQLiteOpenHelper {
    public static final String name ="StationDatabase";
    public static final int version =3;
    public static final String TABLE_NAME = "Messages";
    public static final String col_message = "Message";
    public static final String search_button_info = "SearchButton";
    public static final String col_time_sent = "TimeSent";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    // DDL  create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_message + " TEXT, " + search_button_info + " INTEGER, "+ col_time_sent + " Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
