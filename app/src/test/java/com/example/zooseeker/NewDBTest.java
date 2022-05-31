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
public class NewDBTest {
    Context context = ApplicationProvider.getApplicationContext();
    private ExhibitDao dao;
    private ExhibitDatabase db;
    PlanList plan;
    Map<String, ZooData.VertexInfo> vertices;
    List<Location> planList;
    PlanManager helper;
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

    public void populateList() {
        zooMap = new ZooMap(context);
        plan = new PlanList(context);
        navList = new NavigatePlannedList(plan);
        vertices = ZooData.loadVertexInfoJSON(context);
        helper = new PlanManager(context, plan);
        for (Map.Entry<String, ZooData.VertexInfo> loc : vertices.entrySet()) {
            ZooData.VertexInfo v = loc.getValue();
            helper.addLocation(v);
        }
        plan.sort();
        planList = plan.getMyList();
    }


    @Test
    public void loadingList(){
        populateList();
        plan.printList();
        //test for duplicates
        plan.saveList(dao);
        plan.saveList(dao);
        plan.saveList(dao);
        //plan.printList();
        //plan.saveList(dao);
        PlanList newPlan = new PlanList(context);
        //System.out.println("start");
        newPlan.loadList(dao);
        //System.out.println("fini");
        newPlan.printList();
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