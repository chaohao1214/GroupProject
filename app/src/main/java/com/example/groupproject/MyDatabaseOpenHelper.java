package com.example.groupproject;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class helps to create, update a database
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    /**
     * database name
     */
    public static final String DATABASE_NAME = "MyDatabaseFile";
    /**
     * version of a database
     */
    public static final int VERSION_NUM = 1;
    /**
     * table name
     */
    public static final String TABLE_NAME = "Stations";
    /**
     * column name in a table
     */
    public static final String COL_ID = "_id";
    /**
     * column name in a table
     */
    public static final String COL_TITLE = "TITLE";
    /**
     * column name in a table
     */
    public static final String COL_LATITUDE = "LATITUDE";
    /**
     * column name in a table
     */
    public static final String COL_LONGITUDE = "LONGITUDE";
    /**
     * column name in a table
     */
    public static final String COL_PHONE = "PHONE";
    /**
     * Constructor to create a database open helper
     * @param ctx activity
     */
    public MyDatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }
    /**
     * Methods creates a table
     * @param db database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_LATITUDE + " REAL, "
                + COL_LONGITUDE + " REAL, " + COL_PHONE + " TEXT)");
    }
    /**
     * Methods upgrades a database
     * @param db database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    /**
     * Method downgrades a database
     * @param db database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}


