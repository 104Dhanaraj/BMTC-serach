package com.example.test3.models;

import java.util.List;

public class Route {
    private String routeName;
    private List<String> stops;
    private List<Bus> buses;

    // ✅ Existing constructor
    public Route() {
        // Default constructor for Firebase
    }

    // ✅ Add this constructor
    public Route(String routeName, List<String> stops, List<Bus> buses) {
        this.routeName = routeName;
        this.stops = stops;
        this.buses = buses;
    }

    public String getRouteName() {
        return routeName;
    }

    public List<String> getStops() {
        return stops;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }
}
