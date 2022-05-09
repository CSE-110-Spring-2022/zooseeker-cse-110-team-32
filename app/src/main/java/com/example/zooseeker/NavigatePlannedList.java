package com.example.zooseeker;

import org.jgrapht.GraphPath;

public class NavigatePlannedList {
    private PlanList planList;
    private int currLocationIndex;
    NavigatePlannedList(PlanList pl){
        this.planList = pl;
        this.currLocationIndex = 0;
    }

    /*tells whether the end of the exhibit has been reached
   returns true if the user is at the end of the exhibit and returns false otherwise
   @return whether or not user is at end of their list
    */
    public Boolean endReached(){
        if ( this.currLocationIndex== this.planList.planSize()-2){
            return true;
        }
        return false;
    }

    /*Return user's current location
   @return user's current location
    */
    public Location getCurrentLocation() {
        return this.planList.getMyList().get(this.currLocationIndex);
    }

    /*returns the next location in planned list of exhibits
    If user has reached the end of their list of exhibits, return null
    @returns the next location in list or null if there aren't any
     */
    public Location getNextLocation() {
        if (this.currLocationIndex + 1 < this.planList.planSize()){
            return this.planList.getMyList().get(this.currLocationIndex+1);
        }
        return null;
    }

    /*
        This method is to be implemented after the merge with the shortest path branch
        it will use zooMap object to get the shortest path from current location to the
        next location in the list
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPathToNextLocation() {
        Location curr = this.planList.getMyList().get(this.currLocationIndex);
        String currId = curr.getId();
        Location next = this.planList.getMyList().get(this.currLocationIndex + 1);
        String nextId = next.getId();
        return this.planList.getZooMap().getShortestPath(currId, nextId);
    }

    /*Returns the directions the user needs to get from their current location to the next one
   @return locations to next location
    */
    public String getDirectionsToNextLocation() {
        Location curr = this.planList.getMyList().get(this.currLocationIndex);
        String currId = curr.getId();
        Location next = this.planList.getMyList().get(this.currLocationIndex + 1);
        String nextId = next.getId();
        return this.planList.getZooMap().getTextDirections(currId, nextId);
    }

    /*Returns distance from current location to next location in user's list
    @return distance between user's location and next planned location
     */
    public double getDistanceToNextLocation(){
        return getPathToNextLocation().getWeight();
    }


    /*Moves the user (moves the current index indicating user's location to the next one) to the
    next location in their list
    Returns true if the user was successfully moved to the next location and false if they reached
    the end of their list
    @return whether or not user was moved to next location
     */
    public Boolean advanceLocation() {
        if(endReached()){
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
        if(this.currLocationIndex < 1){
            return false;
        }
        this.currLocationIndex--;
        return true;
    }
}
