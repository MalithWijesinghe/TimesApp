package com.s22004966.timesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FocusFragment extends Fragment {

    TextView hour;
    TextView minute;
    TextView second;
    Button start;

    int duration = 60 * 25;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        hour = view.findViewById(R.id.hourText);
        minute = view.findViewById(R.id.minText);
        second = view.findViewById(R.id.secondText);
        start = view.findViewById(R.id.startButton);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(duration * 1000L, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long hrs = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                        long min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                        long secs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                        hour.setText(String.format(Locale.getDefault(), "%02d", hrs));
                        minute.setText(String.format(Locale.getDefault(), "%02d", min));
                        second.setText(String.format(Locale.getDefault(), "%02d", secs));
                    }

                    public void onFinish() {
                        hour.setText("00");
                        minute.setText("00");
                        second.setText("00");
                        duration = 25 * 60; // reset
                    }
                }.start();
            }
        });

        return view;
    }
}