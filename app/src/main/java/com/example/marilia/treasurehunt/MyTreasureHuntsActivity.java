package com.example.marilia.treasurehunt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;

public class MyTreasureHuntsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferenceConfig preferenceConfig;

    private TreasureHunt[] ths;
    private int userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_by_me);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        User user = preferenceConfig.getUserLoggedIn();
        username = user.username;

        new GetTreasureHuntsTask().execute();
    }

    private void displayTreasureHunts(){
        recyclerView = (RecyclerView) findViewById(R.id.th_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rvAdapter = new TreasureHuntsAdapter(ths);
        recyclerView.setAdapter(rvAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

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

            displayTreasureHunts();
        }
    }

}
