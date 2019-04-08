package com.example.marilia.treasurehunt.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.constraint.ConstraintLayout;

@Database(entities = {User.class, TreasureHunt.class, Clue.class, Player.class, PlayerClue.class}, version = 3 )
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    abstract public TreasureHuntDao treasureHuntDao();
    abstract public UserDao userDao();
    abstract public ClueDao clueDao();
    abstract public PlayerDao playerDao();
    abstract public PlayerClueDao playerClueDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // we have not made any change so far
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Player "
                    + " ADD COLUMN user_id INTEGER");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User "
                    + " ADD COLUMN uuid STRING");
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User "
                    + " ADD COLUMN username STRING");
            database.execSQL("ALTER TABLE User "
                    + " ADD COLUMN password STRING");
        }
    };

    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TreasureHunt "
                    + " ADD COLUMN status STRING");
        }
    };

    public static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TreasureHunt "
                    + " ADD COLUMN startTime DATE");
            database.execSQL("ALTER TABLE TreasureHunt "
                    + " ADD COLUMN endTime DATE");
        }
    };

}
