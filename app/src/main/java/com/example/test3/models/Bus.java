package com.example.test3.models;

public class Bus {
    private String busNumber;
    private int fare;
    private int totalTime;

    // Default constructor for Firebase
    public Bus() {
    }

    public Bus(String busNumber, int fare, int totalTime) {
        this.busNumber = busNumber;
        this.fare = fare;
        this.totalTime = totalTime;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public int getFare() {
        return fare;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
