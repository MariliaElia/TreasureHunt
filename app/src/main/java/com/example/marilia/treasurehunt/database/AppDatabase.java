package com.example.marilia.treasurehunt.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {User.class, TreasureHunt.class, Clue.class, Player.class}, version = 1 )
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "AppDatabase.db";
    private static AppDatabase instance;
    abstract public TreasureHuntDao treasureHuntDao();
    abstract public UserDao userDao();
    abstract public PlayerDao playerDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    AppDatabase.class,
                    DB_NAME).build();
        }
        return instance;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // we have not made any change so far
        }
    };
}
