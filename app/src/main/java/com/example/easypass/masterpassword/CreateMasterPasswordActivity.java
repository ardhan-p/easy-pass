package com.example.easypass.masterpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easypass.MainActivity;
import com.example.easypass.R;
import com.google.android.material.textfield.TextInputEditText;

public class CreateMasterPasswordActivity extends AppCompatActivity {
    TextInputEditText masterPasswordInput;
    Button confirmBtn;

    private void saveMasterPassword(String password) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("passwordPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("tutorialComplete", true);
        editor.putString("masterPassword", password);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_master_pw);
        getSupportActionBar().hide();

        Intent mainMenuActivityIntent = new Intent(this, MainActivity.class);

        masterPasswordInput = findViewById(R.id.searchLoginInput);
        confirmBtn = findViewById(R.id.confirmMasterPasswordBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: hash the master password and store in shared preferences
                String msg = masterPasswordInput.getText().toString();
                Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                t.show();
                startActivity(mainMenuActivityIntent);
            }
        });
    }
}
