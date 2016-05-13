package com.example.ms16402.QuizApp.wearableListViewNumber;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 05/04/2016.
 */
public class WearableListItemLayout extends FrameLayout
        implements WearableListView.OnCenterProximityListener{

    private TextView mName;
    private final float mFadedTextAlpha;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

/*
    private static final float SHRINK_LABEL_ALPHA = .5f;
    private static final float EXPAND_LABEL_ALPHA = 1f;
    private final ObjectAnimator mShrinkLabelAnimator;
    private final ObjectAnimator mExpandLabelAnimator;

    mExpandLabelAnimator = ObjectAnimator.ofFloat(mTextView, "alpha",
    SHRINK_LABEL_ALPHA, EXPAND_LABEL_ALPHA);
    mShrinkLabelAnimator = ObjectAnimator.ofFloat(mTextView, "alpha",
    EXPAND_LABEL_ALPHA, SHRINK_LABEL_ALPHA);
*/

    public WearableListItemLayout(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        mFadedTextAlpha = getResources()
                .getInteger(R.integer.action_text_faded_alpha) / 100f;
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // These are defined in the layout file for list items
        // (see next section)
        mName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mName.setAlpha(1f);
        if (animate){
            mName.animate().scaleY(2.3f).scaleX(2.3f).setDuration(100).setStartDelay(0);
        }

    }

    @Override
    public void onNonCenterPosition(boolean animate) {

        if(animate)
        {
            mName.animate().scaleY(1f).scaleX(1f).setDuration(100).setStartDelay(0);
        }
        mName.setAlpha(mFadedTextAlpha);
    }


}