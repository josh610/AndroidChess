package com.example.androidchess;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ReplayGame extends AppCompatActivity {

    private Game game;
    private ArrayList<String> moves;

    TextView playerMove;
    Button prevMove, nextMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_game);

        //activate up arrow to ReplayEditGame
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playerMove = findViewById(R.id.player_move);
        prevMove = findViewById(R.id.previous_move);
        nextMove = findViewById(R.id.next_move);

        //read Game object from Json String
        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        prevMove.setOnClickListener(v -> showPreviousMove());
        nextMove.setOnClickListener(v -> showNextMove());
    }

    private void showPreviousMove(){

        updatePlayerMove();
        //TODO
    }

    private void showNextMove(){

        updatePlayerMove();
        //TODO
    }

    /**
     * Sets the playerMover TextView to the correct player's move
     */
    private void updatePlayerMove(){
        playerMove.setText(game.getPlayerMove());
    }

    /**
     * Updates chess board to current move
     */
    private void updateMove(){
        //TODO
    }
}