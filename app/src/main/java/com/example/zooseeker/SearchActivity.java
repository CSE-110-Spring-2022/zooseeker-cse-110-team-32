package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static PlanList planList;
    public static PlanList getPlan(){
        return planList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }*/

        this.planList = new PlanList("json");
        Location loc = new Exhibit("entrance_plaza", "Entrance Plaza", new ArrayList<String>());
        List<String> tags = new ArrayList<String>();
        tags.add("alligator");
        tags.add("reptile");
        tags.add("gator");

        Location loc2 = new Exhibit("gators", "Alligators", tags);
        planList.addLocation(loc);
        planList.addLocation(loc2);


        Button plan = findViewById(R.id.plan_btn);
        plan.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            //pathIntent.putExtra("PlanList", (Parcelable) planList);
            //pathIntent.putParcelableArrayListExtra("PlanList", (ArrayList<? extends Parcelable>) planList.getMyList());

            startActivity(pathIntent);
        });




    }

    public void search(String query) {

    }


}