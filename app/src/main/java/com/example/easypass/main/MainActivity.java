package com.example.easypass.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.easypass.R;
import com.example.easypass.database.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import net.sqlcipher.database.*;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // needed to get the passphrase for SQLCipher
        SharedPreferences pref = getApplicationContext().getSharedPreferences("appPrefs", MODE_PRIVATE);
        char[] masterPassword = pref.getString(getString(R.string.prefs_master_password), null).toCharArray();

        // initialises access with local room database
        final byte[] passphrase = SQLiteDatabase.getBytes(masterPassword);
        final SupportFactory factory = new SupportFactory(passphrase);
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "easypass-db")
                .openHelperFactory(factory)
                .allowMainThreadQueries()
                .build();

        // initialises default fragment with home fragment in fragment viewer
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();

        // switches displayed fragment depending on what the user has selected on nav bar
        navBar = findViewById(R.id.mainNavBar);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                SettingsFragment setFrag = null;

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

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, frag).addToBackStack(null).commit();
                return true;
            }
        });
    }

    @Override
    public void onRestart() {
        //TODO: optimise the refresh process of login list
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    // closes app if user pressed back on this activity
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}