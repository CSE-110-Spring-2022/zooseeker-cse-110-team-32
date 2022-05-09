package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class AddExhibitUnitTest {
    @Before
    public void createContext(){

    }

    @Test
    public void addExhibit(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        NavigatePlannedList navList = new NavigatePlannedList(plan);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context);
        List<Location> checkList = new ArrayList<>();
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                plan.addLocation(exhibit);
                checkList.add(exhibit);
            }
        }
        for (int i = 0; i < checkList.size(); i++){
            String id = navList.getCurrentLocation().getId();
            assertEquals(checkList.get(i).getId(), id);
            navList.advanceLocation();
        }
    }

    @Test
    public void addDuplicate(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        NavigatePlannedList navList = new NavigatePlannedList(plan);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context);
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);
        Location exhibit = new Exhibit("gators", "Alligators", tagList);
        Location duplicate = new Exhibit("gators", "alligators", tagList);
        plan.addLocation(exhibit);
        plan.addLocation(duplicate);
        assertEquals(exhibit.getId(), navList.getCurrentLocation().getId());
        assertEquals(exhibit.getName(), navList.getCurrentLocation().getName());
        assertFalse(navList.advanceLocation());
        assertEquals(exhibit.getId(), navList.getCurrentLocation().getId());
        assertNotEquals(duplicate.getName(), navList.getCurrentLocation().getName());
        assertEquals(1, plan.planSize());
    }

    @Test
    public void exhibitCount(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context);
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);
        Location exhibit = new Exhibit("gators", "Alligators", tagList);
        plan.addLocation(exhibit);
        assertEquals(1, plan.planSize());
        plan.addLocation(exhibit);
        plan.addLocation(exhibit);
        assertEquals(1, plan.planSize());
    }


}

