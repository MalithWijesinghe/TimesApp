package com.s22004966.timesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProgressFragment extends Fragment {

    TextView currentWaterLevel, targetWaterLevel;
    Button addWaterButton, settingsButton;
    private int current;
    private int goal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        DatabaseHelper db = new DatabaseHelper(getActivity());

        currentWaterLevel = view.findViewById(R.id.currentWaterLevel);
        targetWaterLevel = view.findViewById(R.id.targetWaterLevel);
        addWaterButton = view.findViewById(R.id.addWaterButton);
        settingsButton = view.findViewById(R.id.settingsButton);

        // Load values from database
        Cursor cursor = db.getSettings();
        if (cursor.moveToFirst()) {
            goal = cursor.getInt(cursor.getColumnIndexOrThrow("GOAL"));
            current = cursor.getInt(cursor.getColumnIndexOrThrow("CURRENTLEVEL"));
        }
        cursor.close();

        targetWaterLevel.setText(String.valueOf(goal));
        currentWaterLevel.setText(String.valueOf(current));

        addWaterButton.setOnClickListener(v -> {
            int addAmount = 1000;
            current += addAmount;
            if (current > goal) current = goal;
            currentWaterLevel.setText(String.valueOf(current));
            db.updateCurrentWater(current);
        });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AppSettings.class));
        });

        return view;
    }
}