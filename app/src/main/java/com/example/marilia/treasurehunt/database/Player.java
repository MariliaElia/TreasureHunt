package com.example.marilia.treasurehunt.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = @Index("th_id"), foreignKeys ={@ForeignKey(entity = TreasureHunt.class,
        parentColumns = "id",
        childColumns = "th_id",
        onDelete=CASCADE),@ForeignKey(entity = User.class,
        parentColumns = "id", childColumns = "user_id")})
public class Player {
    @PrimaryKey(autoGenerate = true) public int id;

    @TypeConverters({Converters.class})
    public Date start_date;
    @TypeConverters({Converters.class})
    public Date start_time;
    @TypeConverters({Converters.class})
    public Date end_date;
    @TypeConverters({Converters.class})
    public Date end_time;

    public int total_points;
    public int successful_clues;
    public String status;

    public int th_id;
    public int user_id;

    public Player(Date start_date, Date start_time, Date end_date, Date end_time, int total_points, int successful_clues, String status, int th_id, int user_id){
        this.start_date = start_date;
        this.start_time = start_time;
        this.end_date = end_date;
        this.end_time = end_time;
        this.total_points = total_points;
        this.successful_clues = successful_clues;
        this.status = status;

        this.th_id = th_id;
        this.user_id = user_id;
    }

    //Getters
    public int getId() { return id; }
    public Date getEnd_date() { return end_date; }
    public Date getEnd_time() { return end_time; }
    public Date getStart_date() { return start_date; }
    public Date getStart_time() { return start_time; }
    public String getStatus() { return status; }
    public int getSuccessful_clues() { return successful_clues; }
    public int getTh_id() { return th_id; }
    public int getTotal_points() { return total_points; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setEnd_date(Date end_date) { this.end_date = end_date; }
    public void setEnd_time(Date end_time) { this.end_time = end_time; }
    public void setStart_date(Date start_date) { this.start_date = start_date; }
    public void setStart_time(Date start_time) { this.start_time = start_time; }
    public void setStatus(String status) { this.status = status; }
    public void setSuccessful_clues(int successful_clues) { this.successful_clues = successful_clues; }
    public void setTh_id(int th_id) { this.th_id = th_id; }
    public void setTotal_points(int total_points) { this.total_points = total_points; }
}
