package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*This class loads the pages that display the directions from your current location to the next
exhibit with a next button (back button to be added) that is clicked when the user wants to go to
the next exhibit.
 */
public class ShortestPathActivity extends AppCompatActivity {

    /*Loads directions page and initializes necessary classes and variables for each component
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);

        Button back = findViewById(R.id.back_btn);
        if(!back.isClickable()){
            back.setVisibility(View.GONE);
        }
        PlanList plan = SearchActivity.getPlan();
        NavigatePlannedList navList = new NavigatePlannedList(plan);
        Button next = findViewById(R.id.next_btn);
        if(!navList.endReached()){
            displayTextDirections(navList);
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                displayTextDirections(navList);
            });
        }
    }

    /*Displays the directions from user's current location to the next closes exhibit in their list
    @param plan = user's planned exhibits
     */
    public void displayTextDirections(NavigatePlannedList navList){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        String directions = navList.getDirectionsToNextLocation();
        directions = "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + "\n\n" + directions;

        textView.setText(directions);

        Button next = findViewById(R.id.next_btn);
        Button finish = findViewById(R.id.finish_btn);
        if(navList.endReached()){
            next.setClickable(false);
            next.setVisibility(View.GONE);
            nextNextView.setVisibility(View.GONE);

            Intent intent = new Intent(this, SearchActivity.class);
            finish.setClickable(true);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(view -> {
                startActivity(intent);
            });
        }
        else{
            nextNextView.setText(navList.getNextNextLocation().getName() +
                    ", " + navList.getPathToNextNextLocation().getWeight());
        }
        navList.advanceLocation();

        Button back = findViewById(R.id.back_btn);
        if (!navList.atStart()){
            back.setVisibility(View.VISIBLE);
            back.setClickable(true);
            back.setOnClickListener(view -> {
                displayPrevTextDirections(navList);
            });
        }
    }

    public void displayPrevTextDirections(NavigatePlannedList navList){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location prevLoc = navList.getPrevLocation();
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        String directions = navList.getDirectionsToPreviousLocation();
        directions = "*Going Backwards\n\n" + "From: " + currLoc.getName() + "\nTo: "
                + prevLoc.getName() + "\n\n" + directions;
        textView.setText(directions);

        Button back = findViewById(R.id.back_btn);
        if (navList.atFirst()){
            back.setClickable(false);
            back.setVisibility(View.GONE);
        }

        navList.previousLocation();

        Button next = findViewById(R.id.next_btn);
        Button finish = findViewById(R.id.finish_btn);

        finish.setClickable(false);
        finish.setVisibility(View.GONE);

        next.setVisibility(View.VISIBLE);
        next.setClickable(true);
        next.setOnClickListener(view -> {
            displayTextDirections(navList);
        });

        if (!navList.endReached()){
            nextNextView.setVisibility(View.VISIBLE);
            nextNextView.setText(navList.getNextNextLocation().getName() +
                    ", " + navList.getPathToNextNextLocation().getWeight());
        }

    }

}