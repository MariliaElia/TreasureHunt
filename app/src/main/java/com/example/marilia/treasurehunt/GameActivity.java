package com.example.marilia.treasurehunt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.Clue;
import com.example.marilia.treasurehunt.database.PlayerClue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class GameActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "GameActivity";
    private static final int REQUEST_CODE = 1000;
    public static final float DEFAULT_ZOOM = 15f;
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_SKIPPED = "skipped";
    public static final int CLUE_REWARD = 1;
    public static final int POINTS_REWARD = 10;
    public static final int POINTS_SKIP = -10;

    private GoogleMap mMap;
    Button btn_here, skip;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    LocationCallback locationCallback;
    private Boolean locationPermissionGranted = false;

    private SharedPreferenceConfig preferenceConfig;

    private Location currentLocation;
    private int thID, userID, lastClueID;
    private Clue cl;
    private double lonClue, latClue;
    private double currentLon, currentLat;
    private Date currentDate;
    private TextView clue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        thID = intent.getIntExtra("treasureHuntID", -1);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        userID = preferenceConfig.getUserID();

        new FindClueIDTask().execute();

        //Check Permissions
        getLocationPermission();

        btn_here = (Button) findViewById(R.id.here);
        btn_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get lon lat for this clue
                //Check if user is in that location
                //If correct
                //   make playerClue status from pending to completed
                //   add one to noOfSuccessfulClues
                //   add points to Player
                //Else
                //   user is in the wrong location, display message

                if (locationPermissionGranted) {
                    currentLat = currentLocation.getLatitude();
                    currentLon = currentLocation.getLongitude();

                    boolean checkLonLat = checkLonLat(currentLat, currentLon, lonClue,latClue);

                    if (checkLonLat) {
                        //updatePlayerClue status
                        new UpdatePlayerTHValuesTask().execute();
                        new UpdatePlayerClueStatusTask().execute();

                    } else {
                        Toast.makeText(getApplicationContext(),"Oops! Looks like this isn't the correct location!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Skip Clue");
                builder.setMessage("Skipping this clue you will lose 10 points!");
                builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new UpdatesSkipStatusTask().execute();
                    }
                });
                builder.show();
            }
        });

        //For button skipped
        //make playerClue status skipped
        //decrease points of user from initial table
    }

    public void callGameActivity() {
        Intent intent = new Intent(GameActivity.this, GameActivity.class);
        intent.putExtra("treasureHuntID", thID);
        startActivity(intent);
        finish();
    }

    private boolean checkLonLat(double currentLat, double currentLon, double lonClue, double latClue) {
        double meters = 17;

        // number of km per degree = 111.32 in google maps
        // 1km in degree
        double kmInDegree = 1 / 111.32;
        // 1m in degree
        double mInDegree = kmInDegree /1000;

        double offset = meters * mInDegree;

        double newPosLat = latClue + offset;
        double newNegLat = latClue - offset;

        //degrees to radians
        double radians = Math.PI / 180;

        double newPosLon = lonClue + (offset / Math.cos(latClue*radians));
        double newNegLon = lonClue + (-offset / Math.cos(latClue*radians));

        if ((currentLat<newPosLat && currentLat>newNegLat) && (currentLon>newNegLon && currentLon<newPosLon)){
            return true;
        } else {
            return false;
        }
    }

    /** DATABASE CALLS **/

    private class UpdatesSkipStatusTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //Decrease points from the user
            Login.appDatabase.userDao().updateUserPoints(POINTS_SKIP, userID);
            //Make status skipped
            Login.appDatabase.playerClueDao().updatePlayerClueStatus(STATUS_SKIPPED, thID, userID, lastClueID);
            //check if remaining clues for this treasure hunt
            cl = Login.appDatabase.clueDao().loadClue(thID, lastClueID + 1);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (cl != null) {
                new CreatePlayerClueTask().execute();
            } else {
                //Player has finished the treasure hunt
                new UpdateTreasureHuntStatusTask().execute();
            }
        }
    }

    private class UpdatePlayerClueStatusTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.playerClueDao().updatePlayerClueStatus(STATUS_COMPLETED, thID, userID, lastClueID);
            cl = Login.appDatabase.clueDao().loadClue(thID, lastClueID + 1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (cl != null) {
                new CreatePlayerClueTask().execute();
            } else {
                //Player has finished the treasure hunt
                new UpdateTreasureHuntStatusTask().execute();
            }
        }
    }

    private class UpdateTreasureHuntStatusTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            currentDate = new Date();
            Login.appDatabase.playerDao().updatePlayerTHStatus(thID, userID,STATUS_PENDING, STATUS_COMPLETED, currentDate, currentDate);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(GameActivity.this, FinishedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class CreatePlayerClueTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            PlayerClue playerClue = new PlayerClue(thID, userID, lastClueID + 1, STATUS_PENDING);
            Login.appDatabase.playerClueDao().insertPlayerClue(playerClue);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callGameActivity();
        }
    }

    private class UpdatePlayerTHValuesTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Login.appDatabase.userDao().updateUserPoints(POINTS_REWARD, userID);
            Login.appDatabase.playerDao().updatePlayerTHValues(CLUE_REWARD, POINTS_REWARD, thID, userID, STATUS_PENDING);
            return null;
        }
    }

    /**
     * FindCluIDTask finds the last clue id the player has stopped playing at
     */
    private class FindClueIDTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            lastClueID = Login.appDatabase.playerClueDao().getLastClueID(thID,userID,"pending");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new FindClueTask().execute();
        }
    }

    /**
     * Finds the clue from the TreasureHunt table by using the clueID from last AsyncTask
     */
    private class FindClueTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cl = Login.appDatabase.clueDao().loadClue(thID, lastClueID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayClue();
        }
    }

    /**
     * Displays the clue found in TreasureHunt table
     */
    private void displayClue() {
        clue = findViewById(R.id.clue);
        clue.setText(cl.clue);
        lonClue = cl.longitude;
        latClue = cl.latitude;

    }

    /**
     * Initializes map
     */
    public void initMap() {
        Log.d(TAG, "initMap: initialize map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * when map is ready, sets the location
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * When the activity is no longer visible stops location updates
     */
    @Override
    protected void onStop() {
        super.onStop();

        //if the location permissions were granted
        if (locationPermissionGranted) {
            //Stop Location Updates
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            Log.d(TAG, "Stop Location Updates");
        }

    }

    /**
     * Asks for location permissions
     */
    public void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            Log.d(TAG, "Permissions granted");
            initMap();
            //permission granted
            buildLocationRequest();
            buildLocationCallback();
            locationPermissionGranted = true;

            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            //Start Location Updates
            startLocationUpdates();

        }
    }

    /**
     * Builds location request
     */
    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    /**
     * Requests location updates
     */
    private void startLocationUpdates() {
        Log.d(TAG, "Location updates requested");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    /**
     * Build location callback
     */
    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations()){
                    if (locationPermissionGranted && mMap!=null) {
                        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM);
                        currentLocation = location;
                    }
                }
            }
        };
    }

    /**
     * Moves camera of the map to the user location
     * @param latLng
     * @param zoom
     */
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case REQUEST_CODE:
                if(grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initMap();
                        Log.d(TAG, "onRequestPermissionResult: persmission succeeded");
                        locationPermissionGranted = true;
                        buildLocationRequest();
                        buildLocationCallback();

                        //Create FusedProviderClient
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                        //Start Location Updates
                        startLocationUpdates();
                    }
                }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Turn on location permissions from Settings to continue!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Turn on location permissions from Settings to continue!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        }

    }
}