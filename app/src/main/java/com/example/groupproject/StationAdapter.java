package com.example.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class StationAdapter extends RecyclerView.Adapter <StationAdapter.StationViewHolder> {
    private Context mContext;
    private ArrayList<StationObject> mStationList;



    public StationAdapter(Context context, ArrayList<StationObject> stationList, boolean b){
        mContext = context;
        mStationList = stationList;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_view_car, parent, false);
        return new StationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StationAdapter.StationViewHolder holder, int position) {
        StationObject currentItem = mStationList.get(position);

        String title = currentItem.getTitle();
  /*      double latitude = currentItem.getLatitude();
        double longitude = currentItem.getLongitude();
        String contactNo = currentItem.getContactNo();
*/
        holder.adtTitle.setText("Location: " + title);
   /*     holder.adtLatitude.setText("Latitude" + latitude);
        holder.adtLongitude.setText("Longitude" + longitude);
        holder.adtContactNo.setText("ContactNo" + contactNo);*/
    }

    @Override
    public int getItemCount() {
        return mStationList.size();
    }

    public class StationViewHolder extends RecyclerView.ViewHolder{

        public TextView adtTitle;
  /*      public TextView adtLatitude;
        public TextView adtLongitude;
        public TextView adtContactNo;
*/

        public StationViewHolder(View itemView) {
            super(itemView);
            adtTitle = itemView.findViewById(R.id.stationTitle);
   /*         adtLatitude = itemView.findViewById(R.id.stationLatitude);
            adtLongitude = itemView.findViewById(R.id.stationLongitude);
            adtContactNo = itemView.findViewById(R.id.stationContactNo);
*/


            int pos = getAbsoluteAdapterPosition();
            //find the charging station that was selected,
           // StationObject selectedStation = mStationList.get(pos) ;

           // itemView.setOnClickListener( (click -> {

            //fragment transaction:
            // fragment object
          //  FragmentManager fMgr = getSupportFragmentManager(); // one object
            //FragmentTransaction tx = fMgr.beginTransaction();
            // either add-remove or replace
            //tx.add(R.id.detailsFragment,selectedStation);
            // finally commit
            //tx.commit(); // start loading fragment

            //}));

        }
    }
}
