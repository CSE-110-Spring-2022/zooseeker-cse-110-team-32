package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class ShortestPathActivity extends AppCompatActivity {

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

        Button next = findViewById(R.id.next_btn);
        if(!plan.endReached()){
            displayTextDirections(plan);
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                displayTextDirections(plan);
            });
        }
    }

    public void displayTextDirections(PlanList plan){
        TextView textView = findViewById(R.id.path_result);
        Location currLoc = plan.getCurrentLocation();
        Location nextLoc = plan.getNextLocation();
        System.out.println(currLoc.getId());
        String directions = plan.getDirectionsToNextLocation();
        directions = "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + "\n\n" + directions;
        textView.setText(directions);

        Button next = findViewById(R.id.next_btn);
        if(plan.endReached()){
            next.setClickable(false);
            next.setVisibility(View.GONE);
        }
        else{
            plan.advanceLocation();
        }
    }

}