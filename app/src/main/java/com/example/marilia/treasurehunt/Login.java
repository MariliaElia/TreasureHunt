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

        /*preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        if(preferenceConfig.getLoginStatus()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "AppDatabase.db")
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6,
                        AppDatabase.MIGRATION_6_7)
                .build();

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        if(preferenceConfig.getLoginStatus()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        loginBn = (Button) findViewById(R.id.loginBn);

        loginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();

                new SearchDatabaseTask().execute();

            }
        });

        registerLink = (TextView) findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Register.class));
                finish();
            }
        });
    }

    private class SearchDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            user = appDatabase.userDao().findUserByUsername(username);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (user != null) {
                if (password.equals(user.getPassword())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    preferenceConfig.setLoginStatus(true);
                    preferenceConfig.storeUserData(user);
                    finish();
                } else {
                    Toast toast = Toast.makeText(Login.this,
                            "Wrong Password.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(Login.this,
                        "User does not exist. Please register.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}
