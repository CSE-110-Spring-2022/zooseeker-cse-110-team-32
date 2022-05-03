package com.example.zooseeker;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;


public class Search {
    //Fields
    //String exhibitName;
    Map<String, ZooData.VertexInfo> completeMap;
    Context context;

    //Constructor
    public Search(Context context, String fileName){
        this.context = context;
        //this.exhibitName = exhibitName;
        //get complete map with ZooData loadVertexInfoJSON method
        this.completeMap = ZooData.loadVertexInfoJSON(context, fileName);
    }

    public ArrayList<String> getResults(String exhibitName){

        //Iterate over map and find matching keys
        ArrayList<String> results = new ArrayList<>();

        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);
        System.out.println(valuesCollection.size());

        for (ZooData.VertexInfo vertex: valuesCollection){
            String queryLowerCase = vertex.name.toLowerCase();

            if (queryLowerCase.contains(exhibitName.toLowerCase())){
                results.add(vertex.name);
            }
            else if(vertex.tags != null){
                for(String tagName: vertex.tags){
                    if(tagName.contains(exhibitName.toLowerCase()) || (exhibitName.toLowerCase()).contains(tagName)){
                        results.add(vertex.name);
                        break;
                    }
                }

            }
        }
        return results;
    }

    public ArrayList<ZooData.VertexInfo> getResultsInfo(String exhibitName){
        //Iterate over map and find matching keys
        ArrayList<ZooData.VertexInfo> results = new ArrayList<>();

        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);

        for (ZooData.VertexInfo vertex: valuesCollection){
            String queryLowerCase = vertex.name.toLowerCase();

            if (queryLowerCase.contains(exhibitName)){
                results.add(vertex);
            }
            else if(vertex.tags != null){
                for(String tagName: vertex.tags){
                    if(tagName.contains(exhibitName.toLowerCase()) || (exhibitName.toLowerCase()).contains(tagName)){
                        results.add(vertex);
                        break;
                    }
                }
                //results.add(vertex.name);
            }
        }
        return results;
    }

}
