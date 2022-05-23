package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(AndroidJUnit4.class)
public class NumExhibitsIntegrationTest {
    ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

    @Test
    public void nonzeroExhibits(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Flamingos", true);
            ListView searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            TextView numExhibitsView = activity.findViewById(R.id.exhibits_num);
            String numExhibitsStr = (String) numExhibitsView.getText();
            Pattern numPattern = Pattern.compile("\\d+");
            Matcher numMatcher = numPattern.matcher(numExhibitsStr);
            assertTrue(numMatcher.find());
            assertEquals(1, (int) Integer.valueOf(numMatcher.group(0)));


            searchBar.setQuery("ape", true);
            searchView = activity.findViewById(R.id.search_list);
            searchView.performItemClick(searchView.getAdapter().getView(0, null, null), 0, 0);
            numExhibitsStr = (String) numExhibitsView.getText();
            numMatcher = numPattern.matcher(numExhibitsStr);
            assertTrue(numMatcher.find());
            assertEquals(2, (int) Integer.valueOf(numMatcher.group(0)));
        });
    }

    @Test
    public void zeroExhibits(){
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setQuery("Flamingos", true);
            TextView numExhibitsView = activity.findViewById(R.id.exhibits_num);
            String numExhibitsStr = (String) numExhibitsView.getText();
            Pattern numPattern = Pattern.compile("\\d+");
            Matcher numMatcher = numPattern.matcher(numExhibitsStr);
            if (!numMatcher.find()){
                assertTrue(true);
            }
            else{
                assertEquals(0, (int) Integer.valueOf(numMatcher.group(0)));
            }
        });
    }


}
