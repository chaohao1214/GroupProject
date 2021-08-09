package com.example.groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;



import java.util.Locale;

/**
 * Class shows car charging station's details, loads station on a google map
 */
public class StationFragment extends Fragment {
    private boolean isTablet;
    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }
    /**
     * Method loads station's details on the screen, loads station on a google map, adds station to the list of favourite stations
     * @param savedInstanceState reference to a Bundle object that is passed into the onCreate method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        ChargingStationObject station = (ChargingStationObject) bundle.getSerializable("Station");
        View result = inflater.inflate(R.layout.activity_car_charging_station_item_details, container, false);

        TextView title = (TextView) result.findViewById(R.id.title);
        String stationTitle = station.getTitle();
        title.setText(stationTitle);

        TextView latitude = (TextView) result.findViewById(R.id.latitudeResult);
        double latitudeValue = station.getLatitude();
        latitude.setText(Double.toString(latitudeValue));

        TextView longitude = (TextView) result.findViewById(R.id.longitudeResult);
        double longitudeValue = station.getLongitude();
        longitude.setText(Double.toString(longitudeValue));

        TextView phone = (TextView) result.findViewById(R.id.phoneResult);
        String phoneNumber = station.getPhone();
        if(phoneNumber.equals("null")){
            phone.setText(R.string.phoneNull);
        }
        else {
            phone.setText(phoneNumber);
        }
        Button loadMapBtn = (Button) result.findViewById(R.id.loadMap);
        loadMapBtn.setOnClickListener(clk -> {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", latitudeValue, longitudeValue);
            Uri intentUri = Uri.parse(uri);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this.getActivity());
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        Button addBtn = (Button) result.findViewById(R.id.addButton);
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
        Button seeListBtn = (Button)result.findViewById(R.id.listOfFavouritesButton);
        seeListBtn.setOnClickListener(clk -> {
            Intent nextPage = new Intent(this.getContext(), CarChargingFavouriteStations.class);
            startActivity(nextPage);
        });

        return result;
    }
}
