package com.example.marilia.treasurehunt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class CreateTH extends AppCompatActivity {

    private static final String TAG = "CreateTH";

    private TextView displayOpenDate;
    private TextView displayClosingDate;
    private DatePickerDialog.OnDateSetListener openDateSetListener;
    private DatePickerDialog.OnDateSetListener closeDateSetListener;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;

    //get todays date
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_th);

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

    }
    public void showDateDialog(TextView dateDisplay, int year, int month, int day){
        activeDateDisplay = dateDisplay;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            Log.d(TAG, "date: " + day + "/" + month + "/" + year);
            String date = day + "/" + month + "/" + year;
            activeDateDisplay.setText(date);
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

}

