package com.example.test3.models;

public class Stop {
    private String stopName;
    private double latitude;
    private double longitude;

    public Stop() {
        // Default constructor required for Firebase
    }

    public Stop(String stopName, double latitude, double longitude) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
