package com.example.zooseeker;

import org.jgrapht.GraphPath;

/*This class navigates through the user's list of planned exhibits and gives directions to each exhibit
starting from the entrance gate
 */
public class NavigatePlannedList {

    private PlanList planList;
    private int currLocationIndex;

    /*Constructor that sets the PlanList to use based on what's passed in
    @param plan = plan to navigate through
     */
    NavigatePlannedList(PlanList plan){
        this.planList = plan;
        this.currLocationIndex = 0;

    }

    /*tells whether the end of the exhibit has been reached
   returns true if the user is at the end of the exhibit and returns false otherwise
   @return whether or not user is at end of their list
    */
    public Boolean endReached(){
        if (currLocationIndex == this.planList.planSize()-2){
            return true;
        }
        return false;
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
        if(currLocationIndex+1 >= planList.planSize()){
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
        if(currLocationIndex < 1){
            return false;
        }
        this.currLocationIndex--;
        return true;
    }

    public Boolean skip(){
        if(currLocationIndex+1 >= planList.planSize()){
            return false;
        }
        this.planList.deleteLocation(this.currLocationIndex+1);
        return true;
    }
}
