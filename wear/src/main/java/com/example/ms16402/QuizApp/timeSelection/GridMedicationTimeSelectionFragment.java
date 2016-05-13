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

import java.util.Calendar;

/**
 * Created by ms16402 on 20/04/2016.
 */
public class GridMedicationTimeSelectionFragment extends Fragment implements ButtonFragment.OnClickListener {


    Fragment[] data = new Fragment[4];
    TimeSelectionFragment timeSelectionFragment;
    ButtonFragment buttonFragment;
    ButtonFragment buttonResetFragment;
    ButtonFragment buttonCancelFragment;

    final static String ID_RESET_BUTTON = "randomButton";
    final static String ID_CONFIRM_BUTTON = "confirmButton";
    final static String ID_CANCEL_BUTTON = "cancelButton";

    public static final GridMedicationTimeSelectionFragment newInstance(){
        GridMedicationTimeSelectionFragment fragment = new GridMedicationTimeSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_grid_time_selection, container, false);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);

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
        String[] minute_list = new String[60];
        for (int i = 0; i < minute_list.length; i++) {
            if (i<10)
            {
                minute_list[i] = "0" + i;
            }
            else
            {
                minute_list[i] = "" + i;
            }
        }

        timeSelectionFragment = TimeSelectionFragment.newInstance(hour, minute, hour_list, minute_list);
        buttonFragment = ButtonFragment.newInstance(ID_CONFIRM_BUTTON, this.getTag(), "Set medication Time ?", R.drawable.icone_save);
        buttonResetFragment = ButtonFragment.newInstance(ID_RESET_BUTTON, this.getTag(), "No medication Time ?", R.drawable.icone_save);
        buttonCancelFragment = ButtonFragment.newInstance(ID_CANCEL_BUTTON,this.getTag(), "Cancel", R.drawable.ic_clear_white_48dp);
        buttonCancelFragment.setButtonColor(Color.RED);


        data[0] = timeSelectionFragment;
        data[1] = buttonFragment;
        data[2] = buttonResetFragment;
        data[3] = buttonCancelFragment;

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
            else if(id.equals(ID_RESET_BUTTON))
            {
                hour =  -1;
                minute =  -1;
            }

            SharedPreferences preferences = getActivity().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
            preferences.edit().putInt(MenuActivity.PREFS_LAST_HOUR_MEDICATION_TIME,hour).apply();
            preferences.edit().putInt(MenuActivity.PREFS_LAST_MINUTE_MEDICATION_TIME,minute).apply();

            ((MenuActivity) getActivity()).updateTimeMedication();

            removeFragment();
        }
        else {
            removeFragment();
        }

    }
}
