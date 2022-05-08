package com.example.zooseeker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ShortestPathIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    @Test
    public void displayDirectionsClickNextTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Elephant", true);
            searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchMammal = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchMammal);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                TextView directions = activity1.findViewById(R.id.path_result);
                TextView nextLabel = activity1.findViewById(R.id.next_lbl);
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Arctic Foxes"));
                assertEquals(true, ((String) directions.getText()).contains("1. Walk 10.0 meters"));
                assertEquals("Elephant Odyssey, 800.0", nextLabel.getText());
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                assertTrue(nextBtn.isClickable());
                assertEquals(VISIBLE, nextBtn.getVisibility());
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Arctic Foxes"));
                assertEquals(true, ((String) directions.getText()).contains("To: Elephant Odyssey"));
                assertEquals(true, ((String) directions.getText()).contains("Arctic Avenue"));
                assertEquals(true, ((String) directions.getText()).contains("4. Walk 200.0 meters"));
                assertEquals("Entrance and Exit Gate, 510.0", nextLabel.getText());

            });


        });
    }

    @Test
    public void EndOfExhibitListTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Elephant", true);
            searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchMammal = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchMammal);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                TextView directions = activity1.findViewById(R.id.path_result);
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                TextView nextLabel = activity1.findViewById(R.id.next_lbl);
                assertTrue(nextBtn.isClickable());
                assertEquals(VISIBLE, nextBtn.getVisibility());
                nextBtn.performClick();
                nextBtn.performClick();
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
                assertEquals(GONE, nextLabel.getVisibility());
                assertFalse(nextBtn.isClickable());
                assertEquals(GONE, nextBtn.getVisibility());
            });


        });
    }
}
