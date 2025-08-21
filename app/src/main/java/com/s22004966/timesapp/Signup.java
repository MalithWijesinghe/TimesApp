package com.s22004966.timesapp;

import android.content.Intent;
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

public class Signup extends AppCompatActivity {
    DatabaseHelper myDb;
    Button signup;
    TextView login;
    EditText email, name, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        myDb = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signup = findViewById(R.id.signupButton);
        login = findViewById(R.id.loginTextButton);
        name = findViewById(R.id.name);
        email = findViewById(R.id.newEmail);
        password = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);


        signup.setOnClickListener(v -> {
            if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(Signup.this, "Pleas fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isEmailExist = myDb.validateEmail(email.getText().toString());
            if(isEmailExist) {
                Toast.makeText(Signup.this, "Email is already registered", Toast.LENGTH_LONG).show();
                return;
            }
            if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(Signup.this, "Password doesn't match. Please Check", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(Signup.this, Login.class);
            addData();
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
        });
    }

    public void addData() {
        boolean isInserted = myDb.insertLoginData(email.getText().toString(), name.getText().toString(), password.getText().toString());
        if(isInserted == true) {
            Toast.makeText(Signup.this, "Account successfully created. Please login.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Signup.this, "Try again", Toast.LENGTH_LONG).show();
        }
    }
}