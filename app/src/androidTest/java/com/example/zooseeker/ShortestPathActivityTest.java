package com.example.zooseeker;import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class ShortestPathActivityTest {

    @Test
    public void selectNext(){

        PlanList list = new PlanList("josn");
        List<String> tags = new ArrayList<String>();
        tags.addAll(Arrays.asList("enter",
                "leave",
                "start",
                "begin",
                "entrance",
                "exit"));
        Location entrance = new Gate("entrance_exit_gate","Entrance and Exit Gate",tags);
        list.addLocation(entrance);
        List<String> tags2 = new ArrayList<String>();
        tags2.addAll(Arrays.asList("gorilla",
                "monkey",
                "ape",
                "mammal"));
        Location gorillas = new Exhibit("Gorillas","Gorillas", tags2);

        List<String> tags3 = new ArrayList<String>();
        tags3.addAll(Arrays.asList("alligator",
                "reptile",
                "gator"));
        Location gators = new Exhibit("gators", "Alligators", tags3);
        assertTrue(list.addLocation(gorillas));
        //list.addLocation(gorillas);
        list.addLocation(gators);
        //ActivityScenario scenario = ActivityScenario.launch(ShortestPathActivity.class);
        //assertEquals(3, list.planSize());
        //scenario.moveToState(Lifecycle.State.CREATED);
        //scenario.moveToState(Lifecycle.State.STARTED);
        //scenario.moveToState(Lifecycle.State.RESUMED);

       /* scenario.onActivity(activity -> {
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

        });*/

    }

    @Test
    public void testClickNextAtEnd(){
        /*ExhibitsList list = new ExhibitsList();
        Exhibit plaza = new Exhibit("entrance_plaza");
        Exhibit gorillas = new Exhibit("gorillas");

        list.addExhibit(plaza);
        list.addExhibit(gorillas);

        int num = 0;
        assertEquals(1,num);
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


        });*/
    }
}

