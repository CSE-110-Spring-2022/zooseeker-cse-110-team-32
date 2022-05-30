package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SkipUnitTest {
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
            ZooData.VertexInfo v = loc.getValue();
            if ((v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT) && v.parent_id == null) || v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().lat, loc.getValue().lng);
                plan.addLocation(exhibit);
            }
        }
        plan.sort();
        planList = plan.getMyList();
    }

    @Test
    public void skipForwards(){
        Location gate = navList.getCurrentLocation();
        Location skip = navList.getNextLocation();
        int origSize = plan.planSize();
        navList.skip();
        assertEquals(origSize-1, plan.planSize());
        assertEquals(gate, navList.getCurrentLocation());
        assertFalse(planList.contains(skip));

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

    @Test
    public void skipBackwards(){
        navList.advanceLocation();
        navList.previousLocation();
        Location curr = navList.getCurrentLocation();
        Location skip = navList.getDestination();
        int origSize = plan.planSize();
        navList.skip();
        assertEquals(origSize-1, plan.planSize());
        assertEquals(curr, navList.getCurrentLocation());
        assertFalse(planList.contains(skip));
    }


}
