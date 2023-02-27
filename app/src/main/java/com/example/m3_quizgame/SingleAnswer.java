package com.example.m3_quizgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class SingleAnswer extends Fragment {
    public SingleAnswer() {
        // Required empty public constructor
    }
    public static SingleAnswer newInstance() {
        SingleAnswer fragment = new SingleAnswer();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] question = getArguments().getStringArray("Q_KEY");
        int answerOffset = getArguments().getInt("A_KEY");      // should always be 2
        final int numAnswers = getArguments().getInt("NA_KEY"); // should always be 1
        View view = inflater.inflate(R.layout.fragment_single_answer, container, false);


        // Display answer radiobuttons. Hide empty answers
        RadioButton rb;
        for(int i = 0; i < GamePlay.MAX_ANSWERS; i++ ) {
            rb = view.findViewById(GamePlay.rbResID[i]);
            if (numAnswers > i) {
                rb.setText(question[answerOffset + i]);
                rb.setVisibility(View.VISIBLE);
            } else {
                rb.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }
}