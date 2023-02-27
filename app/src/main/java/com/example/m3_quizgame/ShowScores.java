package com.example.m3_quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowScores extends AppCompatActivity {

    // Resources for Top 5 score storage, utliized locally and by GamePlay.java
    public static final String[] scoreKey = {"score0", "score1", "score2", "score3", "score4"};
    public static final String[] nameKey = {"name0", "name1", "name2", "name3", "name4"};
    public static final String sharedPrefKey = "com.example.m3_quizgame.TOP5_SCORES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);
        showTopScores();
    }

    public void showTopScores(){
        // array of Resources for Top 5 score display iteration
        final int[] tvScoreResID = {R.id.tvScore0, R.id.tvScore1, R.id.tvScore2,
                R.id.tvScore3, R.id.tvScore4};
        // Load scores from Shared Pref resource
        SharedPreferences spTopScores = getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);

        String uName;
        int uScore;
        for(int i = 0; i<5; i++){
            uName = spTopScores.getString(nameKey[i], "NULL");
            Log.d("showTopScores", "uName: " + uName);
            if(uName.matches("NULL")) break; // no more scores to display
            uScore = spTopScores.getInt(scoreKey[i], 0);
            TextView tvScore = findViewById(tvScoreResID[i]);
            tvScore.setText(uName + "  -  " + uScore);
        }
    }

    public void back(View view) {
        finish();
    }
}