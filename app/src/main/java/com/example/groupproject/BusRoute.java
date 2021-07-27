package com.example.groupproject;

public class BusRoute {
    private String busNumber;
    private String destination;
    private String direction;
    private String directionID;
    private String latitude;
    private String longitude;
    private String gpsSpeed;
    private String startTime;
    private String adjustedTime;
    private long id;

    public BusRoute(){

    }

    public BusRoute(String busNumber, String destination, String direction, String directionID, long id){
        this.busNumber = busNumber;
        this.destination = destination;
        this.direction = direction;
        this.directionID = directionID;
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirectionID() {
        return directionID;
    }

    public void setDirectionID(String directionID) {
        this.directionID = directionID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(String gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAdjustedTime() {
        return adjustedTime;
    }

    public void setAdjustedTime(String adjustedTime) {
        this.adjustedTime = adjustedTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
