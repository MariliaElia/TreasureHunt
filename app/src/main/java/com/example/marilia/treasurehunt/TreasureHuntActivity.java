package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.TreasureHunt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TreasureHuntActivity extends AppCompatActivity {
    private static final String TAG = "TreasureHuntActivity";

    String title, description;
    Date startDate, startTime, endDate, endTime;

    TextView titleTV, descriptionTV, startTV, endTV;

    Button play;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_th);

        Intent intent = getIntent();
        TreasureHunt th = intent.getParcelableExtra("treasureHunt");

        title = th.title;
        description = th.description;
        startDate = th.open_on;
        startTime = th.startTime;
        endDate = th.close_on;
        endTime = th.endTime;

        titleTV = (TextView) findViewById(R.id.title);
        descriptionTV = (TextView) findViewById(R.id.description);
        startTV = (TextView) findViewById(R.id.starts);
        endTV = (TextView) findViewById(R.id.ends);

        titleTV.setText(title);
        descriptionTV.setText(description);
        startTV.setText(dateToString(startDate, startTime));
        endTV.setText(dateToString(endDate, endTime));

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TreasureHuntActivity.this, GameActivity.class);
                startActivity(intent);
                //Add it to treasure hunts currently playing
                //Check if it is already started by the user
                //call game activity
            }
        });
    }

    public String dateToString(Date date, Date time){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String strTime = timeFormat.format(time);

        String dateTime = strDate + " " + strTime;

        return dateTime;
    }



}
