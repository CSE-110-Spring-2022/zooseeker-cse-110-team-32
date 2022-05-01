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
        // I don't know if this works but at least it compiles
        //Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        //Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        //Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        //ZooMap zooMap = new ZooMap(graph, vertices, edges);
        //List<String> directions = zooMap.getTextDirections("entrance_exit_gate", "arctic_foxes");
        //These are test values-- real method will be probably called in a directions class?

        ZooMap zooMap = populateMap("sample_zoo_graph.json",
                "sample_node_info.json","sample_edge_info.json");

        /*String startNode = "entrance_exit_gate";
        String endNode = "elephant_odyssey";
        ExhibitsList exhibits = new ExhibitsList();
        Exhibit start = new Exhibit(startNode);
        Exhibit end = new Exhibit(endNode);
        exhibits.addExhibit(start);
        exhibits.addExhibit(end);

        Exhibit nextOne = new Exhibit("gorillas");
        Exhibit afterThat = new Exhibit("elephant_odyssey");
        exhibits.addExhibit(nextOne);
        exhibits.addExhibit(afterThat);*/

        displayTextDirections(plan, zooMap);
        //System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + directions/*"this one!"*/);
    }

    public ZooMap populateMap(String graphFile, String verticesFile, String edgeFile){
        Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(this,graphFile);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(this, verticesFile);
        Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(this, edgeFile);
        ZooMap zooMap = new ZooMap(graph, vertices, edges);
        return zooMap;
    }
    public void displayTextDirections(PlanList list, ZooMap zooMap){
        List<String> directionsList = new ArrayList<String>();

        for(int i = 0; i < list.planSize()-1;i++){
            String startNode = list.getMyList().get(i).getId();
            String endNode = list.getMyList().get(i+1).getId();
            String directions = zooMap.getTextDirections(startNode, endNode);
            //System.out.println(directions);
            //TextView textView = findViewById(R.id.path_result);
            //textView.setText("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n"+directions);
            startNode = startNode.replaceAll("[_]"," ");
            endNode = endNode.replaceAll("[_]", " ");
            directions = "From: " + startNode + "\nTo: " + endNode + "\n\n" + directions;
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
            if(directionsList.size() == 0){
                next.setClickable(false);
                next.setVisibility(View.GONE);
            }

    }
}