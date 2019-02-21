package com.example.marilia.treasurehunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        //setAdapter sets a custom adapter as source for all items displayed in grid
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);

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
                    default:
                        intent = new Intent(v.getContext(), CreateTH.class);
                }
                startActivity(intent);
            }
        });
    }
}
