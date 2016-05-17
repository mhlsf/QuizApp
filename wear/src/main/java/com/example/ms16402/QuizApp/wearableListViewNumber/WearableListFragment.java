package com.example.ms16402.QuizApp.wearableListViewNumber;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ms16402.QuizApp.quiz.QuizActivity;
import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 05/04/2016.
 */
public class WearableListFragment extends Fragment {

    // Sample dataset for the list
    final static String KEY_NUMBER_OF_ELEMENT = "key_number_of_element";
    String[] elements;
    WearableListView wearablelistview;
    WearableListNumberAdapter wearableListNumberAdapter;

    TextView info_textView;
    String actual_number;

    public static WearableListFragment newInstance(int numberOfElement)
    {
        WearableListFragment fragment = new WearableListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putInt(KEY_NUMBER_OF_ELEMENT, numberOfElement);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int scale = getArguments().getInt(KEY_NUMBER_OF_ELEMENT, 0);

        elements = new String[scale];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_wearable_list, container, false);


        info_textView = (TextView) view.findViewById(R.id.info);

        for (int i = 0; i < elements.length ; i++) {
            elements[i] = "" + i;
        }

        wearablelistview = (WearableListView) view.findViewById(R.id.wearable_list);

        wearablelistview.setGreedyTouchMode(true);
        // Assign an wearableListNumberAdapter to the list
        wearableListNumberAdapter = new WearableListNumberAdapter(getActivity(), elements);
        wearablelistview.setAdapter(wearableListNumberAdapter);

        wearablelistview.addOnScrollListener(scrollListener);

        actual_number = wearableListNumberAdapter.getItemString(0);
        return view;
    }

    private  WearableListView.OnScrollListener scrollListener =
            new WearableListView.OnScrollListener() {
                @Override
                public void onScroll(int i) {

                }

                @Override
                public void onAbsoluteScrollChange(int i) {

                }

                @Override
                public void onCentralPositionChanged(int i) {
                    actual_number = wearableListNumberAdapter.getItemString(i);
                }

                @Override
                public void onScrollStateChanged(int i) {
                    if (i == 0) {
                        info_textView.animate().alpha(1f);
                    } else {
                        info_textView.setAlpha(0f);
                    }

                }
            };

            public String getActual_number(){
                return actual_number;
            }
}
