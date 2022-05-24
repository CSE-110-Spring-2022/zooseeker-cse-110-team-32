package com.example.zooseeker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PreviousLocationIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    @Test
    public void displayDirectionsClickBackTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("lion", true);
            searchView = activity.findViewById(R.id.search_list);
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
                assertEquals(true, ((String) directions.getText()).contains("Arctic Avenue"));
                assertEquals("Lions, 600.0", nextLabel.getText());
                Button backBtn = activity1.findViewById(R.id.back_btn);
                assertTrue(backBtn.isClickable());
                assertEquals(VISIBLE, backBtn.getVisibility());
                backBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Arctic Foxes"));
                assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("Going Backwards"));
                assertEquals(true, ((String) directions.getText()).contains("Arctic Avenue"));
            });


        });
    }

}
