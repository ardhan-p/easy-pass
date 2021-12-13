package com.example.easypass.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.easypass.R;
import com.google.android.material.tabs.TabLayout;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager pager;
    IntroViewPagerAdapter adapter;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // create intro screen objects for viewpager
        List<IntroScreen> screens = new ArrayList<>();
        screens.add(new IntroScreen("Welcome to EasyPass!", R.drawable.locked));
        screens.add(new IntroScreen("The app allows you to make passwords for your accounts and store them in a secure place!", R.drawable.locked));
        screens.add(new IntroScreen("Create new login credentials with the plus button.", R.drawable.locked));
        screens.add(new IntroScreen("Input your account details and generate password.", R.drawable.locked));
        screens.add(new IntroScreen("Password will be securely encrypted and will be available in the homepage!", R.drawable.locked));

        // initialises viewpager with adapter with all of the screens
        pager = findViewById(R.id.introViewPager);
        adapter = new IntroViewPagerAdapter(screens, this);
        pager.setAdapter(adapter);

        // initialises tabview bullet indicator
        indicator = findViewById(R.id.introTabLayout);
        indicator.setupWithViewPager(pager);
    }
}