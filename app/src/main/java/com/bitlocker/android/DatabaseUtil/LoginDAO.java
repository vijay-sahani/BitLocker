package com.bitlocker.android.DatabaseUtil;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bitlocker.android.Models.LoginCredential;

import java.util.List;

@Dao
public interface LoginDAO {
    @Insert(onConflict = REPLACE)
    void insert(LoginCredential login);

    @Query("SELECT * FROM login_credentials order by id desc")
    List<LoginCredential> getAll();

    @Query("update login_credentials set title=:title, username=:username, password=:password, url=:url where ID=:id")
    void update(int id, String title,String username,String password,String url);

    @Delete
    void delete(LoginCredential login);
}
