package com.example.marilia.treasurehunt.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.constraint.ConstraintLayout;

@Database(entities = {User.class, TreasureHunt.class, Clue.class, Player.class}, version = 1 )
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    abstract public TreasureHuntDao treasureHuntDao();
    abstract public UserDao userDao();
    abstract public PlayerDao playerDao();

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





}
