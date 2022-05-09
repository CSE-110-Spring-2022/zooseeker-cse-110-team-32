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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*This class loads the search page including search bar, search results, list of planned exhibits,
and number of exhibits the user wants to see
 */
public class SearchActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    ListView resultsView;
    SearchListAdapter searchAdapter;
    ArrayList<ZooData.VertexInfo> searchResults;
    Search searcher;
    SearchView search_bar;
    public static PlanList planList;
    public static PlanList getPlan(){
        return planList;
    }

    /*Creates search page and initializes the needed classes and variables to work each component
    of the search page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        DisplayListAdapter adapter = new DisplayListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.display_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        PlanList plan = new PlanList(this);

        // Locate the ListView in listview_main.xml
        resultsView = (ListView) findViewById(R.id.search_list);

        ArrayList<ZooData.VertexInfo> searchList = new ArrayList<>();
        searcher = new Search(this);

        // Pass results to ListViewAdapter Class
        searchAdapter = new SearchListAdapter(this, searchList);

        // Binds the Adapter to the ListView
        resultsView.setAdapter(searchAdapter);


        // Locate the EditText in listview_main.xml
        search_bar = (SearchView) findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /*Submits the user's search query and changes the user's query to be all lowercase, so
            that search results aren't case sensitive
            @param s = user's search query
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = s;
                text = text.toLowerCase();
                searchResults = searcher.getResults(text);
                searchAdapter.loadSearchResults(searchResults);
                return false;
            }

            /*Detects when text in search bar is updated, currently does nothing with this

             */
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        TextView num_exhibits = findViewById(R.id.exhibits_num);
        resultsView.setOnItemClickListener((adapterView, v, position, id) -> {
            ZooData.VertexInfo searchItem = (ZooData.VertexInfo) adapterView.getItemAtPosition(position);
            Location exhibit = new Exhibit(searchItem.id, searchItem.name, searchItem.tags);
            plan.addLocation(exhibit);
            //after we get the list of exhibits (dynamic), going to grab location names into
            //a separate list to view on display
            List<DisplayListItem> list = new ArrayList<>();
            for (int i = 0; i < plan.getMyList().size(); i++) {
                DisplayListItem item = new DisplayListItem(plan.getMyList().get(i).getName());
                list.add(item);
            }
            adapter.setDisplayItems(list);
            num_exhibits.setText("Number of exhibits: "+ Integer.toString(plan.planSize()));
        });

        planList = plan;

        Button planBtn = findViewById(R.id.plan_btn);
        planBtn.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            plan.sort();
            startActivity(pathIntent);
        });

    }



}
