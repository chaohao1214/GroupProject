package com.example.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {
    private Context mContext;
    private ArrayList<StationObject> mStationList;
    private Boolean mFullInfo;

    public StationAdapter(Context context, ArrayList<StationObject> stationList){
        mContext = context;
        mStationList = stationList;
        //mFullInfo = fullInfo;
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
//        double latitude = currentItem.getLatitude();
//        double longitude = currentItem.getLongitude();
//        String contactNo = currentItem.getContactNo();

        holder.adtTitle.setText(title);
//        holder.adtLatitude.setText("Latitude" + latitude);
//        holder.adtLongitude.setText("Longitude" + longitude);
//        holder.adtContactNo.setText("ContactNo" + contactNo);
    }

    @Override
    public int getItemCount() {
        return mStationList.size();
    }

    public StationObject getItem(int i) {
        return mStationList.get(i);
    }

    public class StationViewHolder extends RecyclerView.ViewHolder{

        public TextView adtTitle;
//        public TextView adtLatitude;
//        public TextView adtLongitude;
//        public TextView adtContactNo;


        public StationViewHolder(View itemView) {
            super(itemView);
            adtTitle = itemView.findViewById(R.id.stationTitle);
//            adtLatitude = itemView.findViewById(R.id.stationLatitude);
//            adtLongitude = itemView.findViewById(R.id.stationLongitude);
//            adtContactNo = itemView.findViewById(R.id.stationContactNo);

        }
    }
}
