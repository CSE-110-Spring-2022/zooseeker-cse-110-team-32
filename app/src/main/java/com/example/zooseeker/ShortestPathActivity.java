package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;

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
        // I don't know if this works but at least it compiles
        Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        ZooMap zooMap = new ZooMap(graph, vertices, edges);
        //List<String> directions = zooMap.getTextDirections("entrance_exit_gate", "arctic_foxes");
        //These are test values-- real method will be probably called in a directions class?
        String startNode = "entrance_exit_gate";
        String endNode = "elephant_odyssey";
        ExhibitsList exhibits = new ExhibitsList();
        Exhibit start = new Exhibit(startNode);
        Exhibit end = new Exhibit(endNode);
        exhibits.addExhibit(start);
        exhibits.addExhibit(end);

        Exhibit nextOne = new Exhibit("gorillas");
        Exhibit afterThat = new Exhibit("elephant_odyssey");
        exhibits.addExhibit(nextOne);
        exhibits.addExhibit(afterThat);
        displayTextDirections(exhibits, zooMap);
        //System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + directions/*"this one!"*/);
    }

    public void displayTextDirections(ExhibitsList list, ZooMap zooMap){
        List<String> directionsList = new ArrayList<String>();

        for(int i = 0; i < list.size()-1;i++){
            String startNode = list.get(i).toString();
            String endNode = list.get(i+1).toString();
            String directions = zooMap.getTextDirections(startNode, endNode);
            //System.out.println(directions);
            //TextView textView = findViewById(R.id.path_result);
            //textView.setText("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n"+directions);
            startNode = startNode.replaceAll("[_]"," ");
            endNode = endNode.replaceAll("[_]", " ");
            directions = "Shortest path from " + startNode + " to " + endNode + ": \n" + directions;
            directionsList.add(directions);
            //textView.setText("Shortest path from "+startNode+ " to "+ endNode+": \n"+directions);
        }

        Button next = findViewById(R.id.next_btn);
        if(directionsList.size() > 0){
            displayNextHelper(next, directionsList);
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                displayNextHelper(next, directionsList);
            });
        }
    }

    public void displayNextHelper(Button next, List<String> directionsList){

            TextView textView = findViewById(R.id.path_result);
            textView.setText(directionsList.get(0));
            directionsList.remove(0);
            if(directionsList.size() ==0){
                next.setClickable(false);
                next.setVisibility(View.GONE);
            }

    }
}