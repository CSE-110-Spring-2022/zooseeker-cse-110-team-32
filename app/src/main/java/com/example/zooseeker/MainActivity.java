package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        //Can't run two activities at once -- later on this activity will be started elsewhere
        //Intent pathIntent = new Intent(this, ShortestPathActivity.class);
        //startActivity(pathIntent);
    }
}