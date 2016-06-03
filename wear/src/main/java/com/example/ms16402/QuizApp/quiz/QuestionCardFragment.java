package com.example.ms16402.QuizApp.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.CardScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 05/04/2016.
 */

public class QuestionCardFragment extends Fragment {

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_TEXT = "key_text";
    public static final String KEY_SCALE = "key_scale";
    public static final String KEY_INFO = "key_info";

    public String title_text;
    public String content_text;
    public String contentPrecision_text;
    public int scale;

    public static QuestionCardFragment newInstance(String title, String content, String contentPrecision, int scale)
    {
        QuestionCardFragment fragment = new QuestionCardFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_SCALE, scale);
        args.putString(KEY_INFO, contentPrecision);
        args.putString(KEY_TEXT,content);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        CardScrollView view = (CardScrollView) inflater.inflate(R.layout.cardframe, container, false);

        view.setExpansionEnabled(true);
        view.setExpansionFactor(10f);

        Bundle args = this.getArguments();
        if(args != null) {
            TextView title = (TextView) view.findViewById(R.id.title);
            if(args.containsKey(KEY_TITLE) && title != null) {
                title.setText(title_text);
            }

            if(args.containsKey(KEY_TEXT)) {
                TextView text = (TextView) view.findViewById(R.id.text);
                if(text != null) {
                    text.setText(content_text);
                }
            }

            if(args.containsKey(KEY_SCALE)) {
                TextView scaleview = (TextView) view.findViewById(R.id.scale);
                if(scaleview != null) {
                    String tmp;
                    if (scale == -1)
                    {
                        tmp = "Answer with Yes or No";
                    }else
                    {
                        tmp = "On a scale from 1 to " + Integer.toString(scale);
                    }
                    scaleview.setText(tmp);
                }
            }

            if(args.containsKey(KEY_INFO)) {

                TextView precisionQuestion = (TextView) view.findViewById(R.id.question_precision);
                if(precisionQuestion != null) {
                    precisionQuestion.setText(contentPrecision_text);
                }
                else {
                    view.removeView(precisionQuestion);
                }
            }
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title_text = getArguments().getString(KEY_TITLE);
        content_text = getArguments().getString(KEY_TEXT);
        contentPrecision_text = getArguments().getString(KEY_INFO);
        scale = getArguments().getInt(KEY_SCALE);
    }
}
