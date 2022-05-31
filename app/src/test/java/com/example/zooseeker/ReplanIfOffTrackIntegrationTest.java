package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.room.Room;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReplanIfOffTrackIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    ExhibitDatabase testDb;
    ExhibitDao exhibitItemDao;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        exhibitItemDao = testDb.exhibitDao();
    }

    @Test
    public void displayDirectionsClickSkipTest() {
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
            searchBar.setQuery("flamingos", true);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            Button planBtn = activity.findViewById(R.id.plan_btn);
            planBtn.performClick();
            ActivityScenario<ShortestPathActivity> pathScenario = ActivityScenario.launch(ShortestPathActivity.class);
            pathScenario.moveToState(Lifecycle.State.CREATED);
            pathScenario.onActivity(activity1 -> {
                System.out.println(activity1.plan.getMyList());
                TextView directions = activity1.findViewById(R.id.path_result);
                Button backBtn = activity1.findViewById(R.id.back_btn);
                Button skipBtn = activity1.findViewById(R.id.skip_btn);
                Button nextBtn = activity1.findViewById(R.id.next_btn);
                EditText lat = activity1.findViewById(R.id.lat);
                EditText lng = activity1.findViewById(R.id.lng);
                Button mock = activity1.findViewById(R.id.mock);

                assertEquals(true, ((String) directions.getText()).contains("From: Entrance and Exit Gate"));
                assertEquals(true, ((String) directions.getText()).contains("To: Flamingos"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Flamingos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Capuchin Monkeys"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Capuchin Monkeys"));
                assertEquals(true, ((String) directions.getText()).contains("To: Hippos"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Orangutans"));

                lat.setText("32.74812588554637");
                lng.setText("-117.17565073656901");
                mock.performClick();

                /*
                activity1.getLastAlertDialog().performClick();

                Button replan = alert.getButton(-1);
                replan.performClick();

                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Gorillas"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Gorillas"));
                assertEquals(true, ((String) directions.getText()).contains("To: Orangutans"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Orangutans"));
                assertEquals(true, ((String) directions.getText()).contains("To: Siamangs"));
                nextBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Siamangs"));
                assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
                assertFalse(skipBtn.isClickable());

                //tests that already visited exhibits remain unchanged
                backBtn.performClick();
                backBtn.performClick();
                backBtn.performClick();
                backBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("To: Capuchin Monkeys"));
                assertEquals(true, ((String) directions.getText()).contains("From: Hippos"));
                backBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Capuchin Monkeys"));
                assertEquals(true, ((String) directions.getText()).contains("To: Flamingos"));
                backBtn.performClick();
                assertEquals(true, ((String) directions.getText()).contains("From: Flamingos"));
                assertEquals(true, ((String) directions.getText()).contains("To: Entrance and Exit Gate"));
*/
            });
        });
    }
}
