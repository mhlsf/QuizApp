package com.example.ms16402.QuizApp.timeSelection;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.GridViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ms16402.gridproject.R;
import com.example.ms16402.QuizApp.menu.MenuActivity;
import com.example.ms16402.QuizApp.quiz.ButtonFragment;

import java.util.concurrent.TimeUnit;

/**
 * Created by ms16402 on 20/04/2016.
 */
public class GridIntervalSelectionFragment extends Fragment implements ButtonFragment.OnClickListener {

    Fragment[] data = new Fragment[5];
    TimeSelectionFragment timeSelectionFragment;
    TimeSelectionFragment timeRandomSelectionFragment;
    ButtonFragment buttonConfirmFragment;
    ButtonFragment buttonRandomFragment;
    ButtonFragment buttonCancelFragment;

    final static String ID_RANDOM_BUTTON = "randomButton";
    final static String ID_CONFIRM_BUTTON = "confirmButton";
    final static String ID_CANCEL_BUTTON = "cancelButton";

    public static final GridIntervalSelectionFragment newInstance(){
        GridIntervalSelectionFragment fragment = new GridIntervalSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_grid_time_selection, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
        int hour = preferences.getInt(MenuActivity.PREFS_HOUR_INTERVAL, 0);
        int minute = preferences.getInt(MenuActivity.PREFS_MINUTE_INTERVAL, 1);
        int hourRandom = preferences.getInt(MenuActivity.PREFS_RANDON_HOUR_INTERVAL, 0);
        int minuteRandom = preferences.getInt(MenuActivity.PREFS_RANDOM_MINUTE_INTERVAL, 1);


        //Setting the list for the hour and the minute
        String[] hour_list = new String[24];
        for (int i = 0; i < hour_list.length ; i++) {
            if (i<10)
            {
                hour_list[i] = "0" + i;
            }
            else
            {
                hour_list[i] = "" + i;
            }
        }
        String[] minute_list = new String[59];
        for (int i = 0; i < minute_list.length; i++) {
            if ((i+1)<10)
            {
                minute_list[i] = "0" + (i+1);
            }
            else
            {
                minute_list[i] = "" + (i+1);
            }
        }

        if (minute == 0)
        {
            minute = 1;
        }

        timeSelectionFragment = TimeSelectionFragment.newInstance(hour, minute, hour_list, minute_list);
        timeRandomSelectionFragment = TimeSelectionFragment.newInstance(hourRandom, minuteRandom, hour_list, minute_list);
        buttonConfirmFragment = ButtonFragment.newInstance(ID_CONFIRM_BUTTON,this.getTag(), "Set Interval ?", R.drawable.icone_save);
        buttonRandomFragment = ButtonFragment.newInstance(ID_RANDOM_BUTTON,this.getTag(), "Set Random Interval ?", R.drawable.icone_save);
        buttonCancelFragment = ButtonFragment.newInstance(ID_CANCEL_BUTTON,this.getTag(), "Cancel", R.drawable.ic_clear_white_48dp);
        buttonCancelFragment.setButtonColor(Color.RED);

        data[0] = timeSelectionFragment;
        data[1] = buttonConfirmFragment;
        data[2] = timeRandomSelectionFragment;
        data[3] = buttonRandomFragment;
        data[4] = buttonCancelFragment;

        GridViewPager gridViewPager = (GridViewPager)
                view.findViewById(R.id.gridViewPagerTimeSelection);
        GridPagerAdapterTimeSelection gridPagerAdapterTimeSelection =
                new GridPagerAdapterTimeSelection(getFragmentManager(), data);
        gridViewPager.setAdapter(gridPagerAdapterTimeSelection);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void removeFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (int i = 0; i < data.length; i++) {
            ft.remove(data[i]);
        }
        ft.remove(this).commit();
    }

    @Override
    public void OnClick(String id) {
        int hour = 0;
        int minute = 0;

        if (!ID_CANCEL_BUTTON.equals(id))
        {
            if (id.equals(ID_CONFIRM_BUTTON))
            {
                hour =  timeSelectionFragment.getHour();
                minute =  timeSelectionFragment.getMinute();
            }
            else if (id.equals(ID_RANDOM_BUTTON))
            {
                long milliseconds = TimeUnit.HOURS.toMillis(timeRandomSelectionFragment.getHour()) + TimeUnit.MINUTES.toMillis(timeRandomSelectionFragment.getMinute());

                long randomMilliseconds = (long)(Math.random() * (milliseconds));

                hour = (int)  TimeUnit.MILLISECONDS.toHours(randomMilliseconds);
                minute = (int)(TimeUnit.MILLISECONDS.toMinutes(randomMilliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(randomMilliseconds)));

                SharedPreferences preferences = getActivity().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
                preferences.edit().putInt(MenuActivity.PREFS_RANDON_HOUR_INTERVAL,timeRandomSelectionFragment.getHour()).apply();
                preferences.edit().putInt(MenuActivity.PREFS_RANDOM_MINUTE_INTERVAL, timeRandomSelectionFragment.getMinute()).apply();
            }

            SharedPreferences preferences = getActivity().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
            preferences.edit().putInt(MenuActivity.PREFS_HOUR_INTERVAL,hour).apply();
            preferences.edit().putInt(MenuActivity.PREFS_MINUTE_INTERVAL, minute).apply();

            ((MenuActivity) getActivity()).updateTimeInterval();
            removeFragment();
        }
        else
        {
            removeFragment();
        }



    }
}
