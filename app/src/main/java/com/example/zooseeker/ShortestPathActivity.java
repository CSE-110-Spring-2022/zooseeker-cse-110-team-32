package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;

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
import java.util.List;
import java.util.Map;

public class ShortestPathActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);


        // I don't know if this works but at least it compiles
        Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        ZooMap zooMap = new ZooMap(graph, vertices, edges);
        //List<String> directions = zooMap.getTextDirections("entrance_exit_gate", "arctic_foxes");
        //These are test values-- real method will be probably called in a directions class?
        String startNode = "entrance_exit_gate";
        String endNode = "arctic_foxes";
        displayTextDirections(startNode, endNode, zooMap);
        //System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + directions/*"this one!"*/);
    }

    public void displayTextDirections(String startNode, String endNode, ZooMap zooMap){
        String directions = zooMap.getTextDirections(startNode, endNode);
        //System.out.println(directions);
        TextView textView = findViewById(R.id.path_result);
        textView.setText("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n"+directions);
        startNode = startNode.replaceAll("[_]"," ");
        endNode = endNode.replaceAll("[_]", " ");
        textView.setText("Shortest path from "+startNode+ " to "+ endNode+": \n"+directions);
    }
}