package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserActivity extends AppCompatActivity {

    private Spinner sourceSpinner, destinationSpinner;
    private Button searchBusButton;
    private DatabaseReference databaseReference;
    private List<String> stopNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize UI elements
        sourceSpinner = findViewById(R.id.sourceSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        searchBusButton = findViewById(R.id.searchBusButton);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance("https://bmtc-t3-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("routes");

        // Load stops into dropdowns
        loadStopsFromFirebase();

        // Handle button click
        searchBusButton.setOnClickListener(v -> findAvailableBuses());
    }

    private void loadStopsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> uniqueStops = new HashSet<>(); // Use Set to avoid duplicates

                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                    DataSnapshot stopsSnapshot = routeSnapshot.child("stops");
                    for (DataSnapshot stop : stopsSnapshot.getChildren()) {
                        String stopName = stop.getValue(String.class);
                        if (stopName != null) {
                            uniqueStops.add(stopName);
                        }
                    }
                }

                stopNames.clear();
                stopNames.addAll(uniqueStops);

                // 🛠 Debug Log
                Log.d("UserActivity", "Stops Loaded: " + stopNames.toString());
                Toast.makeText(UserActivity.this, "Stops Loaded: " + stopNames.size(), Toast.LENGTH_SHORT).show();

                if (stopNames.isEmpty()) {
                    Toast.makeText(UserActivity.this, "No stops found in database!", Toast.LENGTH_LONG).show();
                }

                // Populate dropdowns
                adapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_spinner_item, stopNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                sourceSpinner.setAdapter(adapter);
                destinationSpinner.setAdapter(adapter);

                // 🔄 Force UI Refresh
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "Failed to load stops", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findAvailableBuses() {
        if (sourceSpinner.getSelectedItem() == null || destinationSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select valid source and destination!", Toast.LENGTH_SHORT).show();
            return;
        }

        String source = sourceSpinner.getSelectedItem().toString();
        String destination = destinationSpinner.getSelectedItem().toString();

        if (source.equals(destination)) {
            Toast.makeText(this, "Source and destination cannot be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(UserActivity.this, BusListActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }
}
