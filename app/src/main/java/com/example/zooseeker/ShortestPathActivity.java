package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ShortestPathActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);

        ZooMap zooMap = new ZooMap(this, "sample_graph.JSON");
        Directions directions = zooMap.calculateShortestPath("entranceExitGate1", "arcticFoxViewPoint");
        System.out.println(directions);
        TextView textView = findViewById(R.id.path_result);
        textView.setText("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n"+directions.getLandmarks().toString());
        System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + /*directions.getLandmarks().toString()*/"this one!");
    }


}