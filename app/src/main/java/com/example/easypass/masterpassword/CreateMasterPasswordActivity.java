package com.example.easypass.masterpassword;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.easypass.R;

public class CreateMasterPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_master_pw);
        getSupportActionBar().hide();
    }
}
