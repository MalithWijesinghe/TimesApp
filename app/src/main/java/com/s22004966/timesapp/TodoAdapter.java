package com.s22004966.timesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private DatabaseHelper db;

    public TodoAdapter(DatabaseHelper db) {
        this.db = db;
        this.mList = new ArrayList<>();
    }

    public void setTasks(List<ToDoModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_card_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(item.getStatus() == 1);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.updateStatus(item.getId(), isChecked ? 1 : 0);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public ToDoModel getTaskAt(int position) {
        return mList.get(position);
    }

    public void removeTask(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
}