package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true) public int id;

    public String first_name;
    public String last_name;
    public String dob;
    public String email;
    public int points;

    public User(String first_name, String last_name, String dob, String email, int points){
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.points = points;
    }
}
