package com.example.zooseeker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sorter {
    /*Sorts PlanList by starting at the gate, then picking an Exhibit out of the unadded Exhibits
    with the shortest distance to go next, repeating until all Exhibits have been added. Also
    appends the gate at the end
     */
    public void sort(PlanList plan){
        List<Location> sortList = new ArrayList<>();
        Boolean gateAdded = checkForGate(sortList, plan);

        Location gate = new Gate("","", 0, 0);
        if(!gateAdded) gate = addGate(sortList, plan);

        while(plan.getMyList().size() > 0){
            Location curr = sortList.get(sortList.size()-1);
            //Calculates which exhibit is closest to current exhibit
            int closest = calculateClosest(curr, plan);
            //Adds closest exhibit to the sorted list
            sortList.add(plan.getMyList().get(closest));
            plan.getMyList().remove(closest);
        }
        sortList.add(gate);
        plan.setMyList(sortList);

    }

    /* Adds entrance/exit gate to beginning of sorted list and checks for instances of gate in user's plan
       @param sortedList = list of exhibits in sorted order
       @return true if gate was added, false otherwise
     */
    public boolean checkForGate(List<Location> sortedList, PlanList plan){
        Location startEnd;
        boolean gateAdded = false;
        for (int i = 0; i < plan.planSize(); i++){
            Location stop = plan.getMyList().get(i);
            if (stop.getKind() == ZooData.VertexInfo.Kind.GATE){
                startEnd = stop;
                if(!gateAdded) { sortedList.add(startEnd); }
                plan.getMyList().remove(i);
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
    public Location addGate(List<Location> sortedList, PlanList plan){
        Location gate = new Gate("","", 0, 0);
        for (Map.Entry<String, ZooData.VertexInfo> loc : plan.zooLocs.entrySet()){
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
    public int calculateClosest(Location currentLoc, PlanList plan){
        int smallestInd = 0;
        double smallestDist = Double.MAX_VALUE;
        for (int i = 0; i < plan.getMyList().size(); i++){
            double dist = plan.getZooMap().getShortestPath(currentLoc.getId(), plan.get(i).getId()).getWeight();
            if (dist < smallestDist){
                smallestDist = dist;
                smallestInd = i;
            }
        }
        return smallestInd;
    }

    /* Replans the most optimized route using the user's list of exhibits after the user skips an exhibit
       @param ind = starting index of which exhibits need to be replanned (so if ind = 2 then exhibits
       2 and on need to be replanned)
     */
    public void replan(PlanList plan, int ind){
        Location gate = plan.deleteLocation(plan.planSize()-1);
        //Creates a new list containing only the exhibits that need to be replanned

        for (int j=ind; j < plan.planSize()-1;j++){
            Location curr = plan.get(j);
            int smallestInd = j+1;
            double smallestDist = Double.MAX_VALUE;

            //Calculates distance between locations to determine which exhibit is the closest
            for (int i = j+1; i < plan.planSize(); i++){
                double dist = plan.zooMap.getShortestPath(curr.getId(), plan.get(i).getId()).getWeight();
                if (dist < smallestDist){
                    smallestDist = dist;
                    smallestInd = i;
                }
            }
            //removes the exhibits that were replanned from the old list and inserts the new exhibit order
            Location next = plan.deleteLocation(smallestInd);
            plan.addLocation(j+1, next);

        }
        plan.add(gate);
    }
}
