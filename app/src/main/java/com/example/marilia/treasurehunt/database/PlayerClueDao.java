package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface PlayerClueDao {

    @Insert
    public void insertPlayerClue(PlayerClue playerClue);

    @Query("SELECT clue_id FROM playerclue WHERE th_id == :thID AND user_id == :userID AND " +
            "status == :status")
    public int getLastClueID(int thID, int userID, String status);

    @Query("SELECT id FROM playerclue WHERE th_id == :thID AND user_id == :userID  AND " +
            "clue_id = :clueID ")
    public int getPlayerClueID(int thID, int userID, int clueID);

    @Query("UPDATE PlayerClue SET status = :status WHERE th_id = :thID AND user_id == :userID AND " +
            "clue_id = :clueID")
    public void updatePlayerClueStatus(String status, int thID, int userID, int clueID);
}
