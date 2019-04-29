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
import android.widget.Toast;

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

/**
 * TreasureHuntActivity class displays all the information for a treasure hunt and allows the user
 * to start playing by initializing the necessary fields in the database entities
 */
public class TreasureHuntActivity extends AppCompatActivity {
    private static final String TAG = "TreasureHuntActivity";
    private SharedPreferenceConfig preferenceConfig;

    //As the player is just starting the Treasure hunt the variables are set to 0;
    final int CLUES_FOUND = 0;
    final int PLAYER_POINTS = 0;
    final String STATUS = "pending";

    Date openDate, startTime, closeDate, endTime, currentDate;
    TextView titleTV, descriptionTV, startTV, endTV;
    int thID, userID, firstClueID, playerID;
    String title, description;
    Player player;
    Button play;

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

        //Display treasure hunt details
        titleTV.setText(title);
        descriptionTV.setText(description);
        startTV.setText(dateToString(openDate, startTime));
        endTV.setText(dateToString(closeDate, endTime));

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        userID = preferenceConfig.getUserID();

        //Play Button
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new Date();
                //current Date is before openDate
                if (currentDate.compareTo(openDate) < 0) {
                    Toast toast = Toast.makeText(TreasureHuntActivity.this,
                            "Treasure Hunt is not open yet!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //Check if it is already started by the user
                    new SearchPlayerTask().execute();
                }
            }
        });
    }

    /**
     * Firstly check if the user is already playing this TH by
     */
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
                playerID = player.id;
                callGameActivity();
            }
        }
    }

    /**
     * Create player for this treasure hunt, initialize values
     */
    private class InsertPlayerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Player player = new Player(currentDate,currentDate, null,null,
                    PLAYER_POINTS, CLUES_FOUND, STATUS, thID, userID);
            playerID = (int) Login.appDatabase.playerDao().insertPlayer(player);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Find the first clue for this treasure hunt currently started
            new FindFirstClueIDTask().execute();
        }
    }

    /**
     * Using the treasure hunt ID find the first clue
     */
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

    /**
     * Create PlayerClue object and insert it to the database
     * It saves the current state of the player for this treasure hunt
     */
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

    /**
     * Start the GameActivity
     */
    public void callGameActivity() {
        Intent intent = new Intent(TreasureHuntActivity.this, GameActivity.class);
        intent.putExtra("treasureHuntID", thID);
        intent.putExtra("playerID", playerID);
        startActivity(intent);
        finish();
    }

    /**
     * From date format to string
     * @param date
     * @param time
     * @return
     */
    public String dateToString(Date date, Date time){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String strTime = timeFormat.format(time);

        String dateTime = strDate + " " + strTime;

        return dateTime;
    }

}
