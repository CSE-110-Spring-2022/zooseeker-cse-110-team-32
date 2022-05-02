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

@RunWith(AndroidJUnit4.class)
public class SearchIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

    @Test
    public void testSearch(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Lions", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchLion = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchLion);
            assertEquals("Lions", searchLion.name);
            searchBar.setQuery("fox", true);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFox);
            assertEquals("Arctic Foxes", searchFox.name);
        });
    }
}
