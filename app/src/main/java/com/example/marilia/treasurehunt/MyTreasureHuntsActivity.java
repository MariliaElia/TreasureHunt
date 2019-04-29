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
import com.example.marilia.treasurehunt.database.User;

/**
 * MyTreasureHunts Activity gets all the treasure hunts created by the user logged in and displays
 * them
 */
public class MyTreasureHuntsActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferenceConfig preferenceConfig;

    private TreasureHunt[] ths;
    private int userID;
    private String username;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ths);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        //get user logged in from local storage
        User user = preferenceConfig.getUserLoggedIn();
        username = user.username;

        message = (TextView) findViewById(R.id.message);

        //Find treasure hunts from database
        new GetTreasureHuntsTask().execute();
    }

    /**
     * Display the treasure hunts returned from the database
     */
    private void displayTreasureHunts(){
        recyclerView = (RecyclerView) findViewById(R.id.th_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //passing the onItemClickListener interface to the constructor of the TreasureHuntsAdaptor
        rvAdapter = new TreasureHuntsAdapter(ths);
        recyclerView.setAdapter(rvAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    /**
     * If on of the treasure hunts is clicked take user to Treasure Hunt activity
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
     * Using the userID load all treasure hunts from the database created by that user
     */
    private class GetTreasureHuntsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            userID = Login.appDatabase.userDao().getUserID(username);
            ths = Login.appDatabase.treasureHuntDao().loadAllTreasureHuntsCreatedBy(userID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (ths.length != 0) {
                message.setText("My Treasure Hunts: ");
                displayTreasureHunts();
            } else {
                message.setText("You have not created any treasure hunts yet.");
            }
        }
    }

}
