package com.example.marilia.treasurehunt;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marilia.treasurehunt.database.AppDatabase;
import com.example.marilia.treasurehunt.database.TreasureHunt;
import com.example.marilia.treasurehunt.database.User;

public class Login extends AppCompatActivity {
    private SharedPreferenceConfig preferenceConfig;
    private static final String TAG = "Login";

    public Button loginBn;
    public EditText usernameText, passwordText;
    public String username, password;
    public TextView registerLink;

    public static AppDatabase appDatabase;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize the database
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "AppDatabase.db")
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6,
                        AppDatabase.MIGRATION_6_7)
                .build();

        //create a shared preferences configuration
        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        //check login status of user
        if(preferenceConfig.getLoginStatus()){
            //if logged in do not display log in page
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        //Get user login data
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);

        //Log In
        loginBn = (Button) findViewById(R.id.loginBn);
        loginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();

                //check user input with database
                new SearchDatabaseTask().execute();

            }
        });

        //Register Link
        registerLink = (TextView) findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Register.class));
                finish();
            }
        });
    }

    /**
     * Search database to check if the data the user input in log in form
     * are correct or exist in the database
     */
    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //Find user with that username
            user = appDatabase.userDao().findUserByUsername(username);
            return null;
        }

        /**
         * This method is called after the database searc has finished
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (user != null) {
                if (password.equals(user.getPassword())) {
                    //Data correct take to Main Page
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //Save user logged in, in preferences
                    preferenceConfig.setLoginStatus(true);
                    preferenceConfig.storeUserData(user);
                    preferenceConfig.storeUserID(user.getUserId());
                    finish();
                } else {
                    //Password was incorrect
                    Toast toast = Toast.makeText(Login.this,
                            "Wrong Password.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                //User does not exist
                Toast toast = Toast.makeText(Login.this,
                        "User does not exist. Please register.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}
