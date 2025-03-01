package com.example.test3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.adapters.BusAdapter;
import com.example.test3.models.Bus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noBusesText;
    private DatabaseReference databaseReference;
    private BusAdapter busAdapter;
    private List<Bus> busList = new ArrayList<>();

    private String source, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        // Get Source & Destination
        source = getIntent().getStringExtra("source");
        destination = getIntent().getStringExtra("destination");

        Log.d("BusListActivity", "Searching buses for: " + source + " -> " + destination);

        // Initialize UI Elements
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noBusesText = findViewById(R.id.noBusesText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance("https://bmtc-t3-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("routes");

        // Load Available Buses
        loadAvailableBuses();
    }

    private void loadAvailableBuses() {
        progressBar.setVisibility(View.VISIBLE);
        noBusesText.setVisibility(View.GONE);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                busList.clear();

                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                    List<String> stops = new ArrayList<>();
                    for (DataSnapshot stopSnapshot : routeSnapshot.child("stops").getChildren()) {
                        stops.add(stopSnapshot.getValue(String.class));
                    }

                    if (stops.contains(source) && stops.contains(destination)) {
                        int sourceIndex = stops.indexOf(source);
                        int destinationIndex = stops.indexOf(destination);

                        if (sourceIndex < destinationIndex) { // Bus should go from Source to Destination
                            for (DataSnapshot busSnapshot : routeSnapshot.child("buses").getChildren()) {
                                String busNumber = busSnapshot.child("busNumber").getValue(String.class);
                                int fullFare = busSnapshot.child("fare").getValue(Integer.class);
                                int fullTime = busSnapshot.child("totalTime").getValue(Integer.class);

                                // 🔹 Calculate Reduced Fare & Time
                                int totalStops = stops.size() - 1;
                                int traveledStops = destinationIndex - sourceIndex;

                                int reducedFare = (int) Math.round((fullFare * traveledStops) / (double) totalStops);
                                int reducedTime = (int) Math.round((fullTime * traveledStops) / (double) totalStops);

                                busList.add(new Bus(busNumber, reducedFare, reducedTime));
                                Log.d("BusListActivity", "Bus Found: " + busNumber + " - Fare: " + reducedFare + ", Time: " + reducedTime);
                            }
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
                if (busList.isEmpty()) {
                    noBusesText.setVisibility(View.VISIBLE);
                    Log.d("BusListActivity", "No Buses Found!");
                } else {
                    busAdapter = new BusAdapter(busList, BusListActivity.this, source, destination);
                    recyclerView.setAdapter(busAdapter);
                    Log.d("BusListActivity", "Total Buses Loaded: " + busList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BusListActivity.this, "Failed to load buses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
