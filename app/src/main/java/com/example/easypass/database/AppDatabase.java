package com.example.easypass.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Login.class, MasterPassword.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();
    public abstract MasterPasswordDao masterPasswordDao();
}
