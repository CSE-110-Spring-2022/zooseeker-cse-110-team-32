package com.example.zooseeker;
import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Map;


@RunWith(AndroidJUnit4.class)
public class ShortestPathActivityTest {

    @Test
    public void selectNext(){
        PlanList list = new PlanList("sample_zoo_graph.json");
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        Location foxes = new Exhibit("arctic_foxes", "Arctic Foxes", vertices.get("arctic_foxes").tags);
        Location gorillas = new Exhibit("gorillas", "Gorillas", vertices.get("gorillas").tags);
        Location gators = new Exhibit("gators", "Alligators", vertices.get("gators").tags);
        list.addLocation(foxes);
        list.addLocation(gorillas);
        list.addLocation(gators);
        ActivityScenario scenario = ActivityScenario.launch(ShortestPathActivity.class);

        //scenario.moveToState(Lifecycle.State.CREATED);
        //scenario.moveToState(Lifecycle.State.STARTED);
        //scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            ShortestPathActivity test = new ShortestPathActivity();
            test.displayTextDirections(list,test.populateMap("sample_zoo_graph.json",
                    "sample_node_info.json","sample_edge_info.json"));
            Button next = activity.findViewById(R.id.next_btn);
            assertTrue(next.isClickable());
            next.performClick();
            String expectedDirections = "From: gorillas\nTo: gators\n\n1. Walk 200.0 meters along " +
                    "Africa Rocks Street from Gorillas to Entrance Plaza\n2. Walk 100.0 meters along" +
                    " Reptile Road from Entrance Plaza to Alligators\n";
            assertEquals(expectedDirections, activity.findViewById(R.id.path_result));

        });

    }

    @Test
    public void testClickNextAtEnd(){
        PlanList list = new PlanList("sample_zoo_graph.json");
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        Location foxes = new Exhibit("arctic_foxes", "Arctic Foxes", vertices.get("arctic_foxes").tags);
        Location gorillas = new Exhibit("gorillas", "Gorillas", vertices.get("gorillas").tags);

        list.addLocation(foxes);
        list.addLocation(gorillas);

        ActivityScenario scenario = ActivityScenario.launch(ShortestPathActivity.class);

        //scenario.moveToState(Lifecycle.State.CREATED);
        //scenario.moveToState(Lifecycle.State.STARTED);
        //scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            ShortestPathActivity test = new ShortestPathActivity();
            //test.displayTextDirections(list,test.populateMap("sample_zoo_graph.json",
                    //"sample_node_info.json","sample_edge_info.json"));
            Button next = activity.findViewById(R.id.next_btn);
            assertTrue(next.isClickable());
            next.performClick();
            assertFalse(next.isClickable());


        });
    }
}
