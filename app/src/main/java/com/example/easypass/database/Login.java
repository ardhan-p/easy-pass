package com.example.easypass.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class Login {
    @PrimaryKey (autoGenerate = true)
    public Integer id = 0;

    @ColumnInfo(name = "title")
    @NonNull
    public String loginTitle;

    @ColumnInfo(name = "username")
    @NonNull
    public String loginUsername;

    @ColumnInfo(name = "password")
    @NonNull
    public String loginPassword;

    public Login(String loginTitle, String loginUsername, String loginPassword) {
        this.loginTitle = loginTitle;
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    public Login() {

    }
}