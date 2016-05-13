package com.example.ms16402.QuizApp.timeSelection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.ms16402.QuizApp.wearableListViewNumber.WearableListNumberAdapter;
import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 19/04/2016.
 */
public class TimeSelectionFragment extends Fragment {

    public static final String KEY_HOUR = "key_hour";
    public static final String KEY_MINUTE = "key_minute";
    public static final String KEY_HOUR_LIST = "key_hour_list";
    public static final String KEY_MINUTE_LIST = "key_minute_list";

    WearableListView wearableListViewHour;
    WearableListView wearableListViewMinute;
    WearableListNumberAdapter wearableListNumberAdapterHour;
    WearableListNumberAdapter wearableListNumberAdapterMinute;

    String[] hours;
    String[] minutes;

    TextView hour_textView;
    TextView minute_textView;

    int hour_value;
    int minute_value;

    AlphaAnimation animation1;

    public static final TimeSelectionFragment newInstance(int h, int m, String[] h_list, String[] m_list){
        TimeSelectionFragment fragment = new TimeSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_HOUR, h);
        args.putInt(KEY_MINUTE, m);
        args.putStringArray(KEY_HOUR_LIST, h_list);
        args.putStringArray(KEY_MINUTE_LIST, m_list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_time_selection, container, false);

        hour_textView = (TextView) view.findViewById(R.id.hour);
        minute_textView = (TextView) view.findViewById(R.id.minute);

        wearableListViewHour = (WearableListView) view.findViewById(R.id.we1);
        wearableListNumberAdapterHour = new WearableListNumberAdapter(getActivity(), hours);
        wearableListViewHour.setGreedyTouchMode(true);
        wearableListViewHour.setAdapter(wearableListNumberAdapterHour);

        wearableListViewMinute = (WearableListView) view.findViewById(R.id.we2);
        wearableListNumberAdapterMinute = new WearableListNumberAdapter(getActivity(), minutes);
        wearableListViewMinute.setGreedyTouchMode(true);
        wearableListViewMinute.setAdapter(wearableListNumberAdapterMinute);


        animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);

        wearableListViewHour.addOnScrollListener(onScrollListenerHour);
        wearableListViewMinute.addOnScrollListener(onScrollListenerMinute);

        if (hour_value != 0)
        {
            wearableListViewHour.scrollToPosition(hour_value);
        }

        if (minute_value != 1)
        {
            wearableListViewMinute.scrollToPosition(minute_value-1);
        }


        return view;
    }


    private WearableListView.OnScrollListener onScrollListenerHour = new WearableListView.OnScrollListener() {
        @Override
        public void onScroll(int i) {
        }

        @Override
        public void onAbsoluteScrollChange(int i) {

        }

        @Override
        public void onScrollStateChanged(int i) {
            if (i == 0)
            {
                hour_textView.animate().alpha(1f);
            }
            else {
                hour_textView.setAlpha(0f);
            }
        }

        @Override
        public void onCentralPositionChanged(int i) {
            hour_value = i;
        }
    };

    private WearableListView.OnScrollListener onScrollListenerMinute = new WearableListView.OnScrollListener() {
        @Override
        public void onScroll(int i) {
        }

        @Override
        public void onAbsoluteScrollChange(int i) {

        }

        @Override
        public void onScrollStateChanged(int i) {
            if (i == 0)
            {
                minute_textView.animate().alpha(1f);
            }
            else {
                minute_textView.setAlpha(0f);
            }
        }

        @Override
        public void onCentralPositionChanged(int i) {
            minute_value = Integer.parseInt(minutes[i]);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hours = getArguments().getStringArray(KEY_HOUR_LIST);
        minutes = getArguments().getStringArray(KEY_MINUTE_LIST);


        if (getArguments().getInt(KEY_HOUR) == -1)
        {
            hour_value = Integer.parseInt(hours[0]);
        }
        else
        {
            hour_value = getArguments().getInt(KEY_HOUR);
        }

        if (getArguments().getInt(KEY_MINUTE) == -1)
        {
            minute_value = Integer.parseInt(minutes[0]);
        }
        else
        {
            minute_value = getArguments().getInt(KEY_MINUTE);
        }
    }

    public int getHour()
    {
        return hour_value;
    }

    public int getMinute()
    {
        return minute_value;
    }
}
