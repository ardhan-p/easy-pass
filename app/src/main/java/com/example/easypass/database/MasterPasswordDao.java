package com.example.easypass.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MasterPasswordDao {
    @Insert
    public void insertMasterPassword(MasterPassword pw);

    @Update
    public void updateMasterPassword(MasterPassword pw);

    @Delete
    public void deleteMasterPassword(MasterPassword pw);

    @Query("SELECT master_password FROM MasterPassword WHERE id = 1")
    public String getMasterPassword();
}
