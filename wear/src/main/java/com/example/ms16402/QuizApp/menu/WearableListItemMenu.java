package com.example.ms16402.QuizApp.menu;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 05/04/2016.
 */
public class WearableListItemMenu extends LinearLayout
        implements WearableListView.OnCenterProximityListener, WearableListView.OnScrollListener {

    private TextView menu_text_info;
    private TextView menu_text;
    private boolean isScrolling;

    public WearableListItemMenu(Context context) {
        this(context, null);
    }

    public WearableListItemMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemMenu(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        menu_text = (TextView) findViewById(R.id.menu_text);
        menu_text_info = (TextView) findViewById(R.id.info_menu_item);

    }

    @Override
    public void onCenterPosition(boolean animate) {
        menu_text.setAlpha(1f);
        menu_text.setTextSize(20);
        if (isScrolling == false) {
            menu_text_info.animate().alpha(0.8f).setDuration(500);
        }
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        menu_text.setAlpha(0.1f);
        menu_text.setTextSize(16);
        menu_text_info.animate().alpha(0f).setDuration(0);
    }

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
            isScrolling = false;
        }
        else{
            isScrolling = true;
        }

    }

    @Override
    public void onCentralPositionChanged(int i) {

    }
}