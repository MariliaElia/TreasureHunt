package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface TreasureHuntDao {
    @Insert
    public long insertTreasureHunt(TreasureHunt treasureHunt);

    @Query("SELECT * FROM treasurehunt WHERE :currentDate <= close_on AND town >= :town ORDER BY town ASC, id DESC")
    public TreasureHunt[] loadAllTreasureHuntsInTown(String town, Date currentDate);

    @Query("SELECT * FROM treasurehunt WHERE user_id = :user_id")
    public TreasureHunt[] loadAllTreasureHuntsCreatedBy(int user_id);

    @Query("SELECT * FROM treasurehunt WHERE id IN (:thIDs)")
    public TreasureHunt[] loadAllTreasureHuntsWith(int[] thIDs);

    //Update status of a treasure hunt
    @Query("UPDATE treasurehunt SET status = :status WHERE id = :thID")
    public void updateTreasureHuntStatus(String status, int thID);

    @Query("UPDATE treasurehunt SET close_on = :date WHERE id IN (3,5,9,20)")
    public void updateDates(Date date);

    //Delete Treasure Hunt by ID
    @Query("DELETE FROM treasurehunt WHERE id = :thID")
    public void deleteTreasureHunt(int thID);

}
