package com.s22004966.timesapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Objects;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNav;
    String title;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FocusFragment()).commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.focus:
                    selectedFragment = new FocusFragment();
                    title = "Focus Session";
                    break;
                case R.id.planner:
                    selectedFragment = new PlannerFragment();
                    title = "Day Planner";
                    break;
                case R.id.todo:
                    selectedFragment = new TodoFragment();
                    title = "To Do";
                    break;
                case R.id.notes:
                    selectedFragment = new NotesFragment();
                    title = "Notes";
                    break;
                case R.id.progress:
                    selectedFragment = new ProgressFragment();
                    title = "";
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(title);
            }
            return true;
        });
    }
}