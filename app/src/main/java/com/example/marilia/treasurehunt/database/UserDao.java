package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public void insertUser(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("DELETE FROM User")
    public void nukeTable();

    @Query("SELECT * FROM User WHERE username == :username")
    User findUserByUsername(String username);

    @Query(("SELECT id FROM User WHERE username == :username"))
    int getUserID(String username);

    @Delete
    void deleteUser(User user);


}
