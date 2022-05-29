package com.example.zooseeker;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class DetailedDirectionsUnitTest {
    Context context;
    ZooMap zooMap;
    Graph<String, IdentifiedWeightedEdge> graph;
    Map<String, ZooData.VertexInfo> locVertices;
    Map<String, ZooData.EdgeInfo> roadEdges;


    @Before
    public void setup(){
        context = ApplicationProvider.getApplicationContext();
        zooMap = new ZooMap(context);
        graph = ZooData.loadZooGraphJSON(context);
        locVertices = ZooData.loadVertexInfoJSON(context);
        roadEdges = ZooData.loadEdgeInfoJSON(context);
    }

    @Test
    public void briefDirections(){
        Context context = ApplicationProvider.getApplicationContext();
        ZooMap zooMap = new ZooMap(context);
        GraphPath<String, IdentifiedWeightedEdge> path = zooMap.getShortestPath("siamang", "koi");
        String briefDirections = zooMap.getBriefTextDirections("siamang", "koi");
        List<String> locations = path.getVertexList();
        int i = 1;
        int step = 1;
        String currStreet = "";
        String start = "";
        double meters = 0;
        List<IdentifiedWeightedEdge> edges = path.getEdgeList();
        for(int e = 0; e < edges.size(); e++){
            IdentifiedWeightedEdge edge = edges.get(e);
            if(e == 0){
                currStreet = roadEdges.get(edge.getId()).street;
                start = locVertices.get(locations.get(i-1)).name;
                meters = graph.getEdgeWeight(edge);
                continue;
            }
            if (!currStreet.equals(roadEdges.get(edge.getId()).street)){
                assertTrue(briefDirections.contains(Integer.toString(step) + ". Walk " + meters
                        + " meters along " + currStreet + " from "
                        + start + " to "
                        + locVertices.get(locations.get(i)).name + "\n"));
                currStreet = roadEdges.get(edge.getId()).street;
                start = locVertices.get(locations.get(i)).name;
                meters = graph.getEdgeWeight(edge);
                step = step + 1;
            }
            else{
                meters += graph.getEdgeWeight(edge);
            }
            i = i+1;
        }
        assertTrue(briefDirections.contains(Integer.toString(step) + ". Walk " + meters
                + " meters along " + currStreet + " from "
                + start + " to "
                + locVertices.get(locations.get(i)).name + "\n"));
    }
}
