package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*This class creates a planned route that visits the exhibits the user selected. Based on where in
the list of exhibits the user is, they can go to the next or previous exhibit or notifies the user
when their route is over
 */
public class PlanList {
    private List<Location> myList;
    private int currLocationIndex;
    /*this currLocationIndex will be referring to which location user is on in the list
      this currLocation index will be updated when we call sort or when user manually put
      select their current location Default to be 0 when the list is initialized.

    */
    //adding this ZooMap object for future iteration
    private Context context;
    public ZooMap zooMap;
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

    /* Uses the given ID to find the corresponding exhibit within the user's list of exhibit
       @param id = ID of exhibit
       @return location that matches given ID
     */
    public Location findExhibit(String id){
        for (int i=0; i < myList.size(); i++){
            if (myList.get(i).getId().equals(id)){
                return myList.get(i);
            }
        }
        return null;
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

    /* Deletes location from list
        @param = index of location to be removed in user's list
     */
    public void deleteLocation(int i){
        this.myList.remove(i);
    }

    /* Replans the most optimized route using the user's list of exhibits after the user skips an exhibit
       @param ind = starting index of which exhibits need to be replanned (so if ind = 2 then exhibits
       2 and on need to be replanned)
     */
    public void replan(int ind){
        Location gate = myList.remove(myList.size()-1);
        //Creates a new list containing only the exhibits that need to be replanned

        for (int j=ind; j < myList.size()-1;j++){
            Location curr = myList.get(j);
            int smallestInd = j+1;
            double smallestDist = Double.MAX_VALUE;

            //Calculates distance between locations to determine which exhibit is the closest
            for (int i = j+1; i < myList.size(); i++){
                double dist = zooMap.getShortestPath(curr.getId(), myList.get(i).getId()).getWeight();
                if (dist < smallestDist){
                    smallestDist = dist;
                    smallestInd = i;
                }
            }
            //removes the exhibits that were replanned from the old list and inserts the new exhibit order
            Location next = myList.remove(smallestInd);
            myList.add(j+1,next);
        }
        myList.add(gate);
    }

    /*Sorts PlanList by starting at the gate, then picking an Exhibit out of the unadded Exhibits
    with the shortest distance to go next, repeating until all Exhibits have been added. Also
    appends the gate at the end
     */
    public void sort(){
        List<Location> sortList = new ArrayList<>();
        Boolean gateAdded = checkForGate(sortList);

        Location gate = new Gate("","", 0, 0);
        if(!gateAdded) gate = addGate(sortList);

        while(myList.size() > 0){
            Location curr = sortList.get(sortList.size()-1);
            //Calculates which exhibit is closest to current exhibit
            int closest = calculateClosest(curr);
            //Adds closest exhibit to the sorted list
            sortList.add(myList.get(closest));
            myList.remove(closest);
        }
        this.myList = sortList;
        myList.add(gate);
    }

    /* Adds entrance/exit gate to beginning of sorted list and checks for instances of gate in user's plan
       @param sortedList = list of exhibits in sorted order
       @return true if gate was added, false otherwise
     */
    public boolean checkForGate(List<Location> sortedList){
        Location startEnd;
        boolean gateAdded = false;
        for (int i = 0; i < planSize(); i++){
            Location stop = myList.get(i);
            if (stop.getKind() == ZooData.VertexInfo.Kind.GATE){
                startEnd = stop;
                if(!gateAdded) { sortedList.add(startEnd); }
                myList.remove(i);
                i--;
                gateAdded = true;
            }
        }
        return gateAdded;
    }

    /* Builds the sorted list by checking which exhibit is the closest to the most recently added
       exhibit in the user's list
       @param sortedList = list of exhibits in sorted order
       @return the gated added to sortedList
     */
    public Location addGate(List<Location> sortedList){
        Location gate = new Gate("","", 0, 0);
        for (Map.Entry<String, ZooData.VertexInfo> loc : zooLocs.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.GATE)){
                gate = new Gate(loc.getKey(), loc.getValue().name, loc.getValue().lat, loc.getValue().lng);
                sortedList.add(gate);
                break;
            }
        }
        return gate;
    }

    /* Calculates which exhibit is closest to current exhibit
       @param currentLoc = the location we're trying to find the closest exhibit to. It's the last location
                           added to the sortList
       @return the index of the location closest to currentLoc
     */
    public int calculateClosest(Location currentLoc){
        int smallestInd = 0;
        double smallestDist = Double.MAX_VALUE;
        for (int i = 0; i < myList.size(); i++){
            double dist = zooMap.getShortestPath(currentLoc.getId(), myList.get(i).getId()).getWeight();
            if (dist < smallestDist){
                smallestDist = dist;
                smallestInd = i;
            }
        }
        return smallestInd;
    }

    /* Clears the list from the database and resets the user's index to the beginning
       @param dao = database that stores user's list of exhibits
     */
    public void clearList(ExhibitDao dao){
        this.myList.clear();
        List<Exhibit> temp = dao.getAll();
        for(Exhibit e: dao.getAll()){
            dao.delete(e);
        }
        resetCurrLocationIndex();
        saveList(dao);
    }

    /* Resets the user's location index (ie how far down the list of exhibits the user is) to 0
     */
    public void resetCurrLocationIndex(){
        this.currLocationIndex = 0;
    }

    /* Returns list of exhibits (only exhibits, not locations)
       @return list of user's chosen exhibits
     */
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
