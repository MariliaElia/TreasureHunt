package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.Player;
import com.example.marilia.treasurehunt.database.TreasureHunt;

/**
 * Finished Activity class displays to the user the scores he gained while playing the Treasure Hunt
 */
public class FinishedActivity extends AppCompatActivity {
    TextView pointsGainedText, cluesFoundText;
    int playerID;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        //Get player ID from previous activity
        Intent intent = getIntent();
        playerID = intent.getIntExtra("playerID", -1);

        //Get scores from database
        new GetDataTask().execute();

        pointsGainedText = (TextView) findViewById(R.id.pointsGained);
        cluesFoundText = (TextView) findViewById(R.id.cluesFound);

    }

    /**
     * Display scores in Text Views
     */
    public void displayData(){
        pointsGainedText.setText(Integer.toString(player.total_points));
        cluesFoundText.setText(Integer.toString(player.successful_clues));
    }

    /**
     * Using the playerID find the scores the player scored while playing the treasure hunt
     */
    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            player = Login.appDatabase.playerDao().getPlayerData(playerID);
            return null;
        }

        /**
         * After the above task finishes display Scores
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayData();
        }
    }
}
