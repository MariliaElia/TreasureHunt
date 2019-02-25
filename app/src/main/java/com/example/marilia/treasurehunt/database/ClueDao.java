package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface ClueDao {
    @Insert
    public void insertClue(Clue clue);

}
