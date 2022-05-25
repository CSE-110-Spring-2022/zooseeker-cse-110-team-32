package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SaveItineraryTest {
    Context context = ApplicationProvider.getApplicationContext();
    private ExhibitDao dao;
    private ExhibitDatabase db;
    PlanList plan;
    Map<String, ZooData.VertexInfo> vertices;
    List<Location> planList;
    ZooMap zooMap;
    NavigatePlannedList navList;

    @Before
    public void loadPlan() {
        db = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitDao();

    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    public void savings() {
        zooMap = new ZooMap(context);
        plan = new PlanList(context);
        navList = new NavigatePlannedList(plan);
        vertices = ZooData.loadVertexInfoJSON(context);
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()) {
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
    public void loadingList(){
        savings();
        //test for duplicates
        plan.saveList(dao);
        plan.saveList(dao);
        plan.saveList(dao);
        for (Exhibit g : plan.getExhibits()) {
            System.out.println("hi" + g.Uid);
        }
        plan.saveList(dao);
        PlanList newPlan = new PlanList(context);
        newPlan.loadList(dao);

        List<Exhibit> exhibits = newPlan.getExhibits();
        List<Exhibit> expected = plan.getExhibits();

        newPlan.clearList(dao);
        System.out.println(exhibits.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), exhibits.get(i).toString());
        }
        assertEquals(exhibits.size(), expected.size());
    }
}