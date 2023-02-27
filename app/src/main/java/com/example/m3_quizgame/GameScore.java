package com.example.m3_quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_score);
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        TextView tvScore = findViewById(R.id.tvGameScore);
        tvScore.setText(score + "");
    }

    public void back(View view){
        finish();
    }


    public void showTopScores(View view) {
        Intent intent = new Intent(this, ShowScores.class);
        startActivity(intent);
        finish();
    }
}