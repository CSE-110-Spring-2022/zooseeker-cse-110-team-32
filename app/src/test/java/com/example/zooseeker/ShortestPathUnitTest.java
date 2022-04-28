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

@RunWith(AndroidJUnit4.class)
public class ShortestPathUnitTest {
    @Before
    public void createContext(){

    }

    @Test
    public void direct_path() {
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context,"sample_graph.JSON");
        Directions directions = zooMap.calculateShortestPath("entranceExitGate1", "gatorViewpoint1");
        String start = "entranceExitGate1";
        String end = "gatorViewpoint1";
        double distance = 2.0;
        assertEquals(start, directions.getStart());
        assertEquals(end, directions.getEnd());
        assertEquals(distance, directions.getDistance(), 0.01);
    }

    @Test
    public void indirect_path(){
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context,"sample_graph.JSON");
        Directions directions = zooMap.calculateShortestPath("arcticFoxViewpoint", "elephantFoxIntersection");
        String start = "arcticFoxViewpoint";
        String mid = "gorillaViewpoint1";
        String end = "elephantFoxIntersection";
        double distance = 4.0;
        assertEquals(start, directions.getStart());
        assertEquals(mid, directions.getLandmarks().get(1));
        assertEquals(end, directions.getEnd());
        assertEquals(distance, directions.getDistance(), 0.01);
    }
}