package com.example.zooseeker;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ShortestPathUnitTest {
    @Before
    public void createContext(){

    }

    @Test
    public void direct_path() {
        Context context = ApplicationProvider.getApplicationContext();
        Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        ZooMap zooMap = new ZooMap(graph, vertices, edges);
        String start = "entrance_exit_gate";
        String street_id = "edge-0";
        String street_name = "Entrance Way";
        String end = "entrance_plaza";
        GraphPath<String, IdentifiedWeightedEdge> directions = zooMap.getShortestPath(start, end);
        double distance = 10.0;
        assertEquals(start, directions.getStartVertex());
        assertEquals(end, directions.getEndVertex());
        assertEquals(distance, directions.getWeight(), 0.01);
        List<String> streets = zooMap.getStreets(start, end);
        assertEquals(street_name, streets.get(0));
    }

    @Test
    public void indirect_path(){
        Context context = ApplicationProvider.getApplicationContext();
        Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> edges = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        ZooMap zooMap = new ZooMap(graph, vertices, edges);
        String start = "entrance_plaza";
        String street1_name = "Reptile Road";
        String mid = "gators";
        String street2_name = "Sharp Teeth Shortcut";
        String end = "lions";
        GraphPath<String, IdentifiedWeightedEdge> directions = zooMap.getShortestPath(start, end);
        double distance = 300.0;
        assertEquals(start, directions.getStartVertex());
        assertEquals(end, directions.getEndVertex());
        assertEquals(distance, directions.getWeight(), 0.01);
        List<String> streets = zooMap.getStreets(start, end);
        assertEquals(street1_name, streets.get(0));
        assertEquals(street2_name, streets.get(1));
    }
}