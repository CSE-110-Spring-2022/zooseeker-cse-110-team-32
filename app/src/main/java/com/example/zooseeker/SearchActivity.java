package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
    private ExhibitViewModel viewModel;
    public static PlanList planList;
    public static ExhibitDao exhibitDao;
    public static PlanList getPlan(){
        return planList;
    }

    public static ExhibitDao getDao() { return exhibitDao;}


    /*Creates search page and initializes the needed classes and variables to work each component
    of the search page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        PlanList plan = new PlanList(this);

        exhibitDao = ExhibitDatabase.getSingleton(this, planList).exhibitDao();
        List<Exhibit> exhibitList = exhibitDao.getAll();

        //viewModel = new ExhibitViewModel(this.getApplication(), planList);
        //viewModel.getExhibitItems().observe(this, (Observer<? super List<Exhibit>>) planList.getExhibits());

        planList = plan;
        if(this.exhibitDao != null && this.exhibitDao.getAll().size() > 0){
            planList.loadList(exhibitDao);
            Intent intent = new Intent(this, ShortestPathActivity.class);
            startActivity(intent);
        }
        else{
            plan = new PlanList(this);
        }

        DisplayListAdapter adapter = new DisplayListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.display_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Locate the ListView in listview_main.xml
        resultsView = (ListView) findViewById(R.id.search_list);

        ArrayList<ZooData.VertexInfo> searchList = new ArrayList<>();
        searcher = new Search(this);

        // Pass results to ListViewAdapter Class
        searchAdapter = new SearchListAdapter(this, searchList);

        // Binds the Adapter to the ListView
        resultsView.setAdapter(searchAdapter);

        ExhibitDatabase database = Room.inMemoryDatabaseBuilder(this, ExhibitDatabase.class)
                .allowMainThreadQueries()
                .build();

        exhibitDao = database.exhibitDao();

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
        PlanList finalPlan = plan;
        resultsView.setOnItemClickListener((adapterView, v, position, id) -> {
            ZooData.VertexInfo searchItem = (ZooData.VertexInfo) adapterView.getItemAtPosition(position);
            Location exhibit = new Exhibit(searchItem.id, searchItem.name, searchItem.tags);
            finalPlan.addLocation(exhibit);
            //after we get the list of exhibits (dynamic), going to grab location names into
            //a separate list to view on display
            List<DisplayListItem> list = new ArrayList<>();
            for (int i = 0; i < finalPlan.getMyList().size(); i++) {
                DisplayListItem item = new DisplayListItem(finalPlan.getMyList().get(i).getName());
                list.add(item);
            }
            adapter.setDisplayItems(list);
            num_exhibits.setText("Number of exhibits: "+ Integer.toString(finalPlan.planSize()));
        });

        planList = plan;

        Button planBtn = findViewById(R.id.plan_btn);
        planBtn.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            planList.sort();
            planList.saveList(exhibitDao);
            startActivity(pathIntent);
        });

    }



}
