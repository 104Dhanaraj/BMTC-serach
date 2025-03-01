package com.example.test3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.R;
import com.example.test3.models.Bus;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<Bus> busList;
    private Context context;
    private String source;
    private String destination;

    // ✅ Correct Constructor
    public BusAdapter(List<Bus> busList, Context context, String source, String destination) {
        this.busList = busList;
        this.context = context;
        this.source = source;
        this.destination = destination;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.busNumber.setText("Bus: " + bus.getBusNumber());
        holder.fare.setText("Fare: ₹" + bus.getFare());
        holder.totalTime.setText("Total Time: " + bus.getTotalTime() + " mins");
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView busNumber, fare, totalTime;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumber = itemView.findViewById(R.id.busNumber);
            fare = itemView.findViewById(R.id.fare);
            totalTime = itemView.findViewById(R.id.totalTime);
        }
    }
}
