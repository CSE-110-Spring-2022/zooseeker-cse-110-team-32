package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.jgrapht.GraphPath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortPlanUnitTest {
    @Test
    public void allExhibits() {
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context);
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                plan.addLocation(exhibit);
            }
            else if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.GATE)){
                Location gate = new Gate(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                plan.addGate(gate);
            }
        }
        plan.sort();
        for (int i=0; i < plan.planSize()-1; i++){

        }
    }

}
