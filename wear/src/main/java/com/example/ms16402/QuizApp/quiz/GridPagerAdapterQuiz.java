package com.example.ms16402.QuizApp.quiz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ms16402 on 04/04/2016.
 */
public class GridPagerAdapterQuiz extends FragmentGridPagerAdapter {

    ArrayList<QuestionRow> mData;
    ColorDrawable light_dark_bg;
    ColorDrawable default_bg;

    public GridPagerAdapterQuiz(FragmentManager fm, ArrayList data)
    {
        super(fm);
        mData = data;

        light_dark_bg = new ColorDrawable(Color.argb(200, 0, 0, 0));
        default_bg = new ColorDrawable(Color.TRANSPARENT);

    }

    @Override
    public Fragment getFragment(int row, int column) {
        if (mData.size() == 0)
        {
            return CardFragment.create("End of the quiz", "Swipe right to quit");
        }

        if (column == 0)
        {
            return mData.get(row).getQuestionCardFragment();
        }

        if (column == 1)
        {
            return mData.get(row).getWearableListFragment();
        }

        if (column == 2)
        {
            return mData.get(row).getButtonFragment();
        }

        return null;
    }

    @Override
    public int getRowCount()
    {
        if (mData.size() == 0)
        {
            return 1;
        }
        return mData.size();
    }

    @Override
    public Drawable getBackgroundForPage(final int row, final int column)
    {
        if (column > 0)
        {
            return light_dark_bg;
        }
        return default_bg;
    }

    @Override
    public int getColumnCount(int row)
    {
        if (mData.size() == 0)
        {
            return 1;
        }
        return 3;
    }

    public void deleteQuestion(int i)
    {
        mData.remove(i);
        notifyDataSetChanged();
    }

    public QuestionRow getQuestionRow(int i )
    {
        return mData.get(i);
    }
}
