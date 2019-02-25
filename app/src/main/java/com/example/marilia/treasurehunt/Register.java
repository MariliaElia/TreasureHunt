package com.example.marilia.treasurehunt;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.example.marilia.treasurehunt.database.User;

import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {
    public Button registerBn;
    public EditText nameText, surnameText, passwordText, emailText, usernameText;
    public String name, surname, username, password, email, dob;
    public static final int initialPoints = 100;
    private static final String TAG = "Register";

    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = (EditText) findViewById(R.id.name);
        surnameText = (EditText) findViewById(R.id.surname);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        usernameText = (EditText) findViewById(R.id.username);
        registerBn = (Button) findViewById(R.id.registerBn);


        /* Select Open Date */
        //Get view element for open date
        final TextView displayDob = (TextView) findViewById((R.id.dob));
        //add click listener to text view
        displayDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get todays date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Register.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                dialog.show();
            }
        });

         dateListener = new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                 month = month + 1;
                 dob = day + "/" + month + "/" + year;
                 displayDob.setText(dob);
             }
         };

        registerBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                surname = surnameText.getText().toString();
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                email = emailText.getText().toString();

                new InsertIntoDatabaseTask().execute();

                startActivity(new Intent(v.getContext(), Login.class));
                finish();
            }
        });

    }

    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            User registeredUserData = new User(name, surname,username,password, dob, email, initialPoints);
            Login.appDatabase.userDao().insertUser(registeredUserData);

            Log.d(TAG, registeredUserData.username.toString());

            return null;
        }
    }


}