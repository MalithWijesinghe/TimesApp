package com.s22004966.timesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    Button login;
    TextView signup;
    DatabaseHelper db;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupTextButton);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(Login.this, "Pleas fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isValid = db.checkLogin(email.getText().toString(), password.getText().toString());


            if(isValid) {
                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences("TimesAppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLogged", true);
                editor.apply();

                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }

        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });

    }
}