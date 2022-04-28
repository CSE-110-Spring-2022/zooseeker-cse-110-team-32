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

        //Create graph
        Graph<String, DefaultWeightedEdge> g = null;
        try {
            //Reads in graph from JSON file
            g = createGraphFromJSON(this,"sample_graph.JSON");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Tests the generated graph against a shortest algorithm function from jgrpaht
        calculateShortestPath(g);
    }

    public void calculateShortestPath(Graph<String, DefaultWeightedEdge> g) {
        GraphPath<String, DefaultWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(g, "entranceExitGate1", "arcticFoxViewpoint");
        //I set up this textview to make sure the the alg is running correctly and see what it displays
        TextView textView = findViewById(R.id.path_result);
        textView.setText("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n"+shortest_path.toString());
        System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + /*shortest_path.toString()*/"this one!");
    }

    public static Graph<String, DefaultWeightedEdge> createGraphFromJSON(Context context, String path) throws IOException {
        Graph<String, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        JSONImporter<String, DefaultWeightedEdge> jsonImporter = new JSONImporter<>();
        jsonImporter.setVertexFactory(label -> label);

        InputStream input = context.getAssets().open(path);
        Reader reader = new InputStreamReader(input);
        jsonImporter.importGraph(g, reader);

        return g;
    }
}