package com.example.zooseeker;

import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SkipIntegrationTest {
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
    public void displayDirectionsClickSkipTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("mammal", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 1, 1);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 2, 2);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 3, 3);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 4, 4);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                System.out.println(activity1.plan.getMyList());
                TextView directions = activity1.findViewById(R.id.path_result);
                Button skipBtn = activity1.findViewById(R.id.skip_btn);
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                Button finishBtn = activity1.findViewById(R.id.finish_btn);
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Siamangs"));
                skipBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Orangutans"));
                skipBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Hippos"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Capuchin Monkeys"));
                skipBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Gorillas"));
                skipBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
                assertFalse(skipBtn.isClickable());
                finishBtn.performClick();
            });
        });
    }

    @Test
    public void skipReplan(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("flamin", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("capu", true);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("hippo", true);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("gorilla", true);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                System.out.println(activity1.plan.getMyList());
                TextView directions = activity1.findViewById(R.id.path_result);
                Button skipBtn = activity1.findViewById(R.id.skip_btn);
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                Button finishBtn = activity1.findViewById(R.id.finish_btn);
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Flamingos"));
                skipBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Capuchin Monkeys"));
                nextBtn.performClick();
                nextBtn.performClick();
                finishBtn.performClick();
            });
        });
    }
}
