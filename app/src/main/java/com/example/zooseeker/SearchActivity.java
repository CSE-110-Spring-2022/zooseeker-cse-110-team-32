package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    public static void resetPlan(){ planList = null;}

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
        planList = plan;
        if(this.exhibitDao != null && exhibitDao.getAll().size() > 0){
            planList.loadList(exhibitDao);
            Intent intent = new Intent(this, ShortestPathActivity.class);
            startActivity(intent);
        }
        else{
            planList = new PlanList(this);
        }

        DisplayListAdapter adapter = new DisplayListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.display_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        PlanManager planManager = new PlanManager(this, plan);

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
        List<String> exhibitList = new ArrayList();
        resultsView.setOnItemClickListener((adapterView, v, position, id) -> {
            ZooData.VertexInfo searchItem = (ZooData.VertexInfo) adapterView.getItemAtPosition(position);
            planManager.addLocation(searchItem);
            if (!exhibitList.contains(searchItem.name)) {
                exhibitList.add(searchItem.name);
            }
            //after we get the list of exhibits (dynamic), going to grab location names into
            //a separate list to view on display
            List<DisplayListItem> list = new ArrayList<>();
            for (int i = 0; i < exhibitList.size(); i++) {
                DisplayListItem item = new DisplayListItem(exhibitList.get(i));
                list.add(item);
            }
            adapter.setDisplayItems(list);
            num_exhibits.setText("Number of exhibits: "+ Integer.toString(plan.planSize()));

        });

        Button planBtn = findViewById(R.id.plan_btn);
        planBtn.setOnClickListener(view ->{
            Intent pathIntent = new Intent(this, ShortestPathActivity.class);
            planList = planManager.getFinalPlan();
            planList.saveList(exhibitDao);
            startActivity(pathIntent);
        });

    }



}
