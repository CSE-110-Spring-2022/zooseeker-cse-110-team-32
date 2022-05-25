package com.example.zooseeker;

import static android.view.View.GONE;
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(AndroidJUnit4.class)
public class EmptyListIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
    ExhibitDatabase testDb;
    ExhibitDao todoListItemDao;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries().build();

        ExhibitDatabase.injectExhibitDatabase(testDb);

        //List<Exhibit> todos = Exhibit.loadJSON(context, "demo_todos.json");
        todoListItemDao = testDb.exhibitDao();
        //todoListItemDao.insertAll(todos);
    }

    @Test
    public void clearNonEmptyListTest(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("fox", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            ZooData.VertexInfo searchFox = (ZooData.VertexInfo) searchView.getItemAtPosition(0);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            searchBar.setQuery("Elephant", true);
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
                assertTrue(nextBtn.isClickable());
                assertEquals(VISIBLE, nextBtn.getVisibility());
                nextBtn.performClick();

                assertTrue(nextBtn.isClickable());
                assertEquals(VISIBLE, nextBtn.getVisibility());
                nextBtn.performClick();

                Button finishBtn = activity1.findViewById(R.id.finish_btn);
                assertFalse(nextBtn.isClickable());
                assertEquals(GONE, nextBtn.getVisibility());

                assertTrue(finishBtn.isClickable());
                assertEquals(VISIBLE, finishBtn.getVisibility());

                assertEquals(2, activity.getDao().getAll().size());

                finishBtn.performClick();

                assertEquals(0, activity.getDao().getAll().size());

            });


        });
    }

}