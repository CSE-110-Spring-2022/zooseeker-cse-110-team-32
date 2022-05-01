package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class SearchItem {
    //Fields
    //String exhibitName;
    String fileName;
    Map<String, ZooData.VertexInfo> completeMap;
    Context context;

    //Constructor
    public SearchItem(Context context, String fileName){
        this.context = context;
        //this.exhibitName = exhibitName;
        this.fileName = fileName;
        //get complete map with ZooData loadVertexInfoJSON method
        this.completeMap = ZooData.loadVertexInfoJSON(context, fileName);
    }

    public ArrayList<String> getResults(String exhibitName){
        //Iterate over map and find matching keys
        ArrayList<String> results = new ArrayList<>();
        
        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);

        for (ZooData.VertexInfo vertex: values){
            if (vertex.name.contains(exhibitName)){
                results.add(vertex.name);
            }
        }
        return results;
    }

}
