package com.example.easypass.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easypass.R;
import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.textfield.TextInputEditText;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;


public class CreateLoginActivity extends AppCompatActivity {
    private TextInputEditText accountInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private Button generatePasswordBtn;
    private Button createLoginBtn;

    // generates a random alphanumeric string that acts as a password
    // for future reference, perhaps add a custom way to change the characteristics of the pw string?
    private String generatePassword(int length) {
        String alphaNumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (alphaNumericChars.length() * Math.random());
            builder.append(alphaNumericChars.charAt(index));
        }

        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);

        accountInput = findViewById(R.id.viewLoginAccountNameInput);
        usernameInput = findViewById(R.id.viewLoginUsernameInput);
        passwordInput = findViewById(R.id.viewLoginPasswordInput);
        generatePasswordBtn = findViewById(R.id.generatePasswordButton);
        createLoginBtn = findViewById(R.id.createLoginObjectButton);

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

        generatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = generatePassword(20);
                passwordInput.setText(pw);
                Toast.makeText(getApplicationContext(), "Password created!", Toast.LENGTH_SHORT).show();
            }
        });

        createLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(accountInput.getText().toString())
                        || TextUtils.isEmpty(usernameInput.getText().toString())
                        || TextUtils.isEmpty(passwordInput.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                } else if (db.loginDao().checkLogin(accountInput.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Account name already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    Login newLogin = new Login(accountInput.getText().toString(),
                            usernameInput.getText().toString(),
                            passwordInput.getText().toString());

                    db.loginDao().insertLogin(newLogin);
                    Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}