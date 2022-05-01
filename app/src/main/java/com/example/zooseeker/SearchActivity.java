package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

// https://www.geeksforgeeks.org/android-searchview-with-example/

public class SearchActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView recyclerView_plan;
    public PlanList plan;
    ListView resultsView;
    SearchListAdapter searchAdapter;
    ArrayList<ZooData.VertexInfo> searchResults;
    Search searcher;
    SearchView search_bar;
    ArrayList<String> search_display_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }

        DisplayListAdapter adapter = new DisplayListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.display_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        PlanList plan = new PlanList("sample_zoo_graph.json");


        // Locate the ListView in listview_main.xml
        resultsView = (ListView) findViewById(R.id.search_list);

        ArrayList<ZooData.VertexInfo> searchList = new ArrayList<>();
        searcher = new Search(this,"sample_node_info.json");

        // Pass results to ListViewAdapter Class
        searchAdapter = new SearchListAdapter(this, searchList);

        // Binds the Adapter to the ListView
        resultsView.setAdapter(searchAdapter);

        // Locate the EditText in listview_main.xml
        search_bar = (SearchView) findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = s;
                searchResults = searcher.getResultsInfo(text);
                searchAdapter.loadSearchResults(searchResults);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        resultsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id){
                ZooData.VertexInfo searchItem = (ZooData.VertexInfo) adapterView.getItemAtPosition(position);
                System.out.println(searchItem.id);
                Location exhibit = new Exhibit(searchItem.id, searchItem.name, searchItem.tags);
                plan.addLocation(exhibit);
                //after we get the list of exhibits (dynamic), going to grab location names into
                //a separate list to view on display
                List<DisplayListItem> list = new ArrayList<DisplayListItem>();
                for (int i = 0; i < plan.getMyList().size(); i++) {
                    DisplayListItem item = new DisplayListItem(plan.getMyList().get(i).getName());
                    list.add(item);
                }

                adapter.setDisplayItems(list);
            }
        });
    }


    public void search(String query) {

    }


}