package com.example.marilia.treasurehunt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.AppDatabase;
import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CreateTH class gets the information the user has input and creates a Treasure Hunt
 */
public class  CreateTH extends AppCompatActivity {
    private static final String TAG = "CreateTH";
    private static final String STATUS_DEVELOPMENT = "development";

    private TextView displayOpenDate;
    private TextView displayClosingDate;
    private TextView displayStartTime;
    private TextView displayEndTime;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    private TextView activeDateDisplay, activeTimeDisplay;

    //get todays date
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minutes = cal.get(Calendar.MINUTE);
    String currentDate = day + "-" + month + "-" + year;

    public EditText titleText, descriptionText, countryText, cityText;
    public String title, description, country, town;
    public Date dateCreated, openDate, closingDate, startTime, endTime;
    public Date time;
    public Button addCluesBn;

    private SharedPreferenceConfig preferenceConfig;
    int userID, thID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_th);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        titleText = (EditText) findViewById(R.id.title);
        descriptionText = (EditText) findViewById(R.id.description);
        cityText = (EditText) findViewById(R.id.city);
        countryText = (EditText) findViewById(R.id.country);
        dateCreated = stringToDate(currentDate);

        /* Select Open Date and Time */
        //Get view element for open date
        displayOpenDate = (TextView) findViewById((R.id.openDate));
        //add click listener to text view
        displayOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialog(displayOpenDate, year, month, day);
            }
        });

        displayStartTime = (TextView) findViewById(R.id.startTime);
        displayStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(displayStartTime, hour, minutes);
            }
        });

        /* Closing Date and Time*/
        //Get view element for closing date
        displayClosingDate = (TextView) findViewById((R.id.closingDate));
        //add click listener to text view
        displayClosingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(displayClosingDate, year, month, day);
            }
        });

        displayEndTime = (TextView) findViewById(R.id.endTime);
        displayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(displayEndTime, hour, minutes);
            }
        });

        //Add Clues clicked
        addCluesBn = (Button) findViewById(R.id.addClues);
           addCluesBn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Find id of current user
                    userID = preferenceConfig.getUserID();

                    //Get information user has input
                    title = titleText.getText().toString();
                    description = descriptionText.getText().toString();
                    town = cityText.getText().toString().toLowerCase();
                    country = countryText.getText().toString();

                    if (title.isEmpty() || description.isEmpty() || town.isEmpty() || startTime == null || openDate == null
                    ||endTime == null || closingDate == null) {
                        //If null display message to user
                        Toast toast = Toast.makeText(CreateTH.this,
                                "Title,description, dates and city can't be left empty!",
                                Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        //Input into database
                        new InsertIntoDatabaseTask().execute();
                    }
                }
           });
    }

    /**
     * Starts the ClueMaps activity, passing the id of the treasure hunt just created
     */
    private void callClueMapsActivity(){
        Intent intent = new Intent(CreateTH.this, ClueMapsActivity.class);
        intent.putExtra("THID", thID);
        startActivity(intent);

        //finish current activity
        finish();
    }

    /**
     * Create a treasure hunt object and insert it the database
     */
    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            TreasureHunt th = new TreasureHunt(title, description, dateCreated, openDate, startTime, closingDate, startTime, country, town, userID, STATUS_DEVELOPMENT);
            thID = (int) Login.appDatabase.treasureHuntDao().insertTreasureHunt(th);

            return null;
        }

        /**
         * After the above task finishes call Clue-Maps activity
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callClueMapsActivity();
        }
    }

    /** Time Picker Dialog methods **/
    public void showTimeDialog(TextView timeDisplay, int hour, int minutes){
        activeTimeDisplay = timeDisplay;
        showDialog(TIME_DIALOG_ID);
    }

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minutes) {
            String time;
            if (String.valueOf(minutes).length() == 1 ){
                time = hour + ":0" + minutes;
            } else {
                time = hour + ":" + minutes;
            }
            activeTimeDisplay.setText(time);
            if (activeTimeDisplay == displayStartTime) {
                startTime = stringToTime(time);
            } else if (activeTimeDisplay == displayEndTime){
                endTime = stringToTime(time);
            }

            unregisterTimeDisplay();
        }
    };

    private void unregisterTimeDisplay() {
        activeTimeDisplay = null;
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
                DatePickerDialog dateDialog = new DatePickerDialog(CreateTH.this,android.R.style.Theme_Holo_Dialog_MinWidth, dateListener, year, month, day);
                dateDialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                return dateDialog;
            case TIME_DIALOG_ID:
                TimePickerDialog timeDialog = new TimePickerDialog(CreateTH.this,2 ,timeListener, hour, minutes, false);
                timeDialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                return timeDialog;
        }
        return null;
    }

    /**
     * takes a string in a date format and converts it to date
     * @param dateString
     * @return date
     */
    public Date stringToDate(String dateString){
        try {
            dateCreated = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateCreated;
    }

    /**
     * takes string in a time-date format and converts it to date
     * @param timeString
     * @return
     */
    public Date stringToTime(String timeString){
        try {
            time = new SimpleDateFormat("HH:mm").parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }


}

