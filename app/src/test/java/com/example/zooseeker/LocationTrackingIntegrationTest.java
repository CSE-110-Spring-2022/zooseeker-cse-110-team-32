package com.example.zooseeker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class LocationTrackingIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    LocationTracker locTracker;

    ExhibitDatabase testDb;
    ExhibitDao todoListItemDao;

    @Rule
    public InstantTaskExecutorRule execRule = new InstantTaskExecutorRule();

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        todoListItemDao = testDb.exhibitDao();
    }

    @Test
    public void noRerouteTest(){
        scenario.moveToState(Lifecycle.State.STARTED);

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

            Intent intent = new Intent(activity, ShortestPathActivity.class);
            intent.putExtra(ShortestPathActivity.EXTRA_USE_LOCATION_SERVICE, false);
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(intent);
            pathScenario.moveToState(Lifecycle.State.STARTED);

            pathScenario.onActivity(activity1 -> {
                double gateLng = -117.14936;
                double gateLat = 32.73561;
                double ixnFrontTreetopsLng = -117.1521136981983;
                double ixnFrontTreetopsLat = 32.735546539459556;
                double halfwayLng = (gateLng + ixnFrontTreetopsLng) / 2.0;
                double halfwayLat = (gateLat + ixnFrontTreetopsLat) / 2.0;
                Coord halfway = new Coord(halfwayLat, halfwayLng);
                activity1.mockLocation(halfway);

            });
            pathScenario.onActivity(activity1 -> {
                TextView directions = activity1.findViewById(R.id.path_result);
                TextView nextLabel = activity1.findViewById(R.id.next_lbl);
                locTracker = activity1.locTracker;
                NavigatePlannedList navList = activity1.navList;
                String oldDirections = navList.getPlanList().getZooMap().getTextDirections("entrance_exit_gate", "flamingo");
                System.out.println(locTracker.lat);
                System.out.println(locTracker.lng);
                System.out.println(directions.getText());
                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Flamingos"));
                assertEquals(true, ((String) directions.getText()).contains(oldDirections));
                assertEquals("Hippos, 240.0", nextLabel.getText());
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                nextBtn.performClick();
            });
        });
    }

    @Test
    public void mockLocationRerouteTest(){
        scenario.moveToState(Lifecycle.State.STARTED);

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

            Intent intent = new Intent(activity, ShortestPathActivity.class);
            intent.putExtra(ShortestPathActivity.EXTRA_USE_LOCATION_SERVICE, false);
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(intent);
            pathScenario.moveToState(Lifecycle.State.STARTED);

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
                assertEquals(true, ((String) directions.getText()).contains("Capuchin"));
                assertEquals("Hippos, 240.0", nextLabel.getText());
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                nextBtn.performClick();
            });
        });
    }

}
