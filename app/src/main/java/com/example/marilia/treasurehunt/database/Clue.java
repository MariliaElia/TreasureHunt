package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = @Index("th_id"),foreignKeys = @ForeignKey(entity = TreasureHunt.class, parentColumns = "id", childColumns = "th_id", onDelete = CASCADE ))
public class Clue {
    @PrimaryKey(autoGenerate = true) public int id;

    public int th_id;

    public String clue;
    public String description;
    public double latitude;
    public double longitude;

    public Clue(String clue, String description, double latitude, double longitude, int th_id){
        this.clue = clue;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.th_id = th_id;
    }
}
