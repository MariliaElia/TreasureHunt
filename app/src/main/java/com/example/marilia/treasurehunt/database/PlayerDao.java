package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface PlayerDao {

    @Insert
    public void insertPlayer(Player player);

    @Query("SELECT * FROM Player WHERE user_id == :user_id AND th_id == :th_id AND status == :status")
    public Player checkForPlayer(int user_id, int th_id, String status);

    @Query("SELECT id FROM Player WHERE user_id == :userID AND th_id == :thID AND status == :status")
    public int getPlayerID(int userID, int thID, String status);

    @Query("UPDATE Player SET successful_clues =+ :clueReward, total_points =+ :pointReward WHERE th_id = :thID AND user_id = :userID " +
            "AND status == :status")
    public void updatePlayerTHValues(int clueReward, int pointReward, int thID, int userID, String status);

    @Query("UPDATE Player SET status = :status, end_date = :endDate, end_time = :endTime WHERE th_id = :thID AND user_id = :userID " +
            "AND status == :status")
    public void updatePlayerTHStatus(int thID, int userID, String status, Date endDate, Date endTime);
}
