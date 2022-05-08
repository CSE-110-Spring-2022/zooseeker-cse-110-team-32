package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/*
    Citation: https://abhiandroid.com/ui/searchview
    Name: SearchView Tutorial With Example In Android Studio
    Date: 05-01-2022
    Use: Followed given tutorial with multiple alterations to fit our needs
    Teammate who cited: Clair Ma
 */

/* This class allows us to control displaying the search results
 *
 */
public class SearchListAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<ZooData.VertexInfo> list = null;

    public SearchListAdapter(Context context, List<ZooData.VertexInfo> entryList) {
        mContext = context;
        this.list = entryList;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        TextView name;
    }

    /*returns number of items in the list of resulting exhibits
    @return number of items in list of resulting exhibits
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /*Returns exhibit at the given position in the list of resulting exhibits
    @param position = position of exhibit in the list
    @return exhibit information at given position
     */
    @Override
    public ZooData.VertexInfo getItem(int position) {
        return list.get(position);
    }

    /*Returns exhibit's ID
    @param position = exhibit's position
    @return exhibit's ID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*Displays the list of search results in the ListView
     @param position = position of item in ListView
     @param view = view of item
     @param parent = ViewGroup containing item Views
     @return view = created or provided view of item
     */
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_searchresult, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.search_list_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(list.get(position).name);
        return view;
    }

    // Can be used later for autocomplete suggestions
//    public void add(String charText) {
//        list.add(charText);
//        notifyDataSetChanged();
//    }

    /*loads the list of results that match or contain user's search query
    @param results = list of results from user's query
     */
    public void loadSearchResults(ArrayList<ZooData.VertexInfo> results){
        list.clear();
        list.addAll(results);
        notifyDataSetChanged();
    }

}