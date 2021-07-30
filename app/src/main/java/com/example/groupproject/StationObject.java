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

        public void setTitle(String setTitle) {
            mTitle = setTitle;
        }

        public void setLatitude(double setLatitude) {
            mLatitude = setLatitude;
        }

        public void setLongitude(double setLongitude) {
            mLongitude = setLongitude;
        }

        public void setmContactNo(String setContactNo) {
            mContactNo = setContactNo;
    }




}
