package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    ExhibitDatabase testDb;
    ExhibitDao todoListItemDao;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        todoListItemDao = testDb.exhibitDao();
    }

    @Test
    public void testSearch(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Gorillas", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchGorilla = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchGorilla);
            assertEquals("Gorillas", searchGorilla.name);
            searchBar.setQuery("flamingo", true);
            ZooData.VertexInfo searchFlamingo = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFlamingo);
            assertEquals("Flamingos", searchFlamingo.name);
        });
    }

    @Test
    public void testNoResult(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Gorillo", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            assertEquals(0, searchView.getCount());
        });
    }


}
