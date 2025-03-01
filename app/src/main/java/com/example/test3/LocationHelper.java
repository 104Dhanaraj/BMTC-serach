package com.example.test3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {

    private final Context context;
    private final LocationUpdateListener locationUpdateListener;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
    }

    public LocationHelper(Context context, LocationUpdateListener listener) {
        this.context = context;
        this.locationUpdateListener = listener;
        initializeLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationUpdates() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (locationUpdateListener != null) {
                    locationUpdateListener.onLocationUpdated(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Request location updates (every 5 seconds or 10 meters)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
