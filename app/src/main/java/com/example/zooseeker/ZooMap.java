package com.example.zooseeker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class ZooMap {
    Graph<String, IdentifiedWeightedEdge> graph;
    GraphPath<String, IdentifiedWeightedEdge> currPath;
    Map<String, ZooData.VertexInfo> locVertices;
    Map<String, ZooData.EdgeInfo> roadEdges;
    public ZooMap(Graph<String, IdentifiedWeightedEdge> graph, Map<String, ZooData.VertexInfo> locations, Map<String, ZooData.EdgeInfo> roads){
        this.graph = graph;
        this.locVertices = locations;
        this.roadEdges = roads;

    }

    public void setShortestPath(String node_from, String node_to) {
        currPath = getShortestPath(node_from, node_to);
    }

    public void setShortestPath(GraphPath<String, IdentifiedWeightedEdge> path) {
        currPath = path;
    }

    public GraphPath<String, IdentifiedWeightedEdge> getShortestPath(String node_from, String node_to){
        GraphPath<String, IdentifiedWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(graph, node_from, node_to);
        return shortest_path;
    }

    public List<String> getLandmarks(String node_from, String node_to){
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
        List<String> locs = new ArrayList<>();
        for (String v : currPath.getVertexList()) {
            locs.add(locVertices.get(v).name);
        }
        return locs;
    }

    public List<String> getStreets(String node_from, String node_to){
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
        List<String> streets = new ArrayList<>();
        for (IdentifiedWeightedEdge e : currPath.getEdgeList()) {
            streets.add(roadEdges.get(e.getId()).street);
        }
        return streets;
    }

    public double getDistance(String node_from, String node_to){
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
        return currPath.getWeight();
    }

    public List<String> getTextDirections(String node_from, String node_to){
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
        int i = 1;
        List<String> textDirections = new ArrayList<>();
        for (IdentifiedWeightedEdge e : currPath.getEdgeList()) {
            String textline = Integer.toString(i) + ". Walk " + String.valueOf(graph.getEdgeWeight(e)) + " meters along " + roadEdges.get(e).street + " from " + locVertices.get(graph.getEdgeSource(e).toString()).name + " to " + locVertices.get(graph.getEdgeTarget(e).toString()).name;
            textDirections.add(textline);
            i++;
        }
        return textDirections;
    }

}
