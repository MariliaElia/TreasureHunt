package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"username"}, unique = true), @Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true) public int id;
    public String uuid;

    public String first_name;
    public String last_name;
    @NonNull public String username;
    @NonNull public String password;
    public String dob;
    @NonNull public String email;
    public int points;

    public User(String first_name, String last_name,String username, String password, String dob, String email, int points){
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.points = points;
    }

    public int getUserId() { return this.id; }
    public String getPassword() {return this.password;}

}
