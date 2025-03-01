package com.example.test3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test3.R;
import com.example.test3.models.Stop;
import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {
    private List<Stop> stopList;
    private Context context;
    private int currentStopIndex = -1;

    public StopAdapter(Context context, List<Stop> stopList) {
        this.context = context;
        this.stopList = stopList;
    }

    @NonNull
    @Override
    public StopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stop, parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopViewHolder holder, int position) {
        Stop stop = stopList.get(position);
        holder.stopName.setText(stop.getStopName());

        // Highlight the current stop
        if (position == currentStopIndex) {
            holder.stopName.setTextColor(context.getResources().getColor(R.color.teal_700));
        } else {
            holder.stopName.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    public void updateCurrentStop(int index) {
        currentStopIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stopList.size();
    }

    public static class StopViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;

        public StopViewHolder(@NonNull View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stop_name);
        }
    }
}
