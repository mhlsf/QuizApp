package com.example.ms16402.QuizApp.menu;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

import java.util.ArrayList;

/**
 * Created by ms16402 on 05/04/2016.
 */
public class WearableListMenuAdapter extends WearableListView.Adapter {

    private ArrayList<MenuItem> mDataset;
    private final LayoutInflater mInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public WearableListMenuAdapter(Context context, ArrayList<MenuItem> dataset) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = dataset;

    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private TextView textView_info;
        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.menu_text);
            textView_info = (TextView) itemView.findViewById(R.id.info_menu_item);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate our custom layout for list items
        return  new ItemViewHolder(mInflater.inflate(R.layout.list_item_menu, parent, false));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // Retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        TextView textView_info = itemHolder.textView_info;
        // Set the text from the dataset
        view.setText(mDataset.get(position).getMain());
        String tmp = mDataset.get(position).getInfo();
        if (tmp != null)
        {
            textView_info.setText(tmp);
        }
        else {
            textView_info.setVisibility(View.INVISIBLE);
        }

        // Set the item position
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void changeDataSetMain(String s, int i)
    {
        mDataset.get(i).setMain(s);
    }

    public void changeDataSetInfo(String s, int i)
    {
        mDataset.get(i).setInfo(s);
    }

    public ArrayList<MenuItem> getDataSet(){
        return mDataset;
    }

    public boolean contains(String m)
    {
        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).getMain() == m)
            {
                return true;
            }
        }

        return false;
    }

    public void remove(String m)
    {
        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).getMain() == m)
            {
                mDataset.remove(i);
            }
        }
    }
}