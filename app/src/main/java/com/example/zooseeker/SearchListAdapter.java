package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

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

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ZooData.VertexInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    // Filter Class
//    public void add(String charText) {
//        list.add(charText);
//        notifyDataSetChanged();
//    }

    public void loadSearchResults(ArrayList<ZooData.VertexInfo> results){
        list.clear();
        list.addAll(results);
        notifyDataSetChanged();
    }

}