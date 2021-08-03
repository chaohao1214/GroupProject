package com.example.groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class StationDetailsFragment extends Fragment {


    int stationPosition;

    TextView title;
    TextView latitude;
    TextView longitude;
    TextView contactNo;

    String mTitle;
    double mLatitude;
    double mLongitude;
    String mContactNo;
    long id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View stationDetailsView = inflater.inflate(R.layout.station_item_details, container, false);


        StationObject station = new StationObject(id, mTitle,mLatitude, mLongitude, mContactNo);

        title = (TextView) stationDetailsView.findViewById(R.id.title);
        String stationTitle = station.getTitle();
        title.setText(stationTitle);

        latitude = (TextView) stationDetailsView.findViewById(R.id.latitudeResult);
        double latitudeValue = station.getLatitude();
        latitude.setText(Double.toString(latitudeValue));

        longitude = (TextView) stationDetailsView.findViewById(R.id.longitudeResult);
        double longitudeValue = station.getLongitude();
        longitude.setText(Double.toString(longitudeValue));

        contactNo = (TextView) stationDetailsView.findViewById(R.id.phoneResult);
        String phoneNumber = station.getContactNo();
        contactNo.setText(phoneNumber);


        // Google map intent
        Button loadMapBtn = (Button) stationDetailsView.findViewById(R.id.loadMap);
        loadMapBtn.setOnClickListener(clk -> {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", latitudeValue, longitudeValue);
            Uri intentUri = Uri.parse(uri);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this.getActivity());
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        Button addBtn = (Button) stationDetailsView.findViewById(R.id.addButton);
        addBtn.setOnClickListener(click -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpenHelper.COL_TITLE, stationTitle);
            newRowValues.put(MyDatabaseOpenHelper.COL_LATITUDE, latitudeValue);
            newRowValues.put(MyDatabaseOpenHelper.COL_LONGITUDE, longitudeValue);
            newRowValues.put(MyDatabaseOpenHelper.COL_PHONE, phoneNumber);
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            AlertDialog dialog = builder.setMessage("Station was added to the list of favourite stations")
                    .setPositiveButton("OK", (d,w) -> {  /* nothing */})
                    .create();
            dialog.show();
        });

        return stationDetailsView;
    }
}



