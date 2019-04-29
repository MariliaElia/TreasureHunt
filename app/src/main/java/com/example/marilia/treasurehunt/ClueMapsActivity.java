package com.example.marilia.treasurehunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.Clue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * ClueMapsActivity class is the activity that displays the map and allows the user to associate a
 * clue with a location clicked on the map
 */
public class ClueMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "ClueMapsActivity";

    //Google Maps
    private GoogleMap mMap;
    private Marker marker;

    public EditText clueText, descriptionText;
    private double longitude, latitude;
    public String clue, description;
    private Button addClue;
    public int thID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get the treasure hunt ID just created in previous activity
        Intent intent = getIntent();
        thID = intent.getIntExtra("THID", -1);

        //EditTexts in activity
        clueText = (EditText) findViewById(R.id.clue);
        descriptionText = (EditText) findViewById(R.id.description);
        addClue = (Button) findViewById(R.id.addClue);

        //Add Clue clicked
        addClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clue = clueText.getText().toString();
                description = descriptionText.getText().toString();

                if (clue.isEmpty() || latitude == 0 || longitude == 0) {
                    //If null display message to user
                    Toast toast = Toast.makeText(ClueMapsActivity.this,
                            "You have to fill in the fields and select a position on the map to continue!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //Add the data from the editTexts in the database
                    new InsertIntoDatabaseTask().execute();

                    //Start the List Clues activity and pass the treasure hunt ID
                    Intent intent = new Intent(ClueMapsActivity.this, ListClues.class);
                    intent.putExtra("THID", (int) thID);
                    startActivity(intent);

                    //finish this activity
                    finish();
                }
            }

        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get the position where the user clicks on the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //User can only add one marker on the map
                if (marker != null){
                    marker.setPosition(latLng);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                } else{
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
            }
        });
    }

    /**
     * Creates a Clue object and adds it to the database
     */
    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Clue cl = new Clue(clue,description,latitude,longitude,thID);
            Login.appDatabase.clueDao().insertClue(cl);
            return null;
        }
    }

    /**
     * Deletes the treasure hunt created by the user
     */
    private class DeleteFromDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.treasureHuntDao().deleteTreasureHunt(thID);
            return null;
        }
    }

    /**
     * If the user pressed the back button, display an alert giving the user the choice to stay
     * or leave, letting them know that the data inserted for the treasure hunt so far,
     * will get deleted if they leave
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
                ClueMapsActivity.super.onBackPressed();
                Toast.makeText(ClueMapsActivity.this,"Creating Treasure Hunt cancelled.", Toast.LENGTH_SHORT).show();
                new DeleteFromDatabaseTask().execute();
                finish();
            }
        });
        builder.show();
    }

}