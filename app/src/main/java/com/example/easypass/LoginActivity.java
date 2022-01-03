package com.example.easypass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText masterPasswordInput;
    Button loginBtn;


    private boolean validateMasterPassword(String inputPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] passwordParts = storedPassword.split(":");

        int iterations = Integer.parseInt(passwordParts[0]);
        byte[] salt = translateFromHex(passwordParts[1]);
        byte[] hash = translateFromHex(passwordParts[2]);

        PBEKeySpec keySpec = new PBEKeySpec(inputPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] newHash = skf.generateSecret(keySpec).getEncoded();

        int difference = hash.length ^ newHash.length;

        for (int i = 0; i < hash.length && i < newHash.length; i++) {
            difference |= hash[i] ^ newHash[i];
        }

        return difference == 0;
    }

    private byte[] translateFromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent mainMenuActivityIntent = new Intent(this, MainActivity.class);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("appPrefs", MODE_PRIVATE);

        masterPasswordInput = findViewById(R.id.loginMasterPasswordInput);
        loginBtn = findViewById(R.id.loginMasterPasswordButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = masterPasswordInput.getText().toString();
                String masterPassword = pref.getString(getString(R.string.prefs_master_password), null);

                try {
                    if (validateMasterPassword(newPassword, masterPassword)) {
                        startActivity(mainMenuActivityIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}