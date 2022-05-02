package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class DisplayListAdapter extends RecyclerView.Adapter<DisplayListAdapter.ViewHolder> {
    private List<DisplayListItem> displayItems = Collections.emptyList();

    public void setDisplayItems(List<DisplayListItem> newDisplayItems) {
        this.displayItems.clear();
        this.displayItems = newDisplayItems;
        notifyDataSetChanged();
    }

    public List<DisplayListItem> getDisplayItems(){
        return displayItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_searchview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDisplayItem(displayItems.get(position));
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private DisplayListItem displayItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.display_list_item);
        }

        public DisplayListItem getDisplayListItem() { return displayItem;}

        public void setDisplayItem(DisplayListItem displayItem) {
            this.displayItem = displayItem;
            this.textView.setText(displayItem.name);
        }
    }

}
