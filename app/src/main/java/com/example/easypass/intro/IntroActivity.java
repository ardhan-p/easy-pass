package com.example.easypass.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.easypass.R;
import com.example.easypass.masterpassword.CreateMasterPasswordActivity;
import com.google.android.material.tabs.TabLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager pager;
    Button nextBtn;
    int pagePosition = 0;
    boolean readyToStart = false;
    IntroViewPagerAdapter adapter;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // intent for starting next activity
        Intent masterPasswordActivityIntent = new Intent(this, CreateMasterPasswordActivity.class);

        // hides top action bar for aesthetics
        getSupportActionBar().hide();

        // create intro screen objects for viewpager
        // TODO: add screenshots on relevant screens (locked.png is currently a placeholder)
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

        // button action listener
        nextBtn = findViewById(R.id.introNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagePosition = pager.getCurrentItem();

                if (readyToStart) {
                    startActivity(masterPasswordActivityIntent);
                }

                if (pagePosition < screens.size()) {
                    readyToStart = false;
                    pagePosition++;
                    pager.setCurrentItem(pagePosition);
                }

                if (pagePosition == screens.size() - 1) {
                    readyToStart = true;
                    nextBtn.setText(getResources().getString(R.string.btn_get_started));
                    nextBtn.setTextColor(getResources().getColor(R.color.white));
                    nextBtn.setBackgroundColor(getResources().getColor(R.color.blue_btn_color));
                    indicator.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}