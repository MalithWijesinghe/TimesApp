package com.s22004966.timesapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FocusFragment extends Fragment {

    private FocusFragmentViewModel viewModel;
    MediaPlayer mediaPlayer;
    private TextView minute;
    private TextView second;
    private Button start, pause, reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        minute = view.findViewById(R.id.minuteText);
        second = view.findViewById(R.id.secondText);
        start = view.findViewById(R.id.startButton);
        pause = view.findViewById(R.id.pauseButton);
        reset = view.findViewById(R.id.resetButton);

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.timesup); // media player to notification sound

        DatabaseHelper db = new DatabaseHelper(requireContext());
        viewModel = new ViewModelProvider(requireActivity()).get(FocusFragmentViewModel.class);
        viewModel.init(db); // pass DatabaseHelper

        // Take time left to display on focus timer
        viewModel.getTimeLeft().observe(getViewLifecycleOwner(), formatted -> {
            String[] split = formatted.split(":");
            minute.setText(split[0]);
            second.setText(split[1]);
        });

        // check timer finished and play notification sound
        viewModel.getTimerFinished().observe(getViewLifecycleOwner(), finished -> {
            if (finished) {
                mediaPlayer.start();
                viewModel.resetTimerFinishedFlag(); // used to indicate timer is finished
            }
        });

        start.setOnClickListener(v -> viewModel.startTimer());
        pause.setOnClickListener(v -> viewModel.pause());
        reset.setOnClickListener(v -> viewModel.reset());

        return view;
    }
}