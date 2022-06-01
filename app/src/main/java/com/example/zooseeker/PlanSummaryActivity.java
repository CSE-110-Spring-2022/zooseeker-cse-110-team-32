package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/* This class is in charge of loading the components of the plan summary page, including the sorted
list of exhibits and their distances and the "directions" button
 */
public class PlanSummaryActivity extends AppCompatActivity {
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_summary);

        DisplayListAdapter adapter = new DisplayListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.summary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        NavigatePlannedList navList = new NavigatePlannedList(SearchActivity.getPlan());
        List<DisplayListItem> list = createExhibitsList(navList);
        navList.resetCurrLocationIndex();
        adapter.setDisplayItems(list);

        Button directionsBtn = findViewById(R.id.directions_btn);
        directionsBtn.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            startActivity(pathIntent);
        });


    }

    /* Creates a list of exhibits in the sorted order, with the distance from the user to that exhibit
       @param navList = the sorted list that the user would navigate
       @return list of sorted exhibits with distances
     */
    public List<DisplayListItem> createExhibitsList(NavigatePlannedList navList){
        List<DisplayListItem> list = new ArrayList<>();
        double totalDistance = 0.0;
        for (int i = 1; i < navList.getPlanList().planSize()-1; i++) {
            StringBuilder exhibitSummary = new StringBuilder("");
            totalDistance+=navList.getDistanceToNextLocation();
            exhibitSummary.append(i+"). "+navList.getPlanList().get(i).getName()+": "+totalDistance+" meters");
            DisplayListItem item = new DisplayListItem(exhibitSummary.toString());
            navList.advanceLocation();
            list.add(item);
        }
        return list;
    }
}