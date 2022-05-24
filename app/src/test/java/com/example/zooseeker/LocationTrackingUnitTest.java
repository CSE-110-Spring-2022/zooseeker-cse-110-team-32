package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class LocationTrackingUnitTest {
    PlanList plan;
    NavigatePlannedList navList;
    LocationTracker locTracker;
    Map<String, ZooData.VertexInfo> vertices;
    @Before
    public void createContext(){
        Context context = ApplicationProvider.getApplicationContext();
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
        locTracker = new LocationTracker(context, plan);
    }

    @Test
    public void streetDistance(){
        double fromx = 2.0;
        double fromy = 2.0;
        double tox = 8.0;
        double toy = 2.0;
        double x = 4.0;
        double y = 4.0;
        double dist = locTracker.distToStreet(x, y, fromx, fromy, tox, toy);
        assertEquals(2.0, dist, 0.0001);
    }

    @Test
    public void streetDistanceObtuse(){
        double fromx = 4.0;
        double fromy = 4.0;
        double tox = 8.0;
        double toy = 2.0;
        double x = 2.0;
        double y = 2.0;
        double dist = locTracker.distToStreet(x, y, fromx, fromy, tox, toy);
        assertEquals(Math.sqrt(8.0), dist, 0.0001);
    }

    @Test
    public void reroute(){
        GraphPath<String, IdentifiedWeightedEdge> path = navList.getPlanPath(0);
        System.out.println(path.getStartVertex());
        System.out.println(path.getEndVertex());

    }

}
