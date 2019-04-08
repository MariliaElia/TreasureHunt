package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.Clue;
import com.example.marilia.treasurehunt.database.Player;
import com.example.marilia.treasurehunt.database.PlayerClue;
import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TreasureHuntActivity extends AppCompatActivity {
    private static final String TAG = "TreasureHuntActivity";

    //As the player is just starting the Treasure hunt the variables are set to 0;
    final int CLUES_FOUND = 0;
    final int PLAYER_POINTS = 0;
    final String STATUS = "pending";

    String title, description;
    Date openDate, startTime, closeDate, endTime, currentDate;

    TextView titleTV, descriptionTV, startTV, endTV;
    int thID, userID, firstClueID;

    Button play;

    Player player;
    Clue cl;

    private SharedPreferenceConfig preferenceConfig;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_th);

        //Passed treasure hunt ID
        Intent intent = getIntent();
        TreasureHunt th = intent.getParcelableExtra("treasureHunt");

        thID = th.getId();
        title = th.title;
        description = th.description;
        openDate = th.open_on;
        startTime = th.startTime;
        closeDate = th.close_on;
        endTime = th.endTime;

        titleTV = (TextView) findViewById(R.id.title);
        descriptionTV = (TextView) findViewById(R.id.description);
        startTV = (TextView) findViewById(R.id.starts);
        endTV = (TextView) findViewById(R.id.ends);

        titleTV.setText(title);
        descriptionTV.setText(description);
        startTV.setText(dateToString(openDate, startTime));
        endTV.setText(dateToString(closeDate, endTime));

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        userID = preferenceConfig.getUserID();

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new Date();
                //Check if it is already started by the user
                new SearchPlayerTask().execute();
            }
        });
    }

    public void callGameActivity() {
        Intent intent = new Intent(TreasureHuntActivity.this, GameActivity.class);
        intent.putExtra("treasureHuntID", thID);
        startActivity(intent);
        finish();
    }

    public String dateToString(Date date, Date time){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String strTime = timeFormat.format(time);

        String dateTime = strDate + " " + strTime;

        return dateTime;
    }

    private class CreatePlayerClueTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            PlayerClue playerClue = new PlayerClue(thID, userID, firstClueID, STATUS);
            Login.appDatabase.playerClueDao().insertPlayerClue(playerClue);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callGameActivity();
        }
    }

    private class FindFirstClueIDTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            firstClueID = Login.appDatabase.clueDao().loadFistClueID(thID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new CreatePlayerClueTask().execute();
        }
    }

    private class InsertPlayerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Player player = new Player(currentDate,currentDate, null,null,
                    PLAYER_POINTS, CLUES_FOUND, STATUS, thID, userID);
            Login.appDatabase.playerDao().insertPlayer(player);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new FindFirstClueIDTask().execute();
        }
    }

    private class SearchPlayerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String status = "pending";
            //Check if the user is already playing this treasure hunt
            player = Login.appDatabase.playerDao().checkForPlayer(userID, thID, status);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //If nothing found in the database, user is okay to start the treasure hunt
            if (player == null) {
                //Add it to treasure hunts currently played by the user
                new InsertPlayerTask().execute();
            } else {
                callGameActivity();
            }
        }
    }
}
