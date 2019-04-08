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

    @Query("SELECT id FROM clue WHERE th_id == :thID ORDER BY id LIMIT 1 OFFSET 0")
    public int loadFistClueID(int thID);

    @Query("SELECT * FROM clue WHERE th_id == :thID AND id == :clueID")
    public Clue loadClue(int thID, int clueID);

}
