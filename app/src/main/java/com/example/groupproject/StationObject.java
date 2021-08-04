package com.example.groupproject;

public class StationObject {

        private String mTitle;
        private double mLatitude;
        private double mLongitude;
        private String mContactNo;
        private long mId;

    public StationObject(long id, String title, double latitude, double longitude, String contactNo) {
            mTitle = title;
            mLatitude = latitude;
            mLongitude = longitude;
            mContactNo = contactNo;
        }

    public StationObject(String title) {
        mTitle = title;
    }

    public String getTitle(){
            return mTitle;
        }
        public double getLatitude(){
            return mLatitude;
        }

        public double getLongitude(){
            return mLongitude;
        }

        public String getContactNo() {
             return mContactNo;
        }

        public long getId() {
        return mId;
    }


}
