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
        ZooMap zooMap = new ZooMap(context);
        String start = "entrance_exit_gate";
        String street_id = "gate_to_front";
        String street_name = "Gate Path";
        String end = "intxn_front_treetops";
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
        ZooMap zooMap = new ZooMap(context);
        String start = "entrance_exit_gate";
        String street1_name = "Gate Path";
        String street2_name = "Front Street";
        String end = "flamingo";
        GraphPath<String, IdentifiedWeightedEdge> directions = zooMap.getShortestPath(start, end);
        double distance = 90.0;
        assertEquals(start, directions.getStartVertex());
        assertEquals(end, directions.getEndVertex());
        assertEquals(distance, directions.getWeight(), 0.01);
        List<String> streets = zooMap.getStreets(start, end);
        assertEquals(street1_name, streets.get(0));
        assertEquals(street2_name, streets.get(1));
    }

    @Test
    public void complex_path(){
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context);
        String start = "gorilla";
        String start_stop1 = "Monkey Trail";
        String stop1 = "scripps_aviary";
        String stop1_mid = "Monkey Trail";
        String mid = "intxn_hippo_monkey_trails";
        String end = "hippo";
        GraphPath<String, IdentifiedWeightedEdge> directions = zooMap.getShortestPath(start, mid);
        List<String> stops = directions.getVertexList();
        List<String> streets = zooMap.getStreets(start, mid);
        double distance = 130.0;
        assertEquals(start, directions.getStartVertex());
        assertEquals(stop1, stops.get(1));
        assertEquals(mid, stops.get(2));
        assertEquals(start_stop1, streets.get(0));
        assertEquals(stop1_mid, streets.get(1));
        assertEquals(distance, directions.getWeight(), 0.01);
        GraphPath<String, IdentifiedWeightedEdge> directions2 = zooMap.getShortestPath(mid, end);
        List<String> stops2 = directions.getVertexList();
        List<String> streets2 = zooMap.getStreets(mid, end);
        double distance2 = 40.0;
        assertEquals(mid, directions2.getStartVertex());
        assertEquals(end, directions2.getEndVertex());
        assertEquals(distance2, directions2.getWeight(), 0.01);
    }
}