package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/*This class searches the available list of locations and returns a list of locations that match or
contain some part of what the user entered in the search bar. Displays results in a list of locations
where each location can be clicked on and added to the PlanList
 */
public class Search {
    //Fields
    //String exhibitName;
    Map<String, ZooData.VertexInfo> completeMap;
    Context context;

    /*Constructor that sets up Search class using given context to load location information that's used
    in search method
    @param context = gives information of asset files that need to be loaded
     */
    public Search(Context context){
        this.context = context;
        //this.exhibitName = exhibitName;
        //get complete map with ZooData loadVertexInfoJSON method
        this.completeMap = ZooData.loadVertexInfoJSON(context);
    }

    /*Returns list of locations that matches or contains the user's search query
    @param exhibitName = name of Exhibit user is searching for
    @return list of exhibit names (including Gate if applicable) that contain ExhibitName either in the
    Exhibit's name or list of tags
     */
    public ArrayList<String> getResults(String exhibitName){

        //Iterate over map and find matching keys
        ArrayList<String> results = new ArrayList<>();

        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);
        System.out.println(valuesCollection.size());

        for (ZooData.VertexInfo vertex: valuesCollection){
            String temp = vertex.name.toLowerCase();

            if (temp.contains(exhibitName.toLowerCase())){
                results.add(vertex.name);
            }
            else if(vertex.tags != null && vertex.tags.contains(exhibitName)){
                results.add(vertex.name);
            }
        }
        return results;
    }

    /*Returns a list of information about exhibits that match or contains user's search query
    @param exhibitName = name of Exhibit user is searching for
    @return list of exhibits w/ exhibit info (including Gate if applicable) that contain ExhibitName
    either in the Exhibit's name or list of tags
     */
    public ArrayList<ZooData.VertexInfo> getResultsInfo(String exhibitName){
        //Iterate over map and find matching keys
        ArrayList<ZooData.VertexInfo> results = new ArrayList<>();

        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);

        for (ZooData.VertexInfo vertex: valuesCollection){
            String temp = vertex.name.toLowerCase();

            if (temp.contains(exhibitName)){
                results.add(vertex);
            }
            else if(vertex.tags != null){
                for(String tagName: vertex.tags){
                    if(tagName.contains(exhibitName.toLowerCase()) || (exhibitName.toLowerCase()).contains(tagName)){
                        results.add(vertex);
                        break;
                    }
                }

            }
        }
        return results;
    }

}
