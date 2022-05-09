package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SortPlanUnitTest {
    PlanList plan;
    Map<String, ZooData.VertexInfo> vertices;
    List<Location> planList;
    ZooMap zooMap;
    NavigatePlannedList navList;

    @Before
    public void loadPlan(){
        Context context = ApplicationProvider.getApplicationContext();
        zooMap = new ZooMap(context);
        plan = new PlanList(context);
        navList = new NavigatePlannedList(plan);
        vertices = ZooData.loadVertexInfoJSON(context);
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                plan.addLocation(exhibit);
            }
        }
        plan.sort();
        planList = plan.getMyList();
    }

    @Test
    public void gateStartEnd(){
        Location gate = new Gate("", "", new ArrayList<>());
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.GATE)){
                gate = new Gate(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                break;
            }
        }
        assertNotEquals("", gate.getId());
        for (int i=0; i < planList.size(); i++){
            if(i==0 || i==planList.size()-1){
                // Check that start and end locations are gates
                assertEquals(gate.getId(), planList.get(i).getId());
            }
            else{
                // Check that no other locations in between are gates
                assertNotEquals(gate.getId(), planList.get(i).getId());
            }
        }
    }

    @Test
    public void pathDistance() {
        // Remove end gate
        planList.remove(planList.size()-1);
        // For each stop, check that distance to next stop is less than distance to all remaining stops
        for (int i=0; i < planList.size()-1; i++){
            Location curr = planList.get(i);
            double dist = navList.getDistanceToNextLocation();
            for(int j=i+2; j<planList.size(); j++){
                Location alt = planList.get(j);
                assertTrue(dist <= zooMap.getDistance(curr.getId(), alt.getId()));
            }
            navList.advanceLocation();
        }
    }

}
