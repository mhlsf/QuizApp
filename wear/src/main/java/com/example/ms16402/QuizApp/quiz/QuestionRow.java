package com.example.ms16402.QuizApp.quiz;

import android.app.Fragment;

import com.example.ms16402.QuizApp.wearableListViewNumber.WearableListFragment;
import com.example.ms16402.gridproject.R;

/**
 * Created by ms16402 on 11/04/2016.
 */
public class QuestionRow {

    QuestionCardFragment questionCardFragment;
    Fragment answerFragment;
    ButtonFragment buttonFragment;

    public String question;
    public int id;
    public int scale;

    public QuestionRow(int id, String s, String d, int i)
    {
        questionCardFragment = QuestionCardFragment.newInstance("Question :", s, d, i);
        if (i == -1)
        {
            answerFragment = YesAndNoFragment.newInstance("button_question_"+Integer.toString(id), "Test");
        }else {
            answerFragment = WearableListFragment.newInstance(i+1);
        }
        buttonFragment = ButtonFragment.newInstance("button_question_"+Integer.toString(id), null, "Confirm ?", R.drawable.icone_save);

        question = s;
        this.id = id;
        this.scale= i;
    }

    public QuestionCardFragment getQuestionCardFragment()
    {
        return questionCardFragment;
    }

    public int getScale() {
        return scale;
    }

    public Fragment getAnswerFragment()
    {

        return answerFragment;
    }

    public ButtonFragment getButtonFragment()
    {
        return buttonFragment;
    }

    public int getId(){return id; }



}
