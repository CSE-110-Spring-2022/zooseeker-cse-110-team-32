package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/* This class creates an adapter that connects DisplayListItem and RecyclerView????
 *
 */
public class DisplayListAdapter extends RecyclerView.Adapter<DisplayListAdapter.ViewHolder> {
    private List<DisplayListItem> displayItems = Collections.emptyList();

    /* Sets list of DisplayListItems
       @param: newDisplayItems = list of new DisplayListItems
     */
    public void setDisplayItems(List<DisplayListItem> newDisplayItems) {
        this.displayItems.clear();
        this.displayItems = newDisplayItems;
        notifyDataSetChanged();
    }

    /* Returns list of DisplayListItems
      @return: displayItems = list of DisplayListItems, the list of displayable exhibits the
      user wants to see
    */
    public List<DisplayListItem> getDisplayItems(){
        return displayItems;
    }

    /*Not super sure what this does tbh
     */
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

    /*returns the number of exhibits the user wants to see
      @return displayItems.size() = number of exhibits the user selected
     */
    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    /* Again not super sure what this does tbh
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private DisplayListItem displayItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.display_list_item);
        }

        /*returns the item/exhibit displayed on screen
        @return: displayItem = exhibit to be displayed
         */
        public DisplayListItem getDisplayListItem() { return displayItem;}

        /*Sets the exhibit to be displayed in the textview
       @param: displayItem = item to be displayed
        */
        public void setDisplayItem(DisplayListItem displayItem) {
            this.displayItem = displayItem;
            this.textView.setText(displayItem.name);
        }
    }

}
