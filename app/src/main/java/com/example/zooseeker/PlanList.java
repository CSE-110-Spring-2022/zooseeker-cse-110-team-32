package com.example.zooseeker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlanList {
    private List<Location> myList;
    /*
        this currLocationIndex will be referring to which location user is on in the list
        this currLocation index will be updated when we call sort or when user manually put
        select their current location Default to be 0 when the list is initialized.
     */
    private int currLocationIndex;
    //adding this ZooMap object for future iteration
    //private ZooMap zooMap;

    public PlanList(String json_data) {
        this.myList = new ArrayList<>();
        //this.zooMap = new ZooMap(json_data);
        this.currLocationIndex = 0;
    }

    public int currExhibitIndex(Exhibit curr) {
        return myList.indexOf(curr);
    }

    public Boolean addLocation(Location e) {
        return this.myList.add(e);
    }

    public Location getCurrentLocation() {
        return this.myList.get(currLocationIndex);
    }

    /*
        This method is to be implemented after the merge with the shortest path branch
        it will use zooMap object to get the shortest path from current location to the
        next location in the list
     */
//    public Graph<String, IdentifiedWeightedEdge> getPathToNextLocation() {
//        Location curr = this.myList.get(currLocationIndex);
//        String currId = curr.getId();
//        Location next = this.myList.get(currLocationIndex + 1);
//        String nextId = next.getId();
//        return zooMap.getShortestPath(currId, nextId);
//    }

    public void advanceLocation() {
        this.currLocationIndex++;
    }
    /*
        These two methods are for saving and loading PlanList, at the moment because
        sharedPreferences cannot store object, it only store strings. There is a way to doing
        serialization but that would be pretty complicated to do. The other way involving
        putting the name strings into a set into the preferences and to load them, we use
        string name as query to fetch the data from the database
        I am currently using the second way by converting the arraylist into a set, and then
        put the ID into the set and retrieve the set in the loadList method
     */
    public void saveList(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("List_File", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> saveList = new HashSet<>();
        for (Location e : myList) {
            saveList.add(e.getId());
        }
        editor.putStringSet("list", saveList);
        editor.apply();
    }

    /*
    Experimental stage: this function will be used to load the list from preferences, the list
    stored in preferences are list of Location IDs, then this method will retrieve the location
    object from the database and add them to the PlanList(aka the arraylist). Since the ordering
    might be different, we will call sort method in this class to sort the arraylist.
    As you can see outDao will be our future database and sort will be implemented in the future.
     */
//    public void loadList(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences("List_File",
//                Context.MODE_PRIVATE);
//        Set<String> retrieveList = preferences.getStringSet("list", null);
//        ArrayList<Location> newList = new ArrayList<>();
//        for (String LocId : retrieveList ) {
//            Location item = outDao.get(LocId);
//            newList.add(item);
//        }
//        this.myList = newList;
//        this.sort();
//
//    }

    //toImplement sort

}
