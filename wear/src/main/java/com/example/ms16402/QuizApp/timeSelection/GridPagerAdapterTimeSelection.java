package com.example.ms16402.QuizApp.timeSelection;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.example.ms16402.QuizApp.quiz.ButtonFragment;

/**
 * Created by ms16402 on 20/04/2016.
 */
public class GridPagerAdapterTimeSelection extends FragmentGridPagerAdapter {

    Fragment[] mData;

    ColorDrawable dark_gray_bg;
    ColorDrawable default_bg;

    public GridPagerAdapterTimeSelection(FragmentManager fm, Fragment[] data){
        super(fm);
        mData = data;

        dark_gray_bg = new ColorDrawable(Color.argb(225, 0, 0, 0));
        default_bg = new ColorDrawable(Color.DKGRAY);
    }

    @Override
    public Fragment getFragment(int row, int column) {
            return mData[column];
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return mData.length;
    }

    @Override
    public Drawable getBackgroundForPage(final int row, final int column) {

        if (mData[column] instanceof ButtonFragment) {
            return dark_gray_bg;
        }
        return default_bg;
    }
}
