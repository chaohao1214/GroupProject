package com.example.groupproject;

/**
 * A bus route includes detailed information about the route.
 * @author Chaohao
 * @version 1.0
 */
public class BusRoute {
    /**
     * This instance variables holds all the bus detail information
     */
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

    /**
     *  default constructor
     */
    public BusRoute(){

    }

    /**
     *
     * @param busNumber  the route number
     * @param destination  the route destination
     * @param direction   the route direction
     * @param directionID  the route direction id
     * @param id   the route id
     */
    public BusRoute(String busNumber, String destination, String direction, String directionID, long id){
        this.busNumber = busNumber;
        this.destination = destination;
        this.direction = direction;
        this.directionID = directionID;
        this.id = id;
    }

    /**
     * get the bus number
     * @return route number
     */
    public String getBusNumber() {
        return busNumber;
    }

    /**
     * setter for bus number
     * @param busNumber  the bus number
     */
    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    /**
     * getter of the bus destination
     * @return destination of the route
     */
    public String getDestination() {
        return destination;
    }

    /**
     * mutator of the bus destination
     * @param destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * getter of bus direction
     * @return bus direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * getter of the bus direction
     * @param direction direction of the route
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * getter of the direction id
     * @return direction id
     */
    public String getDirectionID() {
        return directionID;
    }

    /**
     * setter of the direction id
     * @param directionID  direction id
     */
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
