package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NotifyIfOffTrackIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    LocationTracker locTracker;

    ExhibitDatabase testDb;
    ExhibitDao exhibitDao;

    @Rule
    public InstantTaskExecutorRule execRule = new InstantTaskExecutorRule();

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        exhibitDao = testDb.exhibitDao();
    }

    @Test
    public void updateMockLocationTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("flamingo", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Hippo", true);
            searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            Intent intent = new Intent();
            intent.putExtra(ShortestPathActivity.EXTRA_USE_LOCATION_SERVICE, false);

            pathScenario.onActivity(activity1 -> {
                Coord capuchin = new Coord(32.751128871469874, -117.16364410510093);
                activity1.mockLocation(capuchin);

            });
            pathScenario.onActivity(activity1 -> {
                TextView directions = activity1.findViewById(R.id.path_result);
                TextView nextLabel = activity1.findViewById(R.id.next_lbl);
                locTracker = activity1.locTracker;
                System.out.println(locTracker.lat);
                System.out.println(locTracker.lng);
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Flamingos"));

                //assertEquals(true, ((String) directions.getText()).contains("Capuchin"));
                assertEquals("Hippos, 240.0", nextLabel.getText());
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                nextBtn.performClick();
            });
        });
    }
}
