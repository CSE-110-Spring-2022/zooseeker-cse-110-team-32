package com.example.zooseeker;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.List;

public class Directions {
    GraphPath<String, DefaultWeightedEdge> graphPath;
    public Directions(GraphPath<String, DefaultWeightedEdge> graphPath){
        this.graphPath = graphPath;
    }

    public String getStart(){
        return graphPath.getStartVertex();
    }

    public String getEnd(){
        return graphPath.getEndVertex();
    }

    public List<String> getLandmarks(){
        return graphPath.getVertexList();
    }

    public double getDistance(){
        return graphPath.getWeight();
    }
}
