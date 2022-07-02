package com.example.easypass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.example.easypass.database.LoginDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import net.sqlcipher.database.*;

import java.io.File;
import java.util.List;

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

        // working the sql room shenanigans

//        Login testLogin = new Login("title", "username", "password");
//        Login testLogin = new Login("Instagram", "bruh", "moment");
//        db.loginDao().insertLogin(testLogin);
//        db.loginDao().deleteLogin(testLogin);

//        List<Login> loginList = db.loginDao().getAllLogins();
//
//        for (Login l : loginList) {
//            Log.d("I", l.getLoginTitle());
//        }

//        Login newLogin = db.loginDao().getLogin(0);
//        Log.d("I", newLogin.getLoginTitle());
//        Log.d("I", newLogin.getLoginUsername());

        // initialises default fragment with home fragment in fragment viewer
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

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, frag).addToBackStack(null).commit();
                return true;
            }
        });
    }

    @Override
    public void onRestart() {
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