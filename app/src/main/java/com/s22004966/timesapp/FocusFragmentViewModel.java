package com.s22004966.timesapp;

import android.database.Cursor;
import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FocusFragmentViewModel extends ViewModel {
    private final MutableLiveData<String> timeLeft = new MutableLiveData<>();
    private final MutableLiveData<Boolean> timerFinished = new MutableLiveData<>();

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long remainingMillis;
    private long initialMillis; // store initial timer value

    private DatabaseHelper db;

    // Constructor to set DatabaseHelper
    public void init(DatabaseHelper helper) {
        this.db = helper;

        // Load TIMER value from settings
        Cursor cursor = db.getSettings();
        int timerMinutes = 25; // default value
        if (cursor.moveToFirst()) {
            timerMinutes = cursor.getInt(cursor.getColumnIndexOrThrow("TIMER"));
        }
        cursor.close();

        initialMillis = timerMinutes * 60 * 1000L;
        remainingMillis = initialMillis;

        // Set initial time text
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", timerMinutes, 0);
        timeLeft.postValue(formatted);
    }

    public LiveData<String> getTimeLeft() {
        return timeLeft;
    }

    public LiveData<Boolean> getTimerFinished() {
        return timerFinished;
    }

    public void startTimer() {
        if (isRunning) return;

        countDownTimer = new CountDownTimer(remainingMillis, 1000) {
            @Override
            public void onTick(long millisToFinish) {
                remainingMillis = millisToFinish;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisToFinish);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisToFinish) % 60;
                String formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timeLeft.postValue(formatted);
            }

            @Override
            public void onFinish() {
                timeLeft.postValue(String.format(Locale.getDefault(), "%02d:%02d", 0, 0));
                isRunning = false;
                timerFinished.postValue(true);
            }
        };
        countDownTimer.start();
        isRunning = true;
    }

    public void pause() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    public void reset() {
        pause();
        remainingMillis = initialMillis; // reset to settings value
        long minutes = TimeUnit.MILLISECONDS.toMinutes(initialMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(initialMillis) % 60;
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeLeft.postValue(formatted);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        pause();
    }

    public void resetTimerFinishedFlag() {
        timerFinished.postValue(false);
    }
}
