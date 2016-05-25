package com.example.ms16402.QuizApp.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.ActionPage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 11/04/2016.
 */
public class ButtonFragment extends Fragment {

    public static final String KEY_ID = "key_id";
    public static final String KEY_ICON_ID = "key_icon_id";
    public static final String KEY_PARENT_TAG = "key_parent_tag";
    public static final String KEY_TEXT = "key_text";

    ActionPage actionPage;
    OnClickListener clickListener;
    View view;
    String id;
    int imageId;
    String parent;
    String text;
    int colorButton = -1;
    ProgressBar spinner;

    public static final ButtonFragment newInstance(String id, String parent, String text, int icon)
    {
        ButtonFragment fragment = new ButtonFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_PARENT_TAG, parent);
        args.putString(KEY_TEXT, text);
        args.putInt(KEY_ICON_ID, icon);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_button_quiz, container, false);

        spinner = (ProgressBar) view.findViewById(R.id.progressBar);
        spinner.setVisibility(View.INVISIBLE);
        actionPage = (ActionPage) view.findViewById(R.id.actionpage);

        actionPage.setText(text);

        actionPage.setImageResource(imageId);

        if (colorButton != -1){
            actionPage.setColor(colorButton);
        }

        actionPage.setBackgroundColor(Color.TRANSPARENT);

        actionPage.setOnClickListener(onClickListener);

        return view;
    }

    private View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            passData(id);
        }
    };

    public void loadingStart()
    {
            actionPage.setOnClickListener(null);
            spinner.setVisibility(View.VISIBLE);
            actionPage.setText("Loading...");
            actionPage.setImageResource(android.R.color.transparent);
    }

    public void loadingStop()
    {
        spinner.setVisibility(View.INVISIBLE);
        actionPage.setImageResource(imageId);
        actionPage.setText(text);
        actionPage.setOnClickListener(onClickListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(KEY_ID);
        parent = getArguments().getString(KEY_PARENT_TAG);
        text = getArguments().getString(KEY_TEXT);
        imageId = getArguments().getInt(KEY_ICON_ID);
        if (parent == null)
        {
            onAttachFragment(getActivity());
        }
        else{
            onAttachFragment(getFragmentManager().findFragmentByTag(parent));
        }
    }

    public void onAttachFragment(Object object)
    {
        clickListener = (OnClickListener) object;
    }

    public void setButtonColor(int color)
    {
        colorButton = color;
    }

    public void setClickListener(OnClickListener d)
    {
        this.clickListener = d;
    }

    public void passData(String id)
    {
        clickListener.OnClick(id);
    }

    public interface OnClickListener
    {
        void OnClick(String id);
    }
}
