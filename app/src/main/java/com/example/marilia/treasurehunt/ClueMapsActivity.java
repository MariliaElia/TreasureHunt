package com.example.marilia.treasurehunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

public class ClueMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "ClueMapsActivity";
    private GoogleMap mMap;
    private Marker marker;
    public EditText clueText, descriptionText;
    public String clue, description;
    private Button addClue;
    private double longitude, latitude;
    public int thID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        thID = intent.getIntExtra("THID", -1);

        clueText = (EditText) findViewById(R.id.clue);
        descriptionText = (EditText) findViewById(R.id.description);
        addClue = (Button) findViewById(R.id.addClue);

        addClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clue = clueText.getText().toString();
                description = descriptionText.getText().toString();

                new InsertIntoDatabaseTask().execute();

                Intent intent = new Intent(ClueMapsActivity.this, ListClues.class);
                intent.putExtra("THID", (int) thID);
                startActivity(intent);
                finish();
            }

        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
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

    /** Database **/
    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Clue cl = new Clue(clue,description,latitude,longitude,thID);
            Login.appDatabase.clueDao().insertClue(cl);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leave page");
        builder.setMessage("Leaving the page will delete all data for the Treasure Hunt.");
        builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stayInClueMapsActivity();
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

    private void stayInClueMapsActivity(){
        Intent intent = new Intent(ClueMapsActivity.this, ClueMapsActivity.class);
        intent.putExtra("THID", (int) thID);
        startActivity(intent);
        finish();
    }

}
