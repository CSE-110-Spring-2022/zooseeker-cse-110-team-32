package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanManager {
    private PlanList plan;
    private Map<String, ZooData.VertexInfo> locs;
    public PlanManager(Context context, PlanList plan){
        this.plan = plan;
        this.locs = ZooData.loadVertexInfoJSON(context);
    }

    public Boolean addLocation(ZooData.VertexInfo v) {
        if (v.parent_id != null){
            String group_id = v.parent_id;
            ExhibitGroup exhibitGroup = (ExhibitGroup) plan.findExhibit(group_id);
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

    public PlanList getFinalPlan(){
        plan.sort();
        return plan;
    }

    public PlanList getPlan(){
        return plan;
    }

}
