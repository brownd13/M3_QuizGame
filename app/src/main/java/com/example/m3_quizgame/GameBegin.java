package com.example.m3_quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameBegin
        extends AppCompatActivity
        implements View.OnClickListener {

    public String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_begin);
        Intent intent = getIntent();
        UserName = intent.getStringExtra("USERNAME");

        // Setup button click listeners
        Button buStartGame = findViewById(R.id.buStartGame);
        Button buShowScore = findViewById(R.id.buShowScore);
        Button buRules     = findViewById(R.id.buRules);
        Button buQuit      = findViewById(R.id.buQuit);
        buStartGame.setOnClickListener(this);
        buShowScore.setOnClickListener(this);
        buRules.setOnClickListener(this);
        buQuit.setOnClickListener(this);
    }

    public void onClick(View view){
        if (view.getId() == R.id.buStartGame) {
            Intent intent = new Intent(this, GamePlay.class);
            intent.putExtra("USERNAME", UserName);
            startActivity(intent);
        } else if (view.getId() == R.id.buShowScore) {
            Intent intent = new Intent(this, ShowScores.class);
            startActivity(intent);
        } else if (view.getId() == R.id.buRules) {
            Intent intent = new Intent(this, ShowRules.class);
            startActivity(intent);
        } else if (view.getId() == R.id.buQuit) {
            finish();
        }
    }
}