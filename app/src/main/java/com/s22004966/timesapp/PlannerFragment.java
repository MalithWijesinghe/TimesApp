package com.s22004966.timesapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.List;

public class PlannerFragment extends Fragment {

    EditText schedule, date, time;
    Button addButton, locationButton;
    private String selectedLocation = "";
    private ActivityResultLauncher<Intent> mapPickerLauncher;

    RecyclerView recyclerView;
    DatabaseHelper db;
    List<PlannerModel> plannerList;
    PlannerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        // Bind UI elements
        schedule = view.findViewById(R.id.scheduleInput);
        date = view.findViewById(R.id.scheduleDate);
        time = view.findViewById(R.id.scheduleTime);
        addButton = view.findViewById(R.id.addScheduleButton);
        locationButton = view.findViewById(R.id.locationPickButton);

        // Disable keyboard input on tap-only fields
        date.setFocusable(false);
        date.setClickable(true);
        time.setFocusable(false);
        time.setClickable(true);
        locationButton.setFocusable(false);
        locationButton.setClickable(true);

        recyclerView = view.findViewById(R.id.plannerRecyclerView);
        db = new DatabaseHelper(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load items from DB
        plannerList = db.getAllSchedules();
        adapter = new PlannerAdapter(plannerList);
        recyclerView.setAdapter(adapter);

        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Date Picker
        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        String formattedDate = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedDate.getTime());
                        date.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Time Picker
        time.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view12, selectedHour, selectedMinute) -> {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        selectedTime.set(Calendar.MINUTE, selectedMinute);
                        String formattedTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(selectedTime.getTime());
                        time.setText(formattedTime);
                    },
                    hour, minute, false
            );
            timePickerDialog.show();
        });

        // Map Picker result handler
        mapPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        // Get address string from MapActivity
                        selectedLocation = result.getData().getStringExtra("address");
                        locationButton.setText(selectedLocation != null ? selectedLocation : "Unknown Location");
                    }
                });

        // Launch MapActivity
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            mapPickerLauncher.launch(intent);
        });

        // Add Schedule Button
        addButton.setOnClickListener(v -> {
            String scheduleString = schedule.getText().toString().trim();
            String dataString = date.getText().toString().trim();
            String timeString = time.getText().toString().trim();

            if (scheduleString.isEmpty() || dataString.isEmpty() || timeString.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            db.insertSchedule(scheduleString, dataString, timeString, selectedLocation);
            loadSchedules();

            schedule.setText("");
            date.setText("");
            time.setText("");
            locationButton.setText("Location");

            Toast.makeText(getContext(), "Schedule added", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadSchedules() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        plannerList.clear();
        plannerList.addAll(db.getAllSchedules());
        adapter.notifyDataSetChanged();
    }
}
