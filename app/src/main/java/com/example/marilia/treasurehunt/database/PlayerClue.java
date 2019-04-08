package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("th_id"), @Index("user_id"), @Index("clue_id")},foreignKeys ={
        @ForeignKey(entity = TreasureHunt.class,
            parentColumns = "id",
            childColumns = "th_id",
            onDelete=CASCADE),
        @ForeignKey(entity = User.class,
            parentColumns = "id",
            childColumns = "user_id"),
        @ForeignKey(entity = Clue.class,
            parentColumns = "id",
            childColumns = "clue_id")})
public class PlayerClue {
    @PrimaryKey(autoGenerate = true) public int id;

    public int th_id;
    public int user_id;
    public int clue_id;

    public String status;

    public PlayerClue(int th_id, int user_id, int clue_id, String status){
        this.th_id = th_id;
        this.user_id = user_id;
        this.clue_id = clue_id;
        this.status = status;
    }
}
