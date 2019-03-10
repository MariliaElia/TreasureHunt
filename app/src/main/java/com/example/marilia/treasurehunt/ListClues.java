package com.example.marilia.treasurehunt;

import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.Clue;

public class ListClues extends AppCompatActivity {
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

        Intent intent = getIntent();
        thID = intent.getIntExtra("THID", -1);

        new SearchDatabaseTask().execute();

        addClue = (Button) findViewById(R.id.addClue);
        addClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListClues.this, ClueMapsActivity.class);
                intent.putExtra("THID", (int) thID);
                startActivity(intent);
                finish();
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteFromDatabaseTask().execute();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Event cancelled!!",
                        Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(ListClues.this, MainActivity.class));
                finish();
            }
        });

        createTH = (Button) findViewById(R.id.createTH);
        createTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateDatabaseTask().execute();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Treasure Hunt created!",
                        Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(ListClues.this, MainActivity.class));
                finish();
            }
        });

    }

    private void displayClues(){
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rvAdapter = new RecyclerViewAdapter(clues);
        recyclerView.setAdapter(rvAdapter);
    }

    private class UpdateDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String status = "realease";
            Login.appDatabase.treasureHuntDao().updateTreasureHuntStatus(status, thID);
            return null;
        }
    }



    private class DeleteFromDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.treasureHuntDao().deleteTreasureHunt(thID);
            return null;
        }
    }
    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            clues = Login.appDatabase.clueDao().loadAllCluesforTH(thID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            displayClues();
        }
    }
}
