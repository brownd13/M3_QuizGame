package com.example.m3_quizgame;

import static com.example.m3_quizgame.ShowScores.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class GamePlay extends AppCompatActivity {

    public static final int MAX_QUESTIONS = 5;
    public static final int MAX_ANSWERS = 5;
    private String[] questions;
    int[] questionSelection;
    int[] correctAnswers = new int[MAX_ANSWERS]; // at most all answers are correct
    int numCorrectAnswers = 0;
    int gameScore = 0;
    boolean[] submittedAnswers = new boolean[MAX_ANSWERS]; // store checkbox states
    // Checkbox resource ID array needed for display iteration and checkbox tally
    public static final int[] rbResID = { R.id.rbAnswerText0, R.id.rbAnswerText1, R.id.rbAnswerText2,
                                          R.id.rbAnswerText3, R.id.rbAnswerText4 };
    public static final int[] cbResID = { R.id.cbAnswerText0, R.id.cbAnswerText1, R.id.cbAnswerText2,
                                          R.id.cbAnswerText3, R.id.cbAnswerText4 };
    private int curQuestion = 0;
    private int questionMax = 0;
    public String UserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Intent intent = getIntent();
        UserName = intent.getStringExtra("USERNAME");

        // Get list and number of questions.
        questions = getResources().getStringArray(R.array.questions);
        int questionCount = questions.length;
        questionMax = Math.min(MAX_QUESTIONS, questionCount);

        // Select up to 5 questions randomly for each game. questionSelection will index Questions
        questionSelection = new Random().ints(0, questionCount).distinct().limit(questionMax).toArray();
        Log.d("GamePlay.displayQuestions", "questionCount: " + questionCount);
        Log.d("GamePlay.displayQuestions", "questionMax: " + questionMax);
        Log.d("GamePlay.displayQuestions", "questionSelection.length: " + questionSelection.length);
        displayNextQuestion();
    }

    // Question Display / submit UI loop
    public void displayNextQuestion() {
        // Initialize answer submission array
        Arrays.fill(submittedAnswers,false);

        // Split each question-answer item into components for display and score keeping
        String[] question = questions[questionSelection[curQuestion]].split(";");
        numCorrectAnswers = Integer.parseInt(question[1]);
        int answerOffset = numCorrectAnswers + 2;
        int numAnswers = question.length - answerOffset;
        for (int i = 0; i < numCorrectAnswers; ++i) {
            correctAnswers[i] = Integer.parseInt(question[(2 + i)]);
        }

        // Display Question
        TextView tvQuestion = findViewById(R.id.questionText);
        tvQuestion.setText(question[0]);

        // Spin up answer display fragment
        // If question has multiple choice answers display checkboxes rather than radio buttons
        if (numCorrectAnswers == 1) {
            SingleAnswer fragment = new SingleAnswer();
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            Bundle QA = new Bundle();
            QA.putStringArray("Q_KEY", question);
            QA.putInt("A_KEY", answerOffset);   // Not strictly needed for SingleAnswer fragment
            QA.putInt("NA_KEY", numAnswers);    // But leaving in to keep similar to MultiAnswer
            fragment.setArguments(QA);
            fragTrans.replace(R.id.AnswerFragment, fragment).commit();
        } else {
            MultiAnswer fragment = new MultiAnswer();
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            Bundle QA = new Bundle();
            QA.putStringArray("Q_KEY", question);
            QA.putInt("A_KEY", answerOffset);
            QA.putInt("NA_KEY", numAnswers);
            fragment.setArguments(QA);
            fragTrans.replace(R.id.AnswerFragment, fragment).commit();
        }
        Log.d("GamePlay.java:", "End of displayQuestion");
    }

    public void answerSubmit(View view){
        int numCorAnsSubmitted = 0;
        int numIncorAnsSubmitted = 0;
        for(int a = 0; a < MAX_ANSWERS; a++) {
            boolean correct = false;
            if ( submittedAnswers[a] ) {
                // check against list of correct answers
                for( int ans = 0; ans < numCorrectAnswers; ans++ ){
                    if ( correctAnswers[ans] == a ) { correct = true;} // correct answer recorded
                }
                if ( correct ) {
                    numCorAnsSubmitted++;
                } else { // incorrect answer recorded
                    numIncorAnsSubmitted++;
                }
            }
        }

        if ( numCorAnsSubmitted == numCorrectAnswers && numIncorAnsSubmitted == 0) {
            gameScore += 5;
        } else { // award partial credit for some correct answers, loose partial for wrong answers
            gameScore += Math.max(0, numCorAnsSubmitted - numIncorAnsSubmitted);
        }
        // log answer count and score for validation in testing
        Log.d("answerSubmit","numCorAnsSubmitted:" + numCorAnsSubmitted);
        Log.d("answerSubmit","numIncorAnsSubmitted:" + numIncorAnsSubmitted);
        Log.d("answerSubmit","gameScore: " + gameScore );
        if( numCorAnsSubmitted + numIncorAnsSubmitted == 0 ) {
            // No answers selected, return to current view for another submission
            Toast.makeText(this, R.string.toNoAnswer, Toast.LENGTH_SHORT).show();
        } else if( curQuestion < questionMax - 1 ){
            curQuestion++;
            displayNextQuestion();
        } else {
            recordScore(); // record score
            Intent intent = new Intent(this, GameScore.class);
            intent.putExtra("SCORE", gameScore);
            startActivity(intent); // display this game's score in new activity
            finish();
        }
    }


    // Handle Checkbox selection for multiple choice answers. Iterate through checkbox resources.
    public void cbAnswerSelect(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        for(int a = 0; a < MAX_ANSWERS; a++){
            if (view.getId() == cbResID[a] ) {
                submittedAnswers[a] = checked;
            }
        }
    }

    // Handle Radiobutton single answer selection
    public void rbAnswerSelect(View view) {
        // No corresponding check activity for RB set unselect.  Initialize all per submission
        Arrays.fill(submittedAnswers,false);
        boolean checked = ((RadioButton) view).isChecked();
        for(int a = 0; a < MAX_ANSWERS; a++){
            if (view.getId() == rbResID[a] ) {
                if (checked) { submittedAnswers[a] = true; }
            }
        }
    }

    public void recordScore(){
        // read top 5 scores, place current game score on list if needed
        int[] topScores = new int[5];
        String[] topUsers = new String[5];
        boolean rankReached = false;
        SharedPreferences spTopScores = getSharedPreferences(ShowScores.sharedPrefKey, Context.MODE_PRIVATE);
        for(int i = 0, u = 0; u<5; i++, u++){
            topUsers[u] = spTopScores.getString(ShowScores.nameKey[i], "NULL");
            topScores[u] = spTopScores.getInt(ShowScores.scoreKey[i], 0);
            // Insert new score entry if score is greater than current entry
            // Only insert / shift leaderboard once, and only if there are spaces left
            if ( gameScore > topScores[i] && !rankReached && i < 4) {
                Log.d("recordScore", "rankReached, score:" + gameScore);
                rankReached = true;
                u++;
                topUsers[u] = topUsers[i];
                topScores[u] = topScores[i];
                topScores[i] = gameScore;   // Current user added to leaderboard
                topUsers[i] = UserName;
            }
        }
        // Store updated list if needed
        if(rankReached){
            SharedPreferences.Editor editor = spTopScores.edit();
            for(int i = 0, u = 0; i<5; i++, u++){
                editor.putString(ShowScores.nameKey[i], topUsers[i]);
                Log.d("TopScores recordScore", "namekey:" + nameKey[i] + " name:" + topUsers[i]);
                editor.putInt(ShowScores.scoreKey[i], topScores[i]);
            }
            editor.apply(); // write updated Top 5 score list.
        }
        finish();
    }

}