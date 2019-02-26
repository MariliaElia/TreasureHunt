package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.marilia.treasurehunt.database.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String uniqueID = null;

    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        //setAdapter sets a custom adapter as source for all items displayed in grid
        GridAdapter gridAdapter = new GridAdapter(this);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent;
                switch (position)
                {
                    case 0:
                        intent = new Intent(v.getContext(), CreateTH.class);
                        break;
                    case 1:
                        intent = new Intent(v.getContext(), CreateTH.class);
                        break;
                    case 2:
                        intent = new Intent(v.getContext(), CreateTH.class);
                        break;
                    case 3:
                        intent = new Intent(v.getContext(), MyAccount.class);
                        break;
                    default:
                        intent = new Intent(v.getContext(), MyAccount.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
