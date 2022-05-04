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
        ZooMap zooMap = new ZooMap(context);
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

    @Test
    public void complex_path(){
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context);
        String start = "gorillas";
        String start_stop1 = "Africa Rocks Street";
        String stop1 = "entrance_plaza";
        String stop1_mid = "Arctic Avenue";
        String mid = "arctic_foxes";
        String end = "elephant_odyssey";
        GraphPath<String, IdentifiedWeightedEdge> directions = zooMap.getShortestPath(start, mid);
        List<String> stops = directions.getVertexList();
        List<String> streets = zooMap.getStreets(start, mid);
        double distance = 500.0;
        assertEquals(start, directions.getStartVertex());
        assertEquals(stop1, stops.get(1));
        assertEquals(mid, stops.get(2));
        assertEquals(start_stop1, streets.get(0));
        assertEquals(stop1_mid, streets.get(1));
        assertEquals(distance, directions.getWeight(), 0.01);
        GraphPath<String, IdentifiedWeightedEdge> directions2 = zooMap.getShortestPath(mid, end);
        List<String> stops2 = directions.getVertexList();
        List<String> streets2 = zooMap.getStreets(mid, end);
        double distance2 = 800.0;
        assertEquals(mid, directions2.getStartVertex());
        assertEquals(end, directions2.getEndVertex());
        assertEquals(distance, directions.getWeight(), 0.01);
    }
}