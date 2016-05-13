package com.example.ms16402.QuizApp.wearableListViewNumber;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 05/04/2016.
 */
public class WearableListNumberAdapter extends WearableListView.Adapter {
    private Context mContext;
    private String[] mDataset;
    private final LayoutInflater mInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public WearableListNumberAdapter(Context context, String[] dataset) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = dataset;

    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate our custom layout for list items
        return  new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        // replace text contents
        view.setText(mDataset[position]);
        // replace list item's metadata
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }



    public String getItemString(int i ){
        return mDataset[i];
    }
}