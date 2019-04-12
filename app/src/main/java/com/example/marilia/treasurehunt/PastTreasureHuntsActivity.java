package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.TreasureHunt;

/**
 * PastTreasureHuntsActivity class gets all the treasure hunts completed by the user in the past and
 * displays them
 */
public class PastTreasureHuntsActivity  extends AppCompatActivity implements ItemClickListener {
    private static final String STATUS_COMPLETED = "completed";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferenceConfig preferenceConfig;

    private TreasureHunt[] ths;
    private int[] thIds;
    private int userID;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ths);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        //get user logged in from local storage
        userID = preferenceConfig.getUserID();

        message = (TextView) findViewById(R.id.message);

        //Find treasure hunts ids played by the user from database
        new GetTreasureHuntIDsTask().execute();
    }

    /**
     * Display the treasure hunts returned from the database
     */
    private void displayTreasureHunts(){
        recyclerView = (RecyclerView) findViewById(R.id.th_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //passing the onItemClickListener interface to the constructor of the TreasureHuntsAdaptor
        rvAdapter = new TreasureHuntsAdapter(ths, this);
        recyclerView.setAdapter(rvAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    /**
     * If one of the treasure hunts is clicked take user to Treasure Hunt activity
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(this, TreasureHuntActivity.class);
        intent.putExtra("treasureHunt", ths[position]);
        startActivity(intent);
    }

    /**
     * Using the userID load all treasure hunt ids from the database completed by that user
     */
    private class GetTreasureHuntIDsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            thIds = Login.appDatabase.playerDao().getTreasureHuntIDs(userID, STATUS_COMPLETED);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (thIds.length != 0) {
                message.setText("Past games: ");
                new GetTreasureHuntsTask().execute();
            } else {
                message.setText("You have not played any treasre hunts yet. ");
            }
        }
    }

    /**
     * Using the treasure hunt IDs load the treasure hunts from the database and display them
     */
    private class GetTreasureHuntsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ths = Login.appDatabase.treasureHuntDao().loadAllTreasureHuntsWith(thIds);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                displayTreasureHunts();
        }
    }
}
