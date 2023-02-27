package com.example.m3_quizgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;


public class MultiAnswer extends Fragment {
    public MultiAnswer() { }  // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get Question/answer text from arg bundle
        String[] question = getArguments().getStringArray("Q_KEY");
        int answerOffset = getArguments().getInt("A_KEY");
        final int numAnswers = getArguments().getInt("NA_KEY");
        View view = inflater.inflate(R.layout.fragment_multi_answer, container, false);

        // Display answer checkboxes. Hide checkboxes for empty answers
        CheckBox cb;
        for(int i = 0; i < GamePlay.MAX_ANSWERS; i++ ) {
            cb = view.findViewById(GamePlay.cbResID[i]);
            if (numAnswers > i) {
                cb.setText(question[answerOffset + i]);
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }


}