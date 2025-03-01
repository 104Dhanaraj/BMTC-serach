package com.example.test3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private Spinner routeSpinner, busSpinner;
    private EditText fareEditText, timeEditText;
    private Button addRouteButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://bmtc-t3-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("routes");

        // Initialize UI elements
        routeSpinner = findViewById(R.id.routeSpinner);
        busSpinner = findViewById(R.id.busSpinner);
        fareEditText = findViewById(R.id.fareEditText);
        timeEditText = findViewById(R.id.timeEditText);
        addRouteButton = findViewById(R.id.addRouteButton);

        // Predefined Routes & Buses
        List<String> routes = Arrays.asList("Route A", "Route B", "Route C");
        List<String> buses = Arrays.asList("BMTC-101", "BMTC-102", "BMTC-103");

        // Set dropdown values
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, routes);
        ArrayAdapter<String> busAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, buses);
        routeSpinner.setAdapter(routeAdapter);
        busSpinner.setAdapter(busAdapter);

        // Handle button click
        addRouteButton.setOnClickListener(v -> addRouteToFirebase());
    }

    private void addRouteToFirebase() {
        String selectedRoute = routeSpinner.getSelectedItem().toString();
        String selectedBus = busSpinner.getSelectedItem().toString();
        String fareText = fareEditText.getText().toString().trim();
        String totalTimeText = timeEditText.getText().toString().trim();

        // Validate input fields
        if (fareText.isEmpty() || totalTimeText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int fare = Integer.parseInt(fareText);
            int totalTime = Integer.parseInt(totalTimeText);

            // Save route data
            DatabaseReference routeRef = databaseReference.child(selectedRoute);
            routeRef.child("routeName").setValue(selectedRoute);

            // Save stops
            List<String> stops = Arrays.asList("Stop A", "Stop B", "Stop C");
            routeRef.child("stops").setValue(stops);

            // Save Bus Data (Replace `-` with `_` to avoid Firebase path issues)
            String busId = selectedBus.replace("-", "_");
            DatabaseReference busRef = routeRef.child("buses").child(busId);
            busRef.child("busNumber").setValue(selectedBus);
            busRef.child("fare").setValue(fare);
            busRef.child("totalTime").setValue(totalTime);

            // Debug Log
            Log.d("AdminActivity", "Bus Data Saved: " + selectedBus + " - Fare: " + fare + ", Total Time: " + totalTime);

            Toast.makeText(this, "Route added successfully!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Log.e("AdminActivity", "Error parsing fare or totalTime", e);
            Toast.makeText(this, "Enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }
}
