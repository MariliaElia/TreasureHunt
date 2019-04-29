package com.example.marilia.treasurehunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.Clue;

/**
 * ListClues class displays the clues added by the user for a treasure hunt so far. It allows them to:
 * - add more clues
 * - publish the treasure hunt
 * - or cancel what they did so far
 */
public class ListClues extends AppCompatActivity {
    static final String STATUS_RELEASE = "realease";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public int thID = 0;
    public Clue[] clues;
    public Button addClue, createTH, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clues);

        //get the treasure hunt ID currently gettin created
        Intent intent = getIntent();
        thID = intent.getIntExtra("THID", -1);

        //Find the clues added for this treasure hunt so far
        new SearchDatabaseTask().execute();

        //Add Clue Button
        addClue = (Button) findViewById(R.id.addClue);
        addClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user add more clues for the treasure hunt currently created
                Intent intent = new Intent(ListClues.this, ClueMapsActivity.class);
                //pass the treasure Hunt ID to the ClueMapsActivity
                intent.putExtra("THID", (int) thID);
                startActivity(intent);
                finish();
            }
        });

        //Cancel Button
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete the treasure Hunt that was currently gettin created
                new DeleteFromDatabaseTask().execute();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Event cancelled!!",
                        Toast.LENGTH_SHORT);
                toast.show();

                //end this activity
                finish();
            }
        });

        //Create Treasure Hunt Button
        createTH = (Button) findViewById(R.id.createTH);
        createTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the status of the treasure hunt to release
                new UpdateDatabaseTask().execute();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Treasure Hunt created!",
                        Toast.LENGTH_SHORT);
                toast.show();

                //Take the user to the main activity
                finish();
            }
        });

    }

    /**
     * Display the clues added so far for the treasure hunt currently getting created
     */
    private void displayClues(){
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rvAdapter = new CluesAdapter(clues);
        recyclerView.setAdapter(rvAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    /**
     * When the user finishes adding clues, make the status of the treasure hunt to "release"
     */
    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.treasureHuntDao().updateTreasureHuntStatus(STATUS_RELEASE, thID);
            return null;
        }
    }

    /**
     * Delete the treasure hunt that was currently getting created, along with its clues
     */
    private class DeleteFromDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.treasureHuntDao().deleteTreasureHunt(thID);
            return null;
        }
    }

    /**
     * Find all the clues added to this treasure hunt so far and display them
     */
    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            clues = Login.appDatabase.clueDao().loadAllCluesforTH(thID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //display clues
            displayClues();
        }
    }

    /**
     * If the user tries to leave the activity show alert dialog to inform them that data will get
     * deleted if they leave
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leave page");
        builder.setMessage("Leaving the page will delete all data for the Treasure Hunt.");
        builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ListClues.super.onBackPressed();
                Toast.makeText(ListClues.this,"Creating Treasure Hunt cancelled.", Toast.LENGTH_SHORT).show();
                new DeleteFromDatabaseTask().execute();
                finish();
            }
        });
        builder.show();
    }
}
