package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* This class manages the user's planned list of exhibits
 */
public class PlanManager {
    private PlanList plan;
    private Map<String, ZooData.VertexInfo> locs;

    /* Constructor that initializes information about the zoo and the user's planned list of exhibits
       @param context = information about zoo
       @param plan = user's list of exhibits
     */
    public PlanManager(Context context, PlanList plan){
        this.plan = plan;
        this.locs = ZooData.loadVertexInfoJSON(context);
    }

    /* Adds location to an exhibit group
       @param v = information about zoo's exhibits
       @return true if location was added successfully and false otherwise
     */
    public Boolean addLocation(ZooData.VertexInfo v) {
        if (v.parent_id != null){
            String group_id = v.parent_id;
            ExhibitGroup exhibitGroup = (ExhibitGroup) plan.findExhibit(group_id);
            //Checks if this exhibit group exists yet and either creates a new exhibit group with the
            //exhibit or adds the exhibit to an already existing group
            if(exhibitGroup == null){
                ZooData.VertexInfo groupV = locs.get(group_id);
                exhibitGroup = new ExhibitGroup(groupV.id, groupV.name, groupV.lat, groupV.lng);
                exhibitGroup.addAnimal(v.id, v.name);
                return plan.addLocation(exhibitGroup);
            }
            else{
                exhibitGroup.addAnimal(v.id, v.name);
                return true;
            }
        }
        else{
            Location exhibit = new Exhibit(v.id, v.name, v.lat, v.lng);
            return plan.addLocation(exhibit);
        }
    }

    /* Gets the sorted list of user's exhibits
       @return sorted plan of user's exhibits
     */
    public PlanList getFinalPlan(){
        plan.sort();
        return plan;
    }

    /* Returns the plan without making any changes to it (ie without sorting it)
       @return user's plan
     */
    public PlanList getPlan(){
        return plan;
    }

}
