package com.example.zooseeker;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ShortestPathUnitTest {
    @Before
    public void createContext(){

    }

    @Test
    public void direct_path() {
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context,"sample_zoo_graph.json");
        String start = "entrance_exit_gate";
        String street_id = "edge-0";
        String street_name = "Entrance Way";
        String end = "entrance_plaza";
        Directions directions = zooMap.calculateShortestPath(start, end);
        double distance = 10.0;
        assertEquals(start, directions.getStart());
        assertEquals(end, directions.getEnd());
        assertEquals(distance, directions.getDistance(), 0.01);
        List<IdentifiedWeightedEdge> streets = directions.getStreets();
        assertEquals(street_id, streets.get(0).getId());
    }

    @Test
    public void indirect_path(){
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context,"sample_zoo_graph.json");
        String start = "entrance_plaza";
        String mid = "gators";
        String end = "lions";
        Directions directions = zooMap.calculateShortestPath(start, end);
        double distance = 300.0;
        assertEquals(start, directions.getStart());
        assertEquals(mid, directions.getLandmarks().get(1));
        assertEquals(end, directions.getEnd());
        assertEquals(distance, directions.getDistance(), 0.01);
    }
}