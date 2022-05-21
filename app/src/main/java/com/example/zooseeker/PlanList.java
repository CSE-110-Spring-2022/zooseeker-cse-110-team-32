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

/*This class creates a planned route that visits the exhibits the user selected. Based on where in
the list of exhibits the user is, they can go to the next or previous exhibit or notifies the user
when their route is over
 */
public class PlanList {
    private List<Location> myList;

    /*this currLocationIndex will be referring to which location user is on in the list
      this currLocation index will be updated when we call sort or when user manually put
      select their current location Default to be 0 when the list is initialized.

    */
    private int currLocationIndex;
    //adding this ZooMap object for future iteration
    private Context context;
    private ZooMap zooMap;
    private Map<String, ZooData.VertexInfo> zooLocs;

    /*Constructor that sets the information of the list of planned exhibits using the data passed in
   @param context = gives information of asset files that need to be loaded
    */
    public PlanList(Context context) {
        this.myList = new ArrayList<>();
        this.context = context;
        this.zooMap = new ZooMap(context);
        this.currLocationIndex = 0;
        this.zooLocs = ZooData.loadVertexInfoJSON(context);
    }

    /* Allows for changing the context (ie changing the asset files used for populating the zooMap
    @param context = gives information of asset files that need to be loaded
     */
    public void changeContext(Context newContext) {
        this.context = newContext;
    }

    /*returns user's list of planned exhibits, only used for testing purposes
    @return user's list of exhibits
     */
    public List<Location> getMyList() { return this.myList; }

    /*returns location at given index
    @return location at given index
     */
    public Location get(int i){
        return myList.get(i);
    }

    /*based on the Exhibit that was passed in, returns the index of the Exhibit (ie how far down the
    exhibit is in their list
    @param curr = the exhibit the user is currently at
    @return index indicating where the current exhibit is in their list
     */
    public int currExhibitIndex(Exhibit curr) {
        return myList.indexOf(curr);
    }

    /*returns the number of exhibits the user plans to see
   @return number of exhibits user selected
    */
    public int planSize(){
        return myList.size();
    }

    /*Returns map of zoo
    @return map of zoo
     */
    public ZooMap getZooMap(){
        return this.zooMap;
    }

    /*adds exhibit to user's list of planned exhibits.
   Checks to see if location has already been added. Returns true if location has not been added
   before now and location was successfully added, and false otherwise.
   @param e = name of location (can be Exhibit or other) user wants to see
   @return whether location was successfully added or not
    */
    public Boolean addLocation(Location e) {
        for (int i=0; i < myList.size(); i++){
            if (myList.get(i).getId().equals(e.getId())){
                return false;
            }
        }
        return this.myList.add(e);
    }

    public void deleteLocation(int i){
        this.myList.remove(i);
    }


    /*Sorts PlanList by starting at the gate, then picking an Exhibit out of the unadded Exhibits
    with the shortest distance to go next, repeating until all Exhibits have been added. Also
    appends the gate at the end
     */
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


}
