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
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Register class, creates a user and adds him to the database, checking first that the user
 * does not already have an account
 */
public class Register extends AppCompatActivity {
    public Button registerBn;
    public EditText nameText, passwordText, emailText, usernameText;
    public String name, surname, username, password, email, dob;
    User checkUsername, checkEmail;
    public static final int INITIAL_POINTS = 100;

    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = (EditText) findViewById(R.id.name);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        usernameText = (EditText) findViewById(R.id.username);
        registerBn = (Button) findViewById(R.id.registerBn);

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

        //Register button clicked
        registerBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get text user has input in text boxes
                name = nameText.getText().toString();
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                email = emailText.getText().toString();

                //Check that password, username and email are not null
                if (password.isEmpty() || username.isEmpty() || email.isEmpty()){
                    //If null display message to user
                    Toast toast = Toast.makeText(Register.this,
                            "Username, password and email can't be left empty!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //before creating the user, check that the user doesn't already exist
                    new SearchDatabaseTask().execute();
                }
            }
        });

    }

    /**
     * Search database to check that the user does not already exist
     */
    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            checkUsername = Login.appDatabase.userDao().findUserByUsername(username);
            checkEmail = Login.appDatabase.userDao().findUserByEmail(email);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checkUsername == null && checkEmail == null) {
                new InsertIntoDatabaseTask().execute();
            } else {
                Toast toast = Toast.makeText(Register.this,
                        "Username or email already exist! Please use different input data!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    /**
     * Add user to database
     */
    private class InsertIntoDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            User registeredUserData = new User(name, surname,username,password, dob, email, INITIAL_POINTS);
            Login.appDatabase.userDao().insertUser(registeredUserData);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(Register.this, Login.class));
            finish();
        }
    }


}