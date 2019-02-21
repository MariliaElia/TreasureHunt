package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

public interface UserDao {
    @Insert
    public void insertUser(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Delete
    void deleteUser(User user);

}
