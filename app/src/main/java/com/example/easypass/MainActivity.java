package com.example.easypass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialises access with local room database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "easypass-db")
                .allowMainThreadQueries()
                .build();

        // initialises default fragment with home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();

        // switches displayed fragment depending on what the user has selected on nav bar
        navBar = findViewById(R.id.mainNavBar);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        frag = new HomeFragment();
                        break;

                    case R.id.nav_search:
                        frag = new SearchFragment();
                        break;

                    case R.id.nav_settings:
                        frag = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, frag).commit();
                return true;
            }
        });
    }

    // closes app if user pressed back on this activity
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}