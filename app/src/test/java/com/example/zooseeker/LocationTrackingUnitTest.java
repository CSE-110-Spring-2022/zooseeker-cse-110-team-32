package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
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
        Sorter sorter = new Sorter();
        sorter.sort(plan);
        GraphPath<String, IdentifiedWeightedEdge> path = navList.getPlanPath(0);
        List<String> stops = path.getVertexList();
        // gate: 32.73561, -117.14936
        // intxn_front_treetops: 32.735546539459556, -117.1521136981983
        // intxn_front_lagoon_1: 32.72726737662313, -117.15496383006723
        // koi: 32.72211788245888, -117.15794384136309
        for(String stop: stops){
            System.out.println(stop);
        }
        // capuchin coords
        locTracker.setLat(32.751128871469874);
        locTracker.setLng(-117.16364410510093);
        GraphPath<String, IdentifiedWeightedEdge> newPath = locTracker.getReroute(path);
        assertEquals("capuchin", newPath.getStartVertex());
        assertEquals("koi", newPath.getEndVertex());
    }

    @Test
    public void noRerouteCloseLocation(){
        Sorter sorter = new Sorter();
        sorter.sort(plan);
        navList.advanceLocation();
        GraphPath<String, IdentifiedWeightedEdge> path = navList.getPlanPath(0);
        assertEquals("koi", path.getStartVertex());
        assertEquals("flamingo", path.getEndVertex());
        List<String> stops = path.getVertexList();
        // koi: 32.72211788245888, -117.15794384136309
        for(String stop: stops){
            System.out.println(stop);
        }
        // intxn_front_lagoon_1 coords
        locTracker.setLat(32.72726737662313);
        locTracker.setLng(-117.15496383006723);
        GraphPath<String, IdentifiedWeightedEdge> newPath = locTracker.getReroute(path);
        assertEquals("koi", newPath.getStartVertex());
        assertEquals("flamingo", newPath.getEndVertex());
    }

    @Test
    public void noRerouteCloseStreet(){
        Sorter sorter = new Sorter();
        sorter.sort(plan);
        navList.advanceLocation();
        GraphPath<String, IdentifiedWeightedEdge> path = navList.getPlanPath(0);
        assertEquals("koi", path.getStartVertex());
        assertEquals("flamingo", path.getEndVertex());
        List<String> stops = path.getVertexList();
        // koi: 32.72211788245888, -117.15794384136309
        for(String stop: stops){
            System.out.println(stop);
        }
        // halfway from koi to intxn_front_lagoon_1 coords
        double koiLng = -117.15794384136309;
        double koiLat = 32.72211788245888;
        double ixnFrontLagoon1Lng = -117.15496383006723;
        double ixnFrontLagoon1Lat = 32.72726737662313;
        double halfwayLng = (koiLng + ixnFrontLagoon1Lng) / 2.0;
        double halfwayLat = (koiLat + ixnFrontLagoon1Lat) / 2.0;
        locTracker.setLat(halfwayLat);
        locTracker.setLng(halfwayLng);
        GraphPath<String, IdentifiedWeightedEdge> newPath = locTracker.getReroute(path);
        assertEquals("koi", newPath.getStartVertex());
        assertEquals("flamingo", newPath.getEndVertex());
    }

}
