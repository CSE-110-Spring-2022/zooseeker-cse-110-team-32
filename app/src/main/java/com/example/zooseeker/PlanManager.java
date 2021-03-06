package com.example.zooseeker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanManager {
    private PlanList plan;
    private Map<String, ZooData.VertexInfo> locs;
    private Sorter sorter;

    public PlanManager(Context context, PlanList plan){
        this.plan = plan;
        this.locs = ZooData.loadVertexInfoJSON(context);
        this.sorter = new Sorter();
    }

    public Boolean addLocation(ZooData.VertexInfo v) {
        if (v.parent_id != null){
            String group_id = v.parent_id;
            ExhibitGroup exhibitGroup = (ExhibitGroup) plan.findExhibit(group_id);
            ZooData.VertexInfo groupV = locs.get(group_id);
            if(exhibitGroup == null){
                exhibitGroup = new ExhibitGroup(groupV.id, groupV.name, groupV.lat, groupV.lng);
                Exhibit ex = new Exhibit(v.id, v.name, groupV.lat, groupV.lng, group_id, groupV.name);
                exhibitGroup.addAnimal(v.id, ex);
                return plan.addLocation(exhibitGroup);
            }
            else {
                Exhibit ex = new Exhibit(v.id, v.name, groupV.lat, groupV.lng, group_id, groupV.name);
                exhibitGroup.addAnimal(v.id, ex);
                return true;
            }
        }
        else if (v.kind == ZooData.VertexInfo.Kind.EXHIBIT){
            Location exhibit = new Exhibit(v.id, v.name, v.lat, v.lng);
            return plan.addLocation(exhibit);
        }
        return false;
    }

    public PlanList getFinalPlan(){
        sorter.sort(plan);
        return plan;
    }

    public PlanList getPlan(){
        return plan;
    }

}
