package com.example.test3;


public class RouteModel {
    private String routeName;
    private String busName;
    private String fare;
    private String totalTime;
    private String stops;

    public RouteModel(String routeName, String busName, String fare, String totalTime, String stops) {
        this.routeName = routeName;
        this.busName = busName;
        this.fare = fare;
        this.totalTime = totalTime;
        this.stops = stops;
    }

    public String getRouteName() { return routeName; }
    public String getBusName() { return busName; }
    public String getFare() { return fare; }
    public String getTotalTime() { return totalTime; }
    public String getStops() { return stops; }
}

