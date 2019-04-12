package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.User;

import static com.example.marilia.treasurehunt.Login.appDatabase;

/**
 * MainActivity class displays the main page of the application, allowing the user to navigate
 * through the pages
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SharedPreferenceConfig preferenceConfig;
    private TextView usernameText, pointsText;
    private String username;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        //get user logged in from local storage
        User user = preferenceConfig.getUserLoggedIn();
        username = user.username;

        usernameText = (TextView) findViewById(R.id.username);
        pointsText = (TextView) findViewById(R.id.points);

        //find points of user
        new SearchDatabaseTask().execute();

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        //setAdapter sets a custom adapter as source for all items displayed in grid
        GridAdapter gridAdapter = new GridAdapter(this);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent;
                switch (position)
                {
                    case 0:
                        intent = new Intent(v.getContext(), StartedActivity.class);
                        break;
                    case 1:
                        intent = new Intent(v.getContext(), CreateTH.class);
                        break;
                    case 2:
                        intent = new Intent(v.getContext(), FindTreasureHunts.class);
                        break;
                    case 3:
                        intent = new Intent(v.getContext(), MyAccount.class);
                        break;
                    default:
                        intent = new Intent(v.getContext(), MainActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }

    /**
     * Update the data displayed when the page gets displayed again
     */
    @Override
    protected void onResume() {
        super.onResume();
        new SearchDatabaseTask().execute();
    }

    /**
     * Show points of user currently logged in
     */
    public void displayPoints() {
        usernameText.setText(username);
        pointsText.setText(Integer.toString(points));
    }

    /**
     * Get the points the user has
     */
    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //Find user with that username
            points = appDatabase.userDao().findUserPoints(username);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayPoints();
        }
    }

}
