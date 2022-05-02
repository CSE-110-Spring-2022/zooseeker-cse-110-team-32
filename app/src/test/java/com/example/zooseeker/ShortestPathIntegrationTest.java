package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void displayDirections(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("mammal", true);
            searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchMammal = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchMammal);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchMammal = (ZooData.VertexInfo) searchView.getItemAtPosition(1);
            assertNotNull(searchMammal);
            searchView.performItemClick(searchView.getAdapter().getView(1, null, null), 1, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                TextView directions = activity1.findViewById(R.id.path_result);
                assertEquals(true, ((String) directions.getText()).contains("From: entrance exit gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: arctic foxes"));
                assertEquals(true, ((String) directions.getText()).contains("1. Walk 10.0 meters"));
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: arctic foxes"));
                assertEquals(true, ((String) directions.getText()).contains("To: elephant odyssey"));
                assertEquals(true, ((String) directions.getText()).contains("Arctic Avenue"));
                assertEquals(true, ((String) directions.getText()).contains("4. Walk 200.0 meters"));
            });


        });
    }
}
