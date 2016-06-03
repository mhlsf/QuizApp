package com.example.ms16402.QuizApp.quiz;

import android.app.Fragment;

/**
 * Created by ms16402 on 31/05/2016.
 */

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 11/04/2016.
 */
public class YesAndNoFragment extends Fragment {

    public static final String KEY_ID = "key_id";
    public static final String KEY_TEXT = "key_text";

    private RelativeLayout surfaceView_left;
    private RelativeLayout surfaceView_right;
    private TextView button_left;
    private TextView button_right;
    private GradientDrawable button_shape_left;
    private GradientDrawable button_shape_right;

    private String answer;

    View view;
    String id;
    String text;
    int colorButton = -1;

    public static final YesAndNoFragment newInstance(String id, String text)
    {
        YesAndNoFragment fragment = new YesAndNoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_yes_and_no, container, false);

        surfaceView_left = (RelativeLayout) view.findViewById(R.id.surface_left);
        surfaceView_right = (RelativeLayout) view.findViewById(R.id.surface_right);
        surfaceView_left.setOnClickListener(onTouchListener_left);
        surfaceView_right.setOnClickListener(onTouchListener_right);

        button_left = (TextView) view.findViewById(R.id.button_left);
        button_right = (TextView) view.findViewById(R.id.button_right);

        button_shape_left = (GradientDrawable) view.findViewById(R.id.button_left).getBackground();
        button_shape_right = (GradientDrawable) view.findViewById(R.id.button_right).getBackground();

        button_shape_right.setStroke(0, Color.WHITE);
        button_shape_left.setStroke(0, Color.WHITE);

        return view;
    }


    private View.OnClickListener onTouchListener_left = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            button_shape_right.setStroke(0, Color.WHITE);
            button_right.setTextColor(ContextCompat.getColor(getActivity(), R.color.button_text));
            button_shape_left.setStroke(5, Color.WHITE);
            button_left.setTextColor(Color.WHITE);
            answer = "No";

        }
    };

    private View.OnClickListener onTouchListener_right = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            button_shape_left.setStroke(0, Color.WHITE);
            button_left.setTextColor(ContextCompat.getColor(getActivity(), R.color.button_text));
            button_shape_right.setStroke(5, Color.WHITE);
            button_right.setTextColor(Color.WHITE);
            answer = "Yes";
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(KEY_ID);
        text = getArguments().getString(KEY_TEXT);
    }


    public void setButtonColor(int color)
    {
        colorButton = color;
    }

    public String getAnswer(){
        return answer;
    }


}
