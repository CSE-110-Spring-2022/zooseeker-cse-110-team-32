package com.example.zooseeker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private Context context;
    private ZooMap zooMap;
    private Map<String, ZooData.VertexInfo> zooLocs;

    public PlanList(Context context) {
        this.myList = new ArrayList<>();
        this.context = context;
        this.zooMap = new ZooMap(context);
        this.currLocationIndex = 0;
        this.zooLocs = ZooData.loadVertexInfoJSON(context);
    }

    public void changeContext(Context newContext) {
        this.context = newContext;
    }

    public List<Location> getMyList() { return this.myList; }

    public int currExhibitIndex(Exhibit curr) {
        return myList.indexOf(curr);
    }

    public Boolean endReached(){
        if (currLocationIndex == planSize()-2){
            return true;
        }
        return false;
    }

    public int planSize(){
        return myList.size();
    }

    public Boolean addLocation(Location e) {
        for (int i=0; i < myList.size(); i++){
            if (myList.get(i).getId().equals(e.getId())){
                return false;
            }
        }
        return this.myList.add(e);
    }

    public Boolean addGate(Location g){
        for (int i=0; i < myList.size(); i++){
            if (myList.get(i).getId().equals(g.getId())){
                return false;
            }
        }
        myList.add(0, g);
        return true;
    }

    public Location getCurrentLocation() {
        return this.myList.get(currLocationIndex);
    }

    public Location getNextLocation() {
        if (currLocationIndex + 1 < planSize()){
            return this.myList.get(currLocationIndex+1);
        }
        return null;
    }

    public Location getNextNextLocation() {
        if (currLocationIndex + 2 < planSize()){
            return this.myList.get(currLocationIndex+2);
        }
        return null;
    }

    /*
        This method is to be implemented after the merge with the shortest path branch
        it will use zooMap object to get the shortest path from current location to the
        next location in the list
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPathToNextLocation() {
        Location curr = this.myList.get(currLocationIndex);
        String currId = curr.getId();
        Location next = this.myList.get(currLocationIndex + 1);
        String nextId = next.getId();
        return zooMap.getShortestPath(currId, nextId);
    }

    public GraphPath<String, IdentifiedWeightedEdge> getPathToNextNextLocation() {
        if (currLocationIndex + 2 >= this.myList.size()) {
            return null;
        }
        Location next = this.myList.get(currLocationIndex + 1);
        String nextId = next.getId();
        Location nextNext = this.myList.get(currLocationIndex + 2);
        String nextNextId = nextNext.getId();
        return zooMap.getShortestPath(nextId, nextNextId);
    }

    public String getDirectionsToNextLocation() {
        Location curr = this.myList.get(currLocationIndex);
        String currId = curr.getId();
        Location next = this.myList.get(currLocationIndex + 1);
        String nextId = next.getId();
        return zooMap.getTextDirections(currId, nextId);
    }

    public double getDistanceToNextLocation(){
        return getPathToNextLocation().getWeight();
    }

    public double getDistanceToNextNextLocation(){
        return getPathToNextNextLocation().getWeight();
    }


    public Boolean advanceLocation() {
        if(currLocationIndex+1 >= myList.size()){
            return false;
        }
        this.currLocationIndex++;
        return true;
    }

    public Boolean previousLocation() {
        if(currLocationIndex < 1){
            return false;
        }
        this.currLocationIndex--;
        return true;
    }

    public void sort(){
        List<Location> sortList = new ArrayList<>();
        Location startEnd;
        Boolean gateAdded = false;
        for (int i = 0; i < planSize(); i++){
            Location stop = myList.get(i);
            if (stop.getKind() == ZooData.VertexInfo.Kind.GATE){
                startEnd = stop;
                if(!gateAdded) {
                    sortList.add(startEnd);
                }
                myList.remove(i);
                i--;
                gateAdded = true;
            }
        }
        Location gate = new Gate("","", new ArrayList<>());
        if(!gateAdded){
            for (Map.Entry<String, ZooData.VertexInfo> loc : zooLocs.entrySet()){
                if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.GATE)){
                    gate = new Gate(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                    sortList.add(gate);
                    break;
                }
            }
        }

        while(myList.size() > 0){
            Location curr = sortList.get(sortList.size()-1);
            int smallestInd = 0;
            double smallestDist = Double.MAX_VALUE;
            for (int i = 0; i < myList.size(); i++){
                double dist = zooMap.getShortestPath(curr.getId(), myList.get(i).getId()).getWeight();
                if (dist < smallestDist){
                    smallestDist = dist;
                    smallestInd = i;
                }
            }
            sortList.add(myList.get(smallestInd));
            myList.remove(smallestInd);
        }
        this.myList = sortList;
        myList.add(gate);
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
    public void saveList() {
        SharedPreferences preferences =
                this.context.getSharedPreferences("List_File", Context.MODE_PRIVATE);
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
//    public void loadList() {
//        SharedPreferences preferences = this.context.getSharedPreferences("List_File",
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
