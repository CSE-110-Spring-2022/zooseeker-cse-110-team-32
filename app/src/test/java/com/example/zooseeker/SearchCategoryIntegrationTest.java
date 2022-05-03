package com.example.zooseeker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.widget.ListView;
import android.widget.SearchView;

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
public class SearchCategoryIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

    @Test
    public void searchByTagTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("mammal", true);
            ListView searchView = activity.findViewById(R.id.search_list);

            assertEquals(4, searchView.getCount());

            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            RecyclerView displayView = activity.recyclerView;
            DisplayListAdapter displayAdapter = (DisplayListAdapter) displayView.getAdapter();
            //Should be 2 even though we only added one thing because it includes starting at entrance
            assertEquals(2, displayAdapter.getItemCount());
        });
    }

    @Test
    public void testIncompleteCategoryName(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("mamm", true);
            ListView searchView = activity.findViewById(R.id.search_list);

            assertEquals(4, searchView.getCount());

            searchBar.setQuery("mammal", true);
            ListView searchViewMammal = activity.findViewById(R.id.search_list);

            assertEquals(searchView.getCount(), searchViewMammal.getCount());

        });
    }
}
