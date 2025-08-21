package com.s22004966.timesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences("TimesAppPreferences", MODE_PRIVATE);
            boolean isLogged = preferences.getBoolean("isLogged", false);

            Intent intent;
            if (isLogged) {
                intent = new Intent(MainActivity.this, Authentication.class);
            } else {
                intent = new Intent(MainActivity.this, Welcome.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_TIMER);
    }
}