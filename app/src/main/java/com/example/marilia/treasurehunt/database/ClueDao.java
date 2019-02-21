package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Insert;

public interface ClueDao {
    @Insert
    public void insertClue(Clue clue);

}
