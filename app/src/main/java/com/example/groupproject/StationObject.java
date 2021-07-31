package com.example.groupproject;

public class StationObject {

        private String mTitle;
        private double mLatitude;
        private double mLongitude;
        private String mContactNo;

    public StationObject(String Title, double latitude, double longitude, String contactNo) {
            mTitle = Title;
            mLatitude = latitude;
            mLongitude = longitude;
            mContactNo = contactNo;
        }

    public StationObject(String Title) {
        mTitle = Title;
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


}
