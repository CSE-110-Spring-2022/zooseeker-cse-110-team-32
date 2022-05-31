package com.example.zooseeker;

import org.jgrapht.GraphPath;

/*This class navigates through the user's list of planned exhibits and gives directions to each exhibit
starting from the entrance gate
 */
public class NavigatePlannedList {

    private PlanList planList;
    public int currLocationIndex;
    public Boolean forwards;

    /*Constructor that sets the PlanList to use based on what's passed in
    @param plan = plan to navigate through
     */
    NavigatePlannedList(PlanList plan){
        this.planList = plan;
        this.currLocationIndex = 0;
        this.forwards = true;

    }

    /*tells whether the user is at the entrance gate
   returns true if the user is at the entrance gate and hasn't seen any exhibits and returns false otherwise
   @return whether or not user is at beginning of their list
     */
    public Boolean atStart(){
        if (currLocationIndex == 0){
            return true;
        }
        return false;
    }

    /*tells us whether the user is at the first exhibit
    returns true if the user is at the first exhibit and false otherwise
    @return whether or not user is at first exhibit
    */
    public Boolean atFirst(){
        if (currLocationIndex == 1){
            return true;
        }
        return false;
    }

    /*tells whether the end of the exhibit has been reached
   returns true if the user is at the end of the exhibit and returns false otherwise
   @return whether or not user is at end of their list
    */
    public Boolean endReached(){
        if (currLocationIndex >= this.planList.planSize()-2){
            return true;
        }
        return false;
    }


    /*returns user's planned list of exhibits
    @return user's planned list of exhibits
     */
    public PlanList getPlanList(){
        return this.planList;
    }

    /*
        Resets user's current location index to 0 when list is cleared
     */
    public void resetCurrLocationIndex(){
        this.currLocationIndex = 0;
    }



    /*based on the Exhibit that was passed in, returns the index of the Exhibit (ie how far down the
    exhibit is in their list
    @param curr = the exhibit the user is currently at
    @return index indicating where the current exhibit is in their list
     */
    public int currExhibitIndex(Exhibit curr) {
        return this.planList.getMyList().indexOf(curr);
    }

    /*Returns user's current location
    @return current location
     */
    public Location getCurrentLocation() {
        return this.planList.get(currLocationIndex);
    }

    /*returns the next location in planned list of exhibits
    If user has reached the end of their list of exhibits, return null
    @returns the next location in list or null if there aren't any
     */
    public Location getNextLocation() {
        if (currLocationIndex + 1 < this.planList.planSize()){
            return this.planList.get(currLocationIndex+1);
        }
        return null;
    }

    /*returns the previous location in planned list of exhibits
    If user has reached the start of their list of exhibits, return null
    @returns the previous location in list or null if there aren't any
     */
    public Location getPrevLocation() {
        if (currLocationIndex - 1 >= 0){
            return this.planList.getMyList().get(currLocationIndex-1);
        }
        return null;
    }

    /*returns the next or previous location depending on going forwards or backwards
    @returns the next/previous location in list or null if there aren't any
    */
    public Location getDestination(){
        if(forwards){
            return getNextLocation();
        }
        else{
            return getPrevLocation();
        }
    }

    /*Returns location after the next location
    @return location after next
     */
    public Location getNextNextLocation() {
        if (currLocationIndex + 2 < this.planList.planSize()){
            return this.planList.get(currLocationIndex+2);
        }
        return null;
    }

    /*Returns path to next location
    @return path to next location
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPathToNextLocation() {
        return getPlanPath(0);
    }

    /*Returns path to location after next location
    @return path to next next location
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPathToNextNextLocation() {
        return getPlanPath(1);
    }

    /*Returns the path through the zoo graph (not directions) user needs to take to get to exhibit
    @return graph showing path to take
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPlanPath(int offset) {
        int currInd = currLocationIndex + offset;
        if (currInd < 0 || currInd > this.planList.planSize()){
            return null;
        }
        Location curr = this.planList.get(currInd);
        String currId = curr.getId();
        Location next = this.planList.get(currInd + 1);
        String nextId = next.getId();
        return this.planList.getZooMap().getShortestPath(currId, nextId);
    }

    /*Returns the directions the user needs to get from their current location to the next one
   @return locations to next location
    */
    public String getDirectionsToNextLocation() {
        Location curr = this.planList.get(currLocationIndex);
        String currId = curr.getId();
        Location next = this.planList.get(currLocationIndex + 1);
        String nextId = next.getId();
        return this.planList.getZooMap().getTextDirections(currId, nextId);
    }

    /*Returns the directions the user needs to get from their current location to the previous one
   @return locations to previous location
    */
    public String getDirectionsToPreviousLocation() {
        Location curr = this.planList.getMyList().get(currLocationIndex);
        String currId = curr.getId();
        Location prev = this.planList.getMyList().get(currLocationIndex-1);
        String prevId = prev.getId();
        return this.planList.getZooMap().getTextDirections(currId, prevId);
    }

    /*Returns the directions the user needs to get from their current location to the next/previous one
    depending on if they're going forwards or backwards
   @return locations to next/previous location
    */
    public String getDirectionsToDestination(){
        if(forwards){
            return getDirectionsToNextLocation();
        }
        else{
            return getDirectionsToPreviousLocation();
        }
    }

    /*returns distance from current location to next location
    @return distance to next location
     */
    public double getDistanceToNextLocation(){
        return getPathToNextLocation().getWeight();
    }

    /*Returns distance to location after the next location
    @return distance to next next location
     */
    public double getDistanceToNextNextLocation(){
        return getPathToNextNextLocation().getWeight();
    }

    /*Moves the user (moves the current index indicating user's location to the next one) to the
    next location in their list
    Returns true if the user was successfully moved to the next location and false if they reached
    the end of their list
    @return whether or not user was moved to next location
     */
    public Boolean advanceLocation() {
        if (!forwards){
            this.currLocationIndex--;
            forwards = true;
            return true;
        }
        if(currLocationIndex + 1 >= planList.getMyList().size()){
            return false;
        }
        this.currLocationIndex++;
        return true;
    }

    /*Moves user to the previous location from their current one
   Returns true if user was successfully moved to the previous location and false if the user is at
   the first location in the list
    */
    public Boolean previousLocation() {
        if(forwards){
            currLocationIndex++;
            forwards = false;
            return true;
        }
        if(currLocationIndex <= 1){
            return false;
        }
        this.currLocationIndex--;
        return true;
    }

    /* Skips an exhibit in the user's list (effectively deletes the exhibit and reroutes the user)
       @return true if the exhibit was successfully skipped and false otherwise
     */
    public Boolean skip(){
        if (forwards) {
            if (currLocationIndex + 1 >= planList.planSize()) {
                return false;
            }
            planList.deleteLocation(currLocationIndex + 1);
            Sorter sorter = new Sorter();
            sorter.replan(planList,currLocationIndex);

        }
        else{
            if (currLocationIndex - 1 <= 0) {
                return false;
            }
            planList.deleteLocation(this.currLocationIndex - 1);
            currLocationIndex = currLocationIndex-1;
        }
        return true;
    }
    
    /*Returns true if the user is progressing forwards, false if backwards
    */
    public Boolean goingForwards(){
        return forwards;
    }
}
