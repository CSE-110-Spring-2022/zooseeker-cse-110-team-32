package com.example.zooseeker;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
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
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class RouteSummaryIntegrationTest {
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
    public void NoExhibitsSelectedTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("bali", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchBird = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchBird);
            Button planBtn1 = activity.findViewById(R.id.plan_btn);

            assertEquals(GONE, planBtn1.getVisibility());

        });
    }

    @Test
    public void ExhibitsSelectedTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("bali", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchBird = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchBird);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("koi", true);
            searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchKoi = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchKoi);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);

            Button planBtn = activity.findViewById(R.id.plan_btn);
            assertTrue(planBtn.isClickable());
            assertEquals(VISIBLE, planBtn.getVisibility());
            planBtn.performClick();
            ActivityScenario<PlanSummaryActivity> summaryScenario = ActivityScenario.launch(PlanSummaryActivity.class);
            summaryScenario.moveToState(Lifecycle.State.CREATED);
            summaryScenario.onActivity(activity1 -> {
                RecyclerView displayView = activity1.recyclerView;
                DisplayListAdapter displayAdapter = (DisplayListAdapter) displayView.getAdapter();
                List<DisplayListItem> locations = displayAdapter.getDisplayItems();
                assertEquals(2, displayAdapter.getItemCount());
                assertTrue(locations.get(0).toString().contains("Koi Fish"));
                assertTrue(locations.get(0).toString().contains("60.0"));
                assertTrue(locations.get(1).toString().contains("Owens Aviary"));
                assertTrue(locations.get(1).toString().contains("230.0"));

                Button directionsBtn = activity1.findViewById(R.id.directions_btn);
                directionsBtn.performClick();
                ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
                pathScenario.moveToState(Lifecycle.State.CREATED);
                pathScenario.onActivity(activity2 ->{
                    TextView directions = activity2.findViewById(R.id.path_result);
                    Button nextBtn = activity2.findViewById(R.id.next_btn);
                    TextView nextLabel = activity2.findViewById(R.id.next_lbl);
                    assertTrue(nextBtn.isClickable());
                    assertEquals(VISIBLE, nextBtn.getVisibility());
                    nextBtn.performClick();
                    nextBtn.performClick();
                    assertFalse(nextBtn.isClickable());
                    assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
                    assertEquals(GONE, nextLabel.getVisibility());
                    assertFalse(nextBtn.isClickable());
                    assertEquals(GONE, nextBtn.getVisibility());
                });

            });


        });
    }
}