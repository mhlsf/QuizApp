package com.example.ms16402.QuizApp.menu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 07/04/2016.
 */
public class DelayedConfirmationFragment extends Fragment implements
        DelayedConfirmationView.DelayedConfirmationListener {

    DelayedConfirmationView mDelayedView;
    TextView content_text_view;
    TextView title_text_view;
    TextView action_text_view;

    OnDataPass dataPasser;

    public static final String KEY_ID = "key_id";
    public static final String KEY_TITLE = "key_title";
    public final static String KEY_CONTENT = "key_content";
    public final static String KEY_ACTION= "key_action";
    public final static String KEY_TIME = "key_time";

    private String id;
    private String title;
    private String content;
    private String action;
    private int time;

    public static final DelayedConfirmationFragment newInstance(String id,String title, String content, String action, int time) {
        DelayedConfirmationFragment fragment = new DelayedConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_TITLE, title);
        args.putString(KEY_CONTENT, content);
        args.putString(KEY_ACTION, action);
        args.putInt(KEY_TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        mDelayedView =
                (DelayedConfirmationView) view.findViewById(R.id.delayed_confirm);
        mDelayedView.setListener(this);

        title_text_view = (TextView) view.findViewById(R.id.title_text_view);
        content_text_view = (TextView) view.findViewById(R.id.content_text_view);
        action_text_view = (TextView) view.findViewById(R.id.action_text_view);

        title_text_view.setText(title);
        content_text_view.setText(content);
        action_text_view.setText(action);

        mDelayedView.setTotalTimeMs(time);
        mDelayedView.setListener(this);
        mDelayedView.start();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString(KEY_ID);
        title =  getArguments().getString(KEY_TITLE);
        content = getArguments().getString(KEY_CONTENT);
        action = getArguments().getString(KEY_ACTION);
        time = getArguments().getInt(KEY_TIME);

    }

    @Override
    public void onPause() {
        super.onPause();
        mDelayedView.reset();
        removeFragment();
    }

    @Override
    public void onTimerFinished(View view) {
        passData(id);
        removeFragment();
    }

    @Override
    public void onTimerSelected(View view) {
        mDelayedView.reset();
        removeFragment();
    }

    public void removeFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this).commit();
    }

    public void setDataPasser(DelayedConfirmationFragment.OnDataPass d)
    {
        this.dataPasser = d;
    }

    public void passData(String id){
        dataPasser.onDataPass(id);
    }

    public interface OnDataPass{
        void onDataPass(String id);
    }

}

