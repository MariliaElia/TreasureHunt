package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Find Treasure Hunts searches the database according to user input(city name)
 * and returns data displaying it to the user
 */
public class FindTreasureHunts extends AppCompatActivity implements ItemClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TreasureHunt[] ths;
    private EditText filterTown;
    private String town;
    private Button find;
    Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        filterTown = (EditText) findViewById(R.id.town);

        find = (Button) findViewById(R.id.find);
        //Find clicked
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                town = filterTown.getText().toString().toLowerCase().replaceAll("\\s+","");
                //search the database
                new FindTreasureHunts.GetTreasureHuntsTask().execute();
            }
        });
    }

    /**
     * Display the Treasure Hunts on a list
     * @param ths
     */
    private void displayTreasureHunts(TreasureHunt[] ths){
        //Check that the database returned back data
        if (ths.length != 0 ) {
            recyclerView = (RecyclerView) findViewById(R.id.th_recycler_view);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //passing the onItemClickListener interface to the constructor of the TreasureHuntsAdaptor
            rvAdapter = new TreasureHuntsAdapter(ths, this);
            recyclerView.setAdapter(rvAdapter);
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
            recyclerView.addItemDecoration(mDividerItemDecoration);
        } else {
            //The array is empty, it means no treasure hunts in that exist are currently available
            Toast toast = Toast.makeText(FindTreasureHunts.this,
                    "No results found for this city!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * When one of the treasure hunts gets clicked from the list, call Treasure Hunt activity
     * and pass the treasure hunt clicked information
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
     * Loads all treasure hunts in city user searched for
     */
    private class GetTreasureHuntsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ths = Login.appDatabase.treasureHuntDao().loadAllTreasureHuntsInTown(town, currentDate);
            return null;
        }

        /**
         * After the above task finishes, display results
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayTreasureHunts(ths);
        }
    }

}

