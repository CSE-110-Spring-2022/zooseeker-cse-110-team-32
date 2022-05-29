package com.example.zooseeker;

import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class DetailedDirectionsIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    ExhibitDatabase testDb;
    ExhibitDao todoListItemDao;
    ZooMap zooMap;
    Graph<String, IdentifiedWeightedEdge> graph;
    Map<String, ZooData.VertexInfo> locVertices;
    Map<String, ZooData.EdgeInfo> roadEdges;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        todoListItemDao = testDb.exhibitDao();
    }

    @Before
    public void zooInfo(){
        Context context = ApplicationProvider.getApplicationContext();
        zooMap = new ZooMap(context);
        graph = ZooData.loadZooGraphJSON(context);
        locVertices = ZooData.loadVertexInfoJSON(context);
        roadEdges = ZooData.loadEdgeInfoJSON(context);
    }

    @Test
    public void detailedDirections(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("flamingo", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFlamingo = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFlamingo);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Hippo", true);
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
                SwitchCompat directionsToggle = activity1.findViewById(R.id.directions_switch);
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                directionsToggle.performClick();
                assertTrue(((String) directions.getText()).contains(zooMap.getDetailedTextDirections("entrance_exit_gate", "flamingo")));
                nextBtn.performClick();
                assertTrue(((String) directions.getText()).contains(zooMap.getDetailedTextDirections("flamingo", "hippo")));
                nextBtn.performClick();
                assertTrue(((String) directions.getText()).contains(zooMap.getDetailedTextDirections("hippo", "entrance_exit_gate")));
            });
        });
    }

    @Test
    public void briefDirections(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("flamingo", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFlamingo = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            assertNotNull(searchFlamingo);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Hippo", true);
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
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                assertTrue(((String) directions.getText()).contains(zooMap.getBriefTextDirections("entrance_exit_gate", "flamingo")));
                nextBtn.performClick();
                assertTrue(((String) directions.getText()).contains(zooMap.getBriefTextDirections("flamingo", "hippo")));
                nextBtn.performClick();
                assertTrue(((String) directions.getText()).contains(zooMap.getBriefTextDirections("hippo", "entrance_exit_gate")));
            });
        });
    }

}
