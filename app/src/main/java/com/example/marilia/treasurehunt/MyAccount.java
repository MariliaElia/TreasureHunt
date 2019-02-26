package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.User;

public class MyAccount  extends AppCompatActivity {
    private static final String TAG = "MyAccount";
    Button logoutBn;
    TextView accountMessage;
    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        logoutBn = (Button) findViewById(R.id.logoutBn);
        accountMessage = (TextView) findViewById(R.id.accountMessage);

        User user = preferenceConfig.getUserLoggedIn();

        Log.d(TAG, "This is the current user: " + user.username);
        accountMessage.setText("Hello " + user.username);
        logoutBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceConfig.clearUserData();
                preferenceConfig.setLoginStatus(false);
                startActivity(new Intent(v.getContext(), Login.class));
                finish();
            }
        });
    }

}
