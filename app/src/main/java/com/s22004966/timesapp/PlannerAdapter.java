package com.s22004966.timesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> {

    private List<PlannerModel> plannerList;

    public PlannerAdapter(List<PlannerModel> plannerList) {
        this.plannerList = plannerList;
    }

    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_card_layout, parent, false);
        return new PlannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerViewHolder holder, int position) {
        PlannerModel model = plannerList.get(position);
        holder.title.setText(model.getSchedule());
        holder.dateTime.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.location.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return plannerList.size();
    }

    public static class PlannerViewHolder extends RecyclerView.ViewHolder {
        TextView title, dateTime, time, location;

        public PlannerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            dateTime = itemView.findViewById(R.id.taskDate);
            time = itemView.findViewById(R.id.taskTime);
            location = itemView.findViewById(R.id.taskLocation);
        }
    }
}