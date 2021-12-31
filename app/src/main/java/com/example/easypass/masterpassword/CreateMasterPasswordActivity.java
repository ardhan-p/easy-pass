package com.example.easypass.masterpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easypass.MainActivity;
import com.example.easypass.R;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CreateMasterPasswordActivity extends AppCompatActivity {
    TextInputEditText masterPasswordInput;
    Button confirmBtn;

    private void saveMasterPassword(String password) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("appPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.prefs_intro_status), true);
        editor.putString(getString(R.string.prefs_master_password), password);
        editor.commit();
    }

    // source:
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1024;
        char[] chars = password.toCharArray();
        byte[] salt = generatePasswordSalt();

        PBEKeySpec keySpec = new PBEKeySpec(chars, salt, iterations,64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(keySpec).getEncoded();

        return iterations + ":" + translateToHex(salt) + ":" + translateToHex(hash);
    }

    private byte[] generatePasswordSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }


    private String translateToHex(byte[] byteArray) throws NoSuchAlgorithmException {
        BigInteger bigInt = new BigInteger(1, byteArray);
        String hex = bigInt.toString(16);

        int paddingLength = (byteArray.length * 2) - hex.length();

        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private byte[] translateFromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

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
                String newPassword = masterPasswordInput.getText().toString();

                try {
                    String newMasterPassword = generatePasswordHash(newPassword);
                    saveMasterPassword(newMasterPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                startActivity(mainMenuActivityIntent);
            }
        });
    }
}
