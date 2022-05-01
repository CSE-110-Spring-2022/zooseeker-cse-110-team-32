package com.example.zooseeker;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;

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
    public List<Location> getMyList() { return this.myList; }

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


    //toImplement sort

}
