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

/*Class contains and returns information about the Zoo, as well as shortest paths between two exhibits
 */
public class ZooMap {
    Graph<String, IdentifiedWeightedEdge> graph;
    GraphPath<String, IdentifiedWeightedEdge> currPath;
    Map<String, ZooData.VertexInfo> locVertices;
    Map<String, ZooData.EdgeInfo> roadEdges;

    /*Constructor that populates zoo info from given context
    @param context = gives information of asset files that need to be loaded
     */
    public ZooMap(Context context){
        this.graph = ZooData.loadZooGraphJSON(context);
        this.locVertices = ZooData.loadVertexInfoJSON(context);
        this.roadEdges = ZooData.loadEdgeInfoJSON(context);

    }

    /*Sets stored shortest path between two exhibits for faster runtime
    @param node_from = start Location
    @param node_to = end Location
     */
    public void setShortestPath(String node_from, String node_to) {
        currPath = getShortestPath(node_from, node_to);
    }


    /*Returns the shortest path between two locations
    @param node_from = start location
    @param node_to = end location
    @return shortest path between the two given locations
     */
    public GraphPath<String, IdentifiedWeightedEdge> getShortestPath(String node_from, String node_to){
        GraphPath<String, IdentifiedWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(graph, node_from, node_to);
        return shortest_path;
    }

    /*Returns list of locations between start and end location
    @param node_from = start location
    @param node_to = end location
    @return list of locations between two points
     */
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

    /*Returns list of streets between start and end locations that user needs to take
    @param node_from = start location
    @param node_to = end location
    @return list of streets between start and end location
     */
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

    /*Returns distance between start location and destination
    @param node_from = start location
    @param node_to = end location
    @return distance between start and finish
     */
    public double getDistance(String node_from, String node_to){
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
        return currPath.getWeight();
    }

    /*Returns list of directions user needs to get from start location to next exhibit
    @param node_from = start location
    @param node_to = end location
    @return set of directions user needs to get from start to finish
     */
    public String getTextDirections(String node_from, String node_to){
        /*
        if (Objects.isNull(currPath) || !currPath.getStartVertex().equals(node_from) && !currPath.getEndVertex().equals(node_to)) {
            setShortestPath(node_from, node_to);
        }
         */
        setShortestPath(node_from, node_to);
        int i = 1;
        StringBuilder textDirections = new StringBuilder("");
        List<String> locations = currPath.getVertexList();
        for (IdentifiedWeightedEdge e : currPath.getEdgeList()) {
            String textline = Integer.toString(i) + ". Walk " + String.valueOf(graph.getEdgeWeight(e))
                    + " meters along " + roadEdges.get(e.getId()).street + " from "
                    + locVertices.get(locations.get(i-1)).name + " to "
                    + locVertices.get(locations.get(i)).name + "\n";
            textDirections.append(textline);
            i++;
        }
        return textDirections.toString();
    }

}
