package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

public interface TreasureHuntDao {
    @Insert
    public void insertTreasureHunt(TreasureHunt treasureHunt);

    @Query("SELECT * FROM treasurehunt")
    public TreasureHunt[] loadAllTreasureHunts();

    @Query("SELECT * FROM treasurehunt WHERE town == :town")
    public TreasureHunt[] loadAllTreasureHuntsInTown(String town);

    @Query("SELECT * FROM treasurehunt WHERE country == :country")
    public TreasureHunt[] loadAllTreasureHuntsInCountry(String country);

}
