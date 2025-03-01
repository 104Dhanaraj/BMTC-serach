package com.example.test3;


public class StopModel {
    private String stopName;
    private double latitude;
    private double longitude;
    private boolean isCurrentStop;

    public StopModel(String stopName, boolean isCurrentStop) {
        this.stopName = stopName;
        this.isCurrentStop = isCurrentStop;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isCurrentStop() {
        return isCurrentStop;
    }

    public void setCurrentStop(boolean currentStop) {
        isCurrentStop = currentStop;
    }
}

