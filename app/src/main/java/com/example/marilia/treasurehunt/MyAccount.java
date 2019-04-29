package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * MyAccount class resets the local storage when the user logs out
 * and allows the user to see the treasure hunts he had had created or had participated in
 */
public class MyAccount  extends AppCompatActivity {
    Button logoutBn, createdByMeBn, participatedInBn;
    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        //Created By Me Button takes the user to the activity displaying the treasure hunts he had created
        createdByMeBn = (Button) findViewById(R.id.createdByMe);
        createdByMeBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccount.this, MyTreasureHuntsActivity.class));
            }
        });

        //Participated In Button takes the user to the actvity displaying the treasure hunts he completed
        participatedInBn = (Button) findViewById(R.id.paricipatedIn);
        participatedInBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccount.this, PastTreasureHuntsActivity.class));
            }
        });

        logoutBn = (Button) findViewById(R.id.logoutBn);
        logoutBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear local data
                preferenceConfig.clearUserData();
                preferenceConfig.setLoginStatus(false);
                Intent intent = new Intent(getApplicationContext(), Login.class);
                //end all activities
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

}
