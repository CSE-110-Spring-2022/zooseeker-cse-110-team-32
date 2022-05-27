package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

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
public class PreviousLocationUnitTest {
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
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().lat, loc.getValue().lng);
                plan.addLocation(exhibit);
            }
        }
        plan.sort();
        planList = plan.getMyList();
    }

    @Test
    public void reverse(){
        List<Location> locs = new ArrayList<>();
        while(!navList.endReached()){
            locs.add(navList.getCurrentLocation());
            navList.advanceLocation();
        }
        navList.previousLocation();
        for(int i=locs.size()-1; i >0; i--){
            assertEquals(locs.get(i), navList.getCurrentLocation());
            navList.previousLocation();
        }
    }
}
