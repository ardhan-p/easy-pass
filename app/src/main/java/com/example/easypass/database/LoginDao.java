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
    void insertLogin(Login login);

    @Update
    void updateLogin(Login login);

    @Delete
    void deleteLogin(Login login);

    @Query("SELECT * FROM Login")
    List<Login> getAllLogins();

    @Query("SELECT * FROM Login WHERE title LIKE '%' || :searchTerm || '%'")
    List<Login> getSearchedLogins(String searchTerm);

    @Query("SELECT * FROM Login WHERE title = :currentTitle")
    Login getLogin(String currentTitle);

    @Query("SELECT EXISTS (SELECT * FROM Login WHERE title = :currentTitle)")
    boolean checkLogin(String currentTitle);
}
