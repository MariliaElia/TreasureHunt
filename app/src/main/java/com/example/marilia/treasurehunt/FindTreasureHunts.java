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

public class FindTreasureHunts extends AppCompatActivity implements ItemClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TreasureHunt[] ths;
    private EditText filterTown;
    private String town;
    private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        filterTown = (EditText) findViewById(R.id.town);

        find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                town = filterTown.getText().toString();
                new FindTreasureHunts.GetTreasureHuntsTask().execute();
            }
        });
    }

    private void displayTreasureHunts(TreasureHunt[] ths){
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
            Toast toast = Toast.makeText(FindTreasureHunts.this,
                    "No results found for this city!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(this, TreasureHuntActivity.class);
        intent.putExtra("treasureHunt", ths[position]);
        startActivity(intent);
    }

    private class GetTreasureHuntsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ths = Login.appDatabase.treasureHuntDao().loadAllTreasureHuntsInTown(town);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayTreasureHunts(ths);
        }
    }

}

