package com.example.test3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.adapters.StopAdapter;
import com.example.test3.models.Stop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StopDisplayActivity extends AppCompatActivity {
    private RecyclerView stopListView;
    private StopAdapter stopAdapter;
    private List<Stop> stopList = new ArrayList<>();
    private TextToSpeech textToSpeech;
    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_display);

        // Initialize UI
        stopListView = findViewById(R.id.stopListView);
        stopListView.setLayoutManager(new LinearLayoutManager(this));

        // Get Stops from Intent
        stopList = (List<Stop>) getIntent().getSerializableExtra("stops");
        if (stopList == null) {
            stopList = new ArrayList<>(); // Prevent crash if stops are null
            Toast.makeText(this, "No stops available!", Toast.LENGTH_LONG).show();
            finish(); // Exit if no stops
            return;
        }

        // Initialize Adapter
        stopAdapter = new StopAdapter(this, stopList);
        stopListView.setAdapter(stopAdapter);

        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        // Initialize Location Provider
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    checkNearestStop(location);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void checkNearestStop(Location userLocation) {
        for (Stop stop : stopList) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                    stop.getLatitude(), stop.getLongitude(), results);
            if (results[0] < 100) { // If user is within 100m of stop
                announceStop(stop.getStopName());
                break;
            }
        }
    }

    private void announceStop(String stopName) {
        textToSpeech.speak("Next stop: " + stopName, TextToSpeech.QUEUE_FLUSH, null, null);
        Toast.makeText(this, "Next Stop: " + stopName, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (locationProviderClient != null && locationCallback != null) {
            locationProviderClient.removeLocationUpdates(locationCallback);
        }
        super.onDestroy();
    }
}
