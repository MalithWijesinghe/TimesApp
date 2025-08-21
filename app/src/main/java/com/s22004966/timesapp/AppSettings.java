package com.s22004966.timesapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AppSettings extends AppCompatActivity {
    EditText timerDuration, waterGoal;
    Button saveButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        timerDuration = findViewById(R.id.timerDuration);
        waterGoal = findViewById(R.id.waterGoal);
        saveButton = findViewById(R.id.saveButton);

        db = new DatabaseHelper(this);

        Cursor cursor = db.getSettings();
        if (cursor != null && cursor.moveToFirst()) {
            int timer = cursor.getInt(cursor.getColumnIndexOrThrow("TIMER"));
            int goal = cursor.getInt(cursor.getColumnIndexOrThrow("GOAL"));
            timerDuration.setText(String.valueOf(timer));
            waterGoal.setText(String.valueOf(goal));
        }
        if(cursor != null) cursor.close();

        saveButton.setOnClickListener(v -> {
            int timer = Integer.parseInt(timerDuration.getText().toString());
            int goal = Integer.parseInt(waterGoal.getText().toString());
            db.saveSettings(timer, goal);
            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        });
    }
}