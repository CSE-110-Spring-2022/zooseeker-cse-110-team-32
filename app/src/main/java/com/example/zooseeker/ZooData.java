package com.example.zooseeker;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

/*This class populates the information about the zoo from the given example files
 */
public class ZooData {
    /*Embedded class that contains the information about locations
     */
    public static class VertexInfo {

        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION
        }

        public String id;
        public Kind kind;
        public String name;
        public List<String> tags;
    }

    /*Embedded class that contains information about the streets in between each Location
     */
    public static class EdgeInfo {
        public String id;
        public String street;
    }


    /*Loads information about each Location from given context
    Tries to load information from context and if it doesn't work, returns empty hashmap
    @param context = gives information of asset files that need to be loaded
    @returns a map with zoo Locations acting as map vertices
     */
    public static Map<String, ZooData.VertexInfo> loadVertexInfoJSON(Context context){
        try {
            Gson gson = new Gson();
            Map<String, String> fileNames = getFileNames(context);

            InputStream inputStream = context.getAssets().open(fileNames.get("nodes"));
            Reader reader = new InputStreamReader(inputStream);
            Type type = new TypeToken<List<ZooData.VertexInfo>>() {
            }.getType();
            List<ZooData.VertexInfo> zooData = gson.fromJson(reader, type);
            Map<String, ZooData.VertexInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            return indexedZooData;
        } catch (IOException e){
            return new HashMap<>();
        }
    }

    /* Loads information about each street in between Locations from given context
    tries to load information from context, and if it doesn't work, returns empty HashMap
    @param context = gives information of asset files that need to be loaded
    @return map with the streets acting as the edges
     */
    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(Context context){
        try {
            Gson gson = new Gson();
            Map<String, String> fileNames = getFileNames(context);

            InputStream inputStream = context.getAssets().open(fileNames.get("edges"));
            Reader reader = new InputStreamReader(inputStream);
            Type type = new TypeToken<List<ZooData.EdgeInfo>>() {
            }.getType();
            List<ZooData.EdgeInfo> zooData = gson.fromJson(reader, type);

            Map<String, ZooData.EdgeInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            return indexedZooData;
        } catch(IOException e){
            return new HashMap<>();
        }
    }

    /*Loads necessary tools for loading info from json files
    @param context = gives information of asset files that need to be loaded
    @return graph containing zoo info
     */
    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context){
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        try{
            Map<String, String> fileNames = getFileNames(context);

            importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);
            InputStream inputStream = context.getAssets().open(fileNames.get("graph"));

            Reader reader = new InputStreamReader(inputStream);

            // And now we just import it!
            importer.importGraph(g, reader);
        } catch (IOException e){

        }
        return g;
    }

    /*Gets names of json files from given context
    Tries to get names of json files from context, if it doesn't work, returns empty HashMap
    @param context = gives information of asset files that need to be loaded
    @return map containing file names
     */
    public static Map<String, String> getFileNames(Context context){
        try {
            Gson gson = new Gson();
            InputStream configInputStream = context.getAssets().open("config.json");
            Reader configReader = new InputStreamReader(configInputStream);
            Type configType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> fileNames = gson.fromJson(configReader, configType);
            return fileNames;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}