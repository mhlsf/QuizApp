package com.example.ms16402.QuizApp;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Extend the WearableListView to circumvent the render error the intellij android designer
 */
public class WearableListViewFix extends WearableListView {
    public WearableListViewFix(Context context) {
        super(context);
    }

    public WearableListViewFix(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WearableListViewFix(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * Override this method to implement the choice for the intellij android designer
     */
    @Override
    public ViewHolder getChildViewHolder(View child) {
        if (!isInEditMode()) {
            return super.getChildViewHolder(child);
        } else {
            /**
             * Override this with an empty body to avoid an error in intellij android designer
             */
            return new WearableListView.ViewHolder(new View(getContext())) {
                @Override
                protected void onCenterProximity(boolean isCentralItem, boolean animate) {

                }
            };
        }
    }
}