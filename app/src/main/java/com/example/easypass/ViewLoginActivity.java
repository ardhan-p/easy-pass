package com.example.easypass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.textfield.TextInputEditText;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

public class ViewLoginActivity extends AppCompatActivity {
    private TextInputEditText accountInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private Button saveEditsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_login);

        accountInput = findViewById(R.id.viewLoginAccountNameInput);
        usernameInput = findViewById(R.id.viewLoginUsernameInput);
        passwordInput = findViewById(R.id.viewLoginPasswordInput);
        saveEditsBtn = findViewById(R.id.saveEditsButton);

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

        Intent prevIntent = getIntent();
        String title = prevIntent.getStringExtra("title");

        // sets login object's variables to the text fields
        Login currentLogin = db.loginDao().getLogin(title);

        accountInput.setText(currentLogin.getLoginTitle());
        usernameInput.setText(currentLogin.getLoginUsername());
        passwordInput.setText(currentLogin.getLoginPassword());

        saveEditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add inevitable error handling lol
                Toast.makeText(getApplicationContext(), "Edit successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}