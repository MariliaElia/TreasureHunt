package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MyAccount  extends AppCompatActivity {
    Button logoutBn;
    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        logoutBn = (Button) findViewById(R.id.logoutBn);

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
