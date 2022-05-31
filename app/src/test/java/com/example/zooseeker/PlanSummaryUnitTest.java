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
import java.util.Collection;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class PlanSummaryUnitTest {

    @Test
    public void testDirectRouteSummary() {
        Context context = ApplicationProvider.getApplicationContext();
        PlanList planList = new PlanList(context);

        Location gate = new Intersection("intxn_front_treetops", "Front Street / Treetops Way",32.735546539459556,-117.1521136981983,new ArrayList<>());
        planList.addLocation(gate);
        assertEquals(1, planList.planSize());
        Sorter sorter = new Sorter();
        sorter.sort(planList);
        NavigatePlannedList navList = new NavigatePlannedList(planList);
        PlanSummaryActivity summary = new PlanSummaryActivity();
        assertTrue(summary.createExhibitsList(navList).get(0).getName().contains("10.0"));
    }

    @Test
    public void testIndirectRouteSummary() {
        Context context = ApplicationProvider.getApplicationContext();
        PlanList planList = new PlanList(context);

        Location koi = new Exhibit("koi", "Koi Fish",32.72211788245888,-117.15794384136309);
        planList.addLocation(koi);
        assertEquals(1, planList.planSize());
        Sorter sorter = new Sorter();
        sorter.sort(planList);
        NavigatePlannedList navList = new NavigatePlannedList(planList);
        PlanSummaryActivity summary = new PlanSummaryActivity();
        assertTrue(summary.createExhibitsList(navList).get(0).getName().contains("60.0"));
    }
}
