package com.example.ms16402.QuizApp.quiz;

import com.example.ms16402.QuizApp.wearableListViewNumber.WearableListFragment;
import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 11/04/2016.
 */
public class QuestionRow {

    QuestionCardFragment questionCardFragment;
    WearableListFragment wearableListFragment;
    ButtonFragment buttonFragment;

    public String question;
    public int id;

    public QuestionRow(int id, String s, String d, int i)
    {
        questionCardFragment = QuestionCardFragment.newInstance("Question :", s, d, i);
        wearableListFragment = WearableListFragment.newInstance(i+1);
        buttonFragment = ButtonFragment.newInstance("button_question_"+Integer.toString(id), null, "Confirm ?", R.drawable.icone_save);

        question = s;
        this.id = id;
    }

    public QuestionCardFragment getQuestionCardFragment()
    {
        return questionCardFragment;
    }

    public WearableListFragment getWearableListFragment()
    {
        return wearableListFragment;
    }

    public ButtonFragment getButtonFragment()
    {
        return buttonFragment;
    }

    public int getId(){return id; }



}
