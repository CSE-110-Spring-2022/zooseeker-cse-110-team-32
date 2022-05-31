package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

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

        List<DisplayListItem> list = new ArrayList<>();
        NavigatePlannedList navList = new NavigatePlannedList(SearchActivity.getPlan());
        double totalDistance = 0.0;
        for (int i = 1; i < SearchActivity.getPlan().planSize()-1; i++) {
            StringBuilder exhibitSummary = new StringBuilder("");
            totalDistance+=navList.getDistanceToNextLocation();
            exhibitSummary.append(i+"). "+SearchActivity.getPlan().get(i).getName()+": "+totalDistance+" meters");
            DisplayListItem item = new DisplayListItem(exhibitSummary.toString());
            navList.advanceLocation();
            list.add(item);
        }
        navList.resetCurrLocationIndex();
        adapter.setDisplayItems(list);

        Button directionsBtn = findViewById(R.id.directions_btn);
        directionsBtn.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            startActivity(pathIntent);
        });
    }
}