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

    /*returns user's list of planned exhibits
    @return user's list of exhibits
     */
    public List<Location> getMyList() { return this.myList; }

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

    /*Returns the location user is currently at
    @returns user's location
     */

    public Boolean addGate(Location g) {
        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).getId().equals(g.getId())) {
                return false;
            }
        }
        return this.myList.add(g);
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

    public List<Exhibit> getExhibits() {
        List<Exhibit> result = new ArrayList<>();
        for (Location loc : this.myList) {
            if (loc.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)) {
                result.add((Exhibit)loc);
            }
        }
        return result;
    }
    /*
        saveList takes a database dao as an argument and it will call get exhibits, and
        store all the current exhibits into the database
     */
    public void saveList(ExhibitDao dao) {
        List<Exhibit> allExhibits = this.getExhibits();
        for (Exhibit ex : allExhibits) {
            if (dao.get(ex.id) == null) {
                dao.insert(ex);
            }
        }
    }

    /*
        loadList takes a database dao as an argument and it will add the exhibit into a new
        arrayList and then this new arraylist will replace the current list. Then calling sort
        function to restore it's past values.
     */

    public void loadList(ExhibitDao dao) {
        List<Exhibit> allExhibits = dao.getAll();
        List<Location> newList = new ArrayList<>();
        for (Exhibit ex : allExhibits) {
            newList.add((Location)ex);
        }
        this.myList = newList;
        this.sort();
    }


    /*
    debug method for planList: print out the current Location in the list with its name and kind
    useful during unit testing, scroll down in the output screen until you see the word printing
    list:, and followed by all the items inside the list with its id and kind, when finished
    it will show Finish printing.
     */
    public void printList() {
        System.out.println("printing list:");
        for (Location loc : myList) {
            System.out.print("id: " + loc.getKind() + "kind: ");
            System.out.println(loc.getKind());
        }
        System.out.println("Finish printing.");
    }
}
