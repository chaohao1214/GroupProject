package com.example.groupproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


class CarChargingFavouriteStations extends AppCompatActivity {

    ArrayList<StationObject> favStations = new ArrayList<>();
    private int positionClicked;
    StationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_charging_station_favourites);
        ListView listOfFavourites = (ListView)findViewById(R.id.listOfFavourites);

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_TITLE, MyDatabaseOpenHelper.COL_LATITUDE,
                MyDatabaseOpenHelper.COL_LONGITUDE, MyDatabaseOpenHelper.COL_PHONE};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_TITLE);
        int latitudeColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_LATITUDE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int longitudeColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_LONGITUDE);
        int phoneColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_PHONE);
        while(results.moveToNext()) {
            String title = results.getString(titleColumnIndex);
            double latitude = results.getDouble(latitudeColIndex);
            double longitude = results.getDouble(longitudeColIndex);
            long id = results.getLong(idColIndex);
            String phone = results.getString(phoneColIndex);
            favStations.add(new StationObject(id, title, latitude, longitude, phone));
            adapter = new StationAdapter(getApplicationContext(), favStations, true);
            listOfFavourites.setAdapter((ListAdapter) adapter);
        }
        listOfFavourites.setOnItemClickListener(( parent,  view,  position,  id) -> {
            positionClicked = position;
            Toast.makeText(getApplicationContext(),"You picked a station to delete",Toast.LENGTH_SHORT).show();
        });
        Button delete = (Button)findViewById(R.id.deleteButton);
        delete.setOnClickListener(clk -> {
            StationObject stationToDelete = favStations.get(positionClicked);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            favStations.remove(positionClicked);
                            adapter.notifyDataSetChanged();
                            int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME,
                                    MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(stationToDelete.getId())});
                            Log.i("StationView", "Deleted " + numDeleted + " rows");
                            Snackbar.make((View)findViewById(R.id.snackbar), "Station was successfully deleted", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (d,w) -> {  /* nothing */})
                    .create();
            dialog.show();
        });
    }
}
