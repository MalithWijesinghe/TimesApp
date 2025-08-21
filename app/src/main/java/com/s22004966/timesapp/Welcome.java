package com.s22004966.timesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome extends AppCompatActivity {
    Button getStarted;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getStarted = findViewById(R.id.getStartedButton);
        login = findViewById(R.id.loginTextButton);

        getStarted.setOnClickListener(v -> {
                Intent intent = new Intent(Welcome.this, Signup.class);
                startActivity(intent);
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
        });

    }
}