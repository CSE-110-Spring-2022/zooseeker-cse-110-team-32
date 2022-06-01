package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ReplanIfOffTrackUnitTest {
    PlanList plan;
    Map<String, ZooData.VertexInfo> vertices;
    List<Location> planList;
    ZooMap zooMap;
    NavigatePlannedList navList;
    LocationTracker locTracker;

    @Before
    public void loadPlan(){
        Context context = ApplicationProvider.getApplicationContext();
        zooMap = new ZooMap(context);
        plan = new PlanList(context);
        navList = new NavigatePlannedList(plan);
        vertices = ZooData.loadVertexInfoJSON(context);
        locTracker = new LocationTracker(context, plan);
        //gorilla
        locTracker.setLat(32.74812588554637);
        locTracker.setLng(-117.17565073656901);
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            ZooData.VertexInfo v = loc.getValue();
            if ((v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT) && v.parent_id == null) || v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().lat, loc.getValue().lng);
                plan.addLocation(exhibit);
            }
        }
        Sorter sorter = new Sorter();
        sorter.sort(plan);
        planList = plan.getMyList();
    }

    @Test
    public void replanAtMiddleOfPlan() {
        List<Location> alreadyVisited = new ArrayList<>();
        alreadyVisited.add(navList.getCurrentLocation());
        navList.advanceLocation();
        alreadyVisited.add(navList.getCurrentLocation());
        navList.advanceLocation();
        alreadyVisited.add(navList.getCurrentLocation());
        navList.advanceLocation();
        alreadyVisited.add(navList.getCurrentLocation());

        assertEquals(3, navList.currLocationIndex);
        Location nextLoc = plan.get(navList.currLocationIndex+1);
        int newLocInd = locTracker.aheadOfCurrentLoc(navList.currLocationIndex+1);
        assertTrue(newLocInd > navList.currLocationIndex+1);
        Location newLoc = plan.get(newLocInd);
        navList.replanOffTrack(newLocInd);

        // already visted remains unchanged
        for (int i = 0; i <= navList.currLocationIndex; i++) {
            assertEquals(alreadyVisited.get(i).getId(), planList.get(i).getId());
        }
        navList.advanceLocation();

        // Remove end gate
        planList.remove(planList.size()-1);
        // For each stop, check that distance to next stop is less than distance to all remaining stops
        for (int i= navList.currLocationIndex+1; i < planList.size()-1; i++){
            Location curr = navList.getCurrentLocation();
            double dist = navList.getDistanceToNextLocation();
            for(int j=i+2; j<planList.size(); j++){
                Location alt = planList.get(j);
                assertTrue(dist <= zooMap.getDistance(curr.getId(), alt.getId()));
            }
            navList.advanceLocation();
        }
    }



}
