package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public int currLocationIndex;
    //adding this ZooMap object for future iteration
    private Context context;
    public ZooMap zooMap;
    public Map<String, ZooData.VertexInfo> zooLocs;

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

    /* Sets user's planned list (only called when replacing old list with sorted list
       @param list = sorted list containing myList's exhibits (plus entrance/exit gate)
     */
    public void setMyList(List<Location> list){
        this.myList = list;
    }
    /*based on the Exhibit that was passed in, returns the index of the Exhibit (ie how far down the
    exhibit is in their list
    @param curr = the exhibit the user is currently at
    @return index indicating where the current exhibit is in their list
     */
    public int currExhibitIndex(Exhibit curr) {
        return myList.indexOf(curr);
    }

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

    /* Checks if the given location is already in the user's list of exhibits
       @param location = location to be checked to see if it's already in the list
       @return true if already in list, false otherwise
     */
    public boolean checkIfInList(Location location){
        for (int i=0; i < myList.size(); i++){
            if (myList.get(i).getId().equals(location.getId())){
                return true;
            }
        }
        return false;
    }
    /* General method for adding locations to user's list of planned exhibits.
       @param e = name of location (can be Exhibit or other)
       @return whether location was successfully added or not
        */
    public Boolean add(Location e){
        return this.myList.add(e);
    }

    /* Adds given location at given index in user's list of planned exhibits
       @param index = where the location would be inserted
       @param location = location to be added to list
       @return true if added to list, false if otherwise
     */
    public boolean addLocation(int index, Location location){
        if(checkIfInList(location)) return false;
        this.myList.add(index, location);
        return true;
    }

    public Boolean addLocation(Location e) {
        if(checkIfInList(e)) return false;
        return this.myList.add(e);
    }


    public void replaceLocationIndex(int index, Location e) {
        myList.set(index, e);
    }

      /* Deletes location from list
        @param = index of location to be removed in user's list
     */

    public Location deleteLocation(int i){
        return this.myList.remove(i);
    }




    public void clearList(ExhibitDao dao){
        this.myList.clear();
        List<Exhibit> temp = dao.getAll();
        for(Exhibit e: dao.getAll()){

            dao.delete(e);
        }
        resetCurrLocationIndex();
        saveList(dao);
    }

    public void resetCurrLocationIndex(){
        this.currLocationIndex = 0;
    }

    public List<Exhibit> getExhibits() {
        List<Exhibit> result = new ArrayList<>();
        for (Location loc : this.myList) {
            if ((loc.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT))) {
                result.add((Exhibit) loc);
            } else if ((loc.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP))) {
                //System.out.println("hi " + loc.getKind() + " " + loc.getName() +" " + loc.getClass());
                Map<String, Exhibit> animals = ((ExhibitGroup) loc).getAnimals();
                for (Exhibit ex : animals.values()) {
                    result.add(ex);
                    //System.out.println("for " + ex.getKind() + " " + ex.getName() +" " + ex.getClass() + " " + ex.parentId);
                }
            }
        }
        return result;
    }

    public void resetDb(ExhibitDao dao){
        List<Exhibit> temp = dao.getAll();
        for(Exhibit e: dao.getAll()){
            dao.delete(e);
        }
        saveList(dao);
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
        Map<String, ExhibitGroup> allGroups = new HashMap<>();
        for (Exhibit ex : allExhibits) {
            if (ex.parentId != null && allGroups.get(ex.parentId) == null) {
                ExhibitGroup temp = new ExhibitGroup(ex.parentId, ex.parentName, ex.lat, ex.lng);
                //System.out.println("parent id " + ex.parentId);
                temp.addAnimal(ex.id, ex);
                newList.add(temp);
                allGroups.put(temp.getId(), temp);
            } else if (allGroups.get(ex.parentId) != null){
                ExhibitGroup temp = allGroups.get(ex.parentId);
                temp.addAnimal(ex.getId(), ex);
            } else {
                newList.add(ex);
            }
        }
        this.myList = newList;
        Location gate = ZooData.getGate(context);
        this.myList.add(0, gate);
        this.myList.add(gate);
        //this.sort();
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
            System.out.print("id: " + loc.getId() + " kind: ");
            System.out.println(loc.getKind());
        }
        System.out.println("Finish printing.");
    }
}
