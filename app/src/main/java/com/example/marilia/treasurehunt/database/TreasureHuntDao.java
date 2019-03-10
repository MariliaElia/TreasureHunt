package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TreasureHuntDao {
    @Insert
    public long insertTreasureHunt(TreasureHunt treasureHunt);

    @Query("SELECT * FROM treasurehunt")
    public TreasureHunt[] loadAllTreasureHunts();

    @Query("SELECT * FROM treasurehunt WHERE town = :town")
    public TreasureHunt[] loadAllTreasureHuntsInTown(String town);

    @Query("SELECT * FROM treasurehunt WHERE country = :country")
    public TreasureHunt[] loadAllTreasureHuntsInCountry(String country);

    //Update status of a treasure hunt
    @Query("UPDATE treasurehunt SET status = :status WHERE id = :thID")
    public void updateTreasureHuntStatus(String status, int thID);

    //Delete Treasure Hunt by ID
    @Query("DELETE FROM treasurehunt WHERE id = :thID")
    public void deleteTreasureHunt(int thID);

}
