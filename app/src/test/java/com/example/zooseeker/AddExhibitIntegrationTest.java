package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AddExhibitIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

    @Test
    public void testAdd(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Arctic Foxes", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            assertEquals("Arctic Foxes", searchFox.name);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            RecyclerView displayView = activity.recyclerView;
            DisplayListAdapter displayAdapter = (DisplayListAdapter) displayView.getAdapter();
            assertEquals(2, displayAdapter.getItemCount());
            List<DisplayListItem> locations = displayAdapter.getDisplayItems();
            assertEquals("Entrance and Exit Gate", locations.get(0).getName());
            assertEquals("Arctic Foxes", locations.get(1).getName());

            searchBar.setQuery("gator", true);
            searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchGator = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchGator);
            assertEquals("Alligators", searchGator.name);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            assertEquals(3, displayAdapter.getItemCount());
            locations = displayAdapter.getDisplayItems();
            assertEquals("Alligators", locations.get(2).getName());
        });
    }

    @Test
    public void testAddDuplicate(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            assertEquals("Arctic Foxes", searchFox.name);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            RecyclerView displayView = activity.recyclerView;
            DisplayListAdapter displayAdapter = (DisplayListAdapter) displayView.getAdapter();
            assertEquals(2, displayAdapter.getItemCount());
            List<DisplayListItem> locations = displayAdapter.getDisplayItems();
            assertEquals("Entrance and Exit Gate", locations.get(0).getName());
        });
    }


}
