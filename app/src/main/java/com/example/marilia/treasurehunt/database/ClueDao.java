package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ClueDao {
    @Insert
    public void insertClue(Clue clue);

    @Query("SELECT * FROM clue WHERE th_id == :thID")
    public Clue[] loadAllCluesforTH(int thID);
}
