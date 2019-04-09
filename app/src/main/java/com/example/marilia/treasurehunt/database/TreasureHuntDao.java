package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TreasureHuntDao {
    @Insert
    public long insertTreasureHunt(TreasureHunt treasureHunt);

    @Query("SELECT * FROM treasurehunt WHERE town = :town")
    public TreasureHunt[] loadAllTreasureHuntsInTown(String town);

    @Query("SELECT * FROM treasurehunt WHERE user_id = :user_id")
    public TreasureHunt[] loadAllTreasureHuntsCreatedBy(int user_id);

    @Query("SELECT * FROM treasurehunt WHERE id IN (:thIDs)")
    public TreasureHunt[] loadAllTreasureHuntsWith(int[] thIDs);

    //Update status of a treasure hunt
    @Query("UPDATE treasurehunt SET status = :status WHERE id = :thID")
    public void updateTreasureHuntStatus(String status, int thID);

    //Delete Treasure Hunt by ID
    @Query("DELETE FROM treasurehunt WHERE id = :thID")
    public void deleteTreasureHunt(int thID);

}
