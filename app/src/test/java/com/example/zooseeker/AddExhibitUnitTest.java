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
import java.util.Collection;
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
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        List<Location> checkList = new ArrayList<>();
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            if (loc.getValue().kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().tags);
                plan.addLocation(exhibit);
                checkList.add(exhibit);
            }
        }
        for (int i = 0; i < checkList.size(); i++){
            String id = plan.getCurrentLocation().getId();
            assertEquals(checkList.get(i).getId(), id);
            plan.advanceLocation();
        }
    }

    @Test
    public void addDuplicate(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);
        Location exhibit = new Exhibit("gators", "Alligators", tagList);
        Location duplicate = new Exhibit("gators", "alligators", tagList);
        plan.addLocation(exhibit);
        plan.addLocation(duplicate);
        assertEquals(exhibit.getId(), plan.getCurrentLocation().getId());
        assertEquals(exhibit.getName(), plan.getCurrentLocation().getName());
        assertFalse(plan.advanceLocation());
        assertEquals(exhibit.getId(), plan.getCurrentLocation().getId());
        assertNotEquals(duplicate.getName(), plan.getCurrentLocation().getName());
        assertEquals(1, plan.planSize());
    }


}

