package com.example.easypass.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class Login {
    @PrimaryKey
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

    @NonNull
    public String getLoginTitle() {
        return loginTitle;
    }

    @NonNull
    public String getLoginUsername() {
        return loginUsername;
    }

    @NonNull
    public String getLoginPassword() {
        return loginPassword;
    }

    public Login() {

    }
}
