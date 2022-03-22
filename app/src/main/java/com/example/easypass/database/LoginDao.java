package com.example.easypass.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LoginDao {
    @Insert
    public void insertLogin(Login login);

    @Update
    public void updateLogin(Login login);

    @Delete
    public void deleteLogin(Login login);

    @Query("SELECT * FROM Login")
    public List<Login> getAllLogins();

    @Query("SELECT * FROM Login WHERE title LIKE '%' || :searchTerm || '%'")
    public List<Login> getSearchedLogins(String searchTerm);

    @Query("SELECT * FROM Login WHERE title = :currentTitle")
    public Login getLogin(int currentTitle);
}
