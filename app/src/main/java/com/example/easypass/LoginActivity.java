package com.example.easypass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {

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
    }
}