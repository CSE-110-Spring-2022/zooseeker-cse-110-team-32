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

    /*Returns a list of information about exhibits that match or contains user's search query
    @param exhibitName = name of Exhibit user is searching for
    @return list of exhibits w/ exhibit info (including Gate if applicable) that contain ExhibitName
    either in the Exhibit's name or list of tags
     */
    public ArrayList<ZooData.VertexInfo> getResults(String exhibitName){
        //Iterate over map and find matching keys
        ArrayList<ZooData.VertexInfo> results = new ArrayList<>();

        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[completeMap.size()];
        valuesCollection.toArray(values);

        for (ZooData.VertexInfo vertex: valuesCollection){
            //Makes it so user input and search results aren't case sensitive
            String temp = vertex.name.toLowerCase();
            String searchText = exhibitName.toLowerCase();

            if (vertex.kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                //Checks exhibit names
                if (temp.contains(searchText)) {
                    results.add(vertex);
                } else{
                    //Checks exhibit's tags for query
                    for (String tag: vertex.tags) {
                        if (tag.contains(searchText)) {
                            results.add(vertex);
                            break;
                        }
                    }
                }
            }
        }
        return results;
    }

}