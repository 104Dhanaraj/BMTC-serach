package com.example.test3;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.test3.models.Route;
import com.example.test3.models.Bus;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDatabase
                .getInstance("https://bmtc-t3-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("routes");
    }

    public void addRoute(Route route) {
        DatabaseReference routeRef = databaseReference.child(route.getRouteName());
        routeRef.child("routeName").setValue(route.getRouteName());
        routeRef.child("stops").setValue(route.getStops());

        for (Bus bus : route.getBuses()) {
            DatabaseReference busRef = routeRef.child("buses").child(bus.getBusNumber());
            busRef.child("busNumber").setValue(bus.getBusNumber());
            busRef.child("fare").setValue(bus.getFare());
            busRef.child("totalTime").setValue(bus.getTotalTime());

            Log.d("FirebaseHelper", "Bus Data Saved: " + bus.getBusNumber() + " - Fare: " + bus.getFare() + ", Total Time: " + bus.getTotalTime());
        }
    }

}
