package com.example.easypass.masterpassword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easypass.R;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class EditMasterPasswordActivity extends AppCompatActivity {
    TextInputEditText newMasterPasswordInput;
    TextInputEditText confirmMasterPasswordInput;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_master_pw);

        newMasterPasswordInput = findViewById(R.id.loginMasterPasswordInput1);
        confirmMasterPasswordInput = findViewById(R.id.loginMasterPasswordInput2);
        confirmBtn = findViewById(R.id.editMasterPasswordBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newMasterPasswordInput.getText().toString();
                String confirmPassword = confirmMasterPasswordInput.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    try {
                        String newMasterPassword = generatePasswordHash(confirmPassword);
                        saveMasterPassword(newMasterPassword);
                        Toast.makeText(getApplicationContext(), "Successfully updated master password!", Toast.LENGTH_SHORT).show();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
