package com.example.easypass.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MasterPassword {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "master_password")
    @NonNull
    public String masterPassword;


    // TODO: create master password constructors
}
