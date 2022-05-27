package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class NumExhibitsUnitTest {

    @Test
    public void multiExhibit(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context);
        int numExhibits = 0;
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()){
            ZooData.VertexInfo v = loc.getValue();
            if ((v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT) && v.parent_id == null) || v.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP)){
                Location exhibit = new Exhibit(loc.getKey(), loc.getValue().name, loc.getValue().lat, loc.getValue().lng);
                plan.addLocation(exhibit);
                numExhibits++;
            }
        }
        assertEquals(numExhibits, plan.planSize());
    }

    @Test
    public void noExhibit(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        int numExhibits = 0;
        assertEquals(numExhibits, plan.planSize());
    }

    @Test
    public void oneExhibit(){
        Context context = ApplicationProvider.getApplicationContext();
        PlanList plan = new PlanList(context);
        Location exhibit = new Exhibit("lions", "Lions", 0, 0);
        int numExhibits = 1;
        plan.addLocation(exhibit);
        assertEquals(numExhibits, plan.planSize());
    }
}
