package com.example.marilia.treasurehunt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.AppDatabase;
import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTH extends AppCompatActivity {

    private static final String TAG = "CreateTH";

    private TextView displayOpenDate;
    private TextView displayClosingDate;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;

    //get todays date
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    String currentDate = day + "-" + month + "-" + year;

    private static AppDatabase db;
    private static boolean databaseLoaded=false;

    public EditText titleText, descriptionText, countryText, cityText;
    public String title, description, country, town;
    public Date dateCreated, openDate, closingDate;
    public Button createTHBn, addClueBn;

    private SharedPreferenceConfig preferenceConfig;
    String username;
    User user;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_th);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        titleText = (EditText) findViewById(R.id.title);
        descriptionText = (EditText) findViewById(R.id.description);
        cityText = (EditText) findViewById(R.id.city);
        countryText = (EditText) findViewById(R.id.country);
        createTHBn = (Button) findViewById(R.id.createTH);
        dateCreated = stringToDate(currentDate);

        /* Select Open Date */
        //Get view element for open date
        displayOpenDate = (TextView) findViewById((R.id.openDate));
        //add click listener to text view
        displayOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialog(displayOpenDate, year, month, day);
            }
        });

        /* Closing Date */
        //Get view element for closing date
        displayClosingDate = (TextView) findViewById((R.id.closingDate));
        //add click listener to text view
        displayClosingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(displayClosingDate, year, month, day);
            }
        });

        /*addClueBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddClue.class);

            }
        });*/

        //Get current user
        user = preferenceConfig.getUserLoggedIn();
        username = user.username;

        createTHBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find id of current user
                new SearchDatabaseTask().execute();

                //Get information user has input
                title = titleText.getText().toString();
                description = descriptionText.getText().toString();
                town = cityText.getText().toString();
                country = countryText.getText().toString();

                new InsertIntoDatabaseTask().execute();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Treasure Hunt created.",
                        Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
    }

    /** Database **/

    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Insert into database this userID: "+ userID);
            TreasureHunt th = new TreasureHunt(title, description, dateCreated, openDate, closingDate, country, town, userID);
            Login.appDatabase.treasureHuntDao().insertTreasureHunt(th);
            Log.d(TAG,title + " " + description + " " + country + " " + town + " " + dateCreated + " " + openDate + " " + closingDate);

            return null;
        }
    }

    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "This is the current user username: " + username);
            userID = Login.appDatabase.userDao().getUserID(username);

            Log.d(TAG, "This is the current user ID: "+ userID );
            return null;
        }
    }

    /** Date Picker Dialog methods **/
    public void showDateDialog(TextView dateDisplay, int year, int month, int day){
        activeDateDisplay = dateDisplay;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            String date = day + "-" + month + "-" + year;
            Log.d(TAG, date);
            activeDateDisplay.setText(date);
            if (activeDateDisplay == displayOpenDate) {
                openDate = stringToDate(date);
            } else if (activeDateDisplay == displayClosingDate){
                closingDate = stringToDate(date);
            }

            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(CreateTH.this,android.R.style.Theme_Holo_Dialog_MinWidth, dateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                return dialog;
        }
        return null;
    }

    public Date stringToDate(String dateString){
        try {
            dateCreated = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateCreated;
    }

}

