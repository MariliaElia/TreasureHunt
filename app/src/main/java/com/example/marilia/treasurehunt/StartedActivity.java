package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.TreasureHunt;

/**
 * Started Activity takes all the treasure hunts that have been started
 * by the user and displays them
 */
public class StartedActivity extends AppCompatActivity implements ItemClickListener {
    private final static String STATUS_PENDING = "pending";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TreasureHunt[] ths;
    private int[] thsID;
    private int userID;
    private SharedPreferenceConfig preferenceConfig;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ths);

        message = (TextView) findViewById(R.id.message);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        userID = preferenceConfig.getUserID();

        //Search the database
        new GetTreasureHuntIDsTask().execute();

    }

    /**
     * Display treasure hunts to the user
     * @param ths
     */
    private void displayTreasureHunts(TreasureHunt[] ths){
        //check that the user has started playing a treasure hunt
        if (ths.length != 0 ) {
            message.setText("Currenlty Playing: ");
            recyclerView = (RecyclerView) findViewById(R.id.th_recycler_view);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //passing the onItemClickListener interface to the constructor of the TreasureHuntsAdaptor
            rvAdapter = new TreasureHuntsAdapter(ths,this);
            recyclerView.setAdapter(rvAdapter);
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
            recyclerView.addItemDecoration(mDividerItemDecoration);
        } else {
            message.setText("Search for Treasure Hunts and start playing!");
        }
    }

    /**
     * If the user clicks on the treasure hunt start TreasureHunt activity
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
     * Find all treasure hunt ids from the database where there status == pending for current user
     */
    private class GetTreasureHuntIDsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            thsID = Login.appDatabase.playerDao().getTreasureHuntIDs(userID, STATUS_PENDING);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetTreasureHuntsTask().execute();
        }
    }

    /**
     * Find the treasure hunts using the ids returned from previous function and display them
     */
    private class GetTreasureHuntsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ths = Login.appDatabase.treasureHuntDao().loadAllTreasureHuntsWith(thsID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayTreasureHunts(ths);
        }
    }
}
