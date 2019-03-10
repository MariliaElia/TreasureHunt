package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(indices = @Index(value = "id", unique = true),
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = CASCADE))
public class TreasureHunt {
    @PrimaryKey(autoGenerate = true) int id;

    public String title;
    public String description;

    @TypeConverters({Converters.class})
    public Date date_created;
    @TypeConverters({Converters.class})
    public Date open_on;
    @TypeConverters({Converters.class})
    public Date close_on;

    public Date startTime;
    public Date endTime;

    public String country;
    public String town;

    public int user_id;

    public String status;

    public TreasureHunt(String title, String description, Date date_created, Date open_on, Date startTime, Date endTime, Date close_on, String country, String town, int user_id, String status) {
        this.title = title;
        this.description = description;
        this.date_created = date_created;
        this.open_on = open_on;
        this.startTime = startTime;
        this.close_on = close_on;
        this.endTime = endTime;
        this.country = country;
        this.town = town;
        this.user_id = user_id;
        this.status = status;
    }

    //Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDate_created() { return date_created; }
    public Date getOpen_on() { return open_on; }
    public Date getClose_on() {return close_on; }
    public String getCountry() { return country; }
    public String getTown() { return town; }
    public int getUser_id() { return user_id; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate_created(Date date_created) { this.date_created = date_created; }
    public void setOpen_on(Date open_on) { this.open_on = open_on; }
    public void setClose_on(Date close_on) { this.close_on = close_on; }
    public void setCountry(String country) { this.country = country; }
    public void setTown(String town) { this.town = town; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

}
