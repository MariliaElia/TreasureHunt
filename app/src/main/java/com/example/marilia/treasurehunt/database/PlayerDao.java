package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface PlayerDao {

    @Insert
    public long insertPlayer(Player player);

    @Query("SELECT * FROM Player WHERE user_id == :user_id AND th_id == :th_id AND status == :status")
    public Player checkForPlayer(int user_id, int th_id, String status);

    @Query("SELECT id FROM Player WHERE user_id == :userID AND th_id == :thID AND status == :status")
    public int getPlayerID(int userID, int thID, String status);

    @Query("SELECT th_id FROM Player WHERE user_id == :user_id AND status == :status")
    public int[] getTreasureHuntIDs(int user_id, String status);

    @Query("SELECT * FROM Player WHERE id == :player_id")
    public Player getPlayerData(int player_id);

    @Query("UPDATE Player SET successful_clues = successful_clues+:clueReward, total_points = total_points+:pointReward WHERE th_id = :thID AND user_id = :userID " +
            "AND status == :status")
    public void updatePlayerTHValues(int clueReward, int pointReward, int thID, int userID, String status);

    @Query("UPDATE Player SET status = :statusCompleted, end_date = :endDate, end_time = :endTime WHERE th_id = :thID AND user_id = :userID " +
            "AND status == :statusPending")
    public void updatePlayerTHStatus(int thID, int userID,String statusPending, String statusCompleted, Date endDate, Date endTime);

}
