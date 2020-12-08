package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contains all game logic.
 */
class Game implements Serializable {

    private String name;
    private LocalDateTime date;

    //Records all moves in the game ("White D2->E4", "Black B1->G7 Queen", "White Resign")
    private ArrayList<String> moves;

    //Whether or not this game has been saved
    public boolean isSaved = false;

    //This is just for testing purposes
    public int value = 0;

    public String playerMove = "White's move";

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}

/**
 * Game screen.
 * User can move pieces, request a draw, resign, or quit.
 * The quit option prompts the user to save the in-progress game.
 * Once the game is over, the user is prompted to save the finished game.
 */

public class Chess extends AppCompatActivity {

    private int GAME_STATUS = 0;
    private Game game;

    TextView playersMove, gameValue;
    Button incr, resign, quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        playersMove = findViewById(R.id.playersMove);
        gameValue = findViewById(R.id.chessVal);
        incr = findViewById(R.id.increment);
        resign = findViewById(R.id.resign);
        quit = findViewById(R.id.quit);

        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.value++;
                if(game.playerMove == "White's move") game.playerMove = "Black's move";
                else game.playerMove = "White's move";
                playersMove.setText(game.playerMove);
                gameValue.setText(game.value);

                if(game.value == 5){
                    endGame(1);
                }
            }
        });
        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame(2);
            }
        });
        quit.setOnClickListener(v -> quit());
    }

    /**
     * Quit game. Prompt user to save game
     */
    public void quit(){
        int saveGame = 0;

        //Do save game
        if(saveGame == 1){
            //game is already in current games list
            if(game.isSaved){
                CurrentGames.updateGame(game);
            }

            //save new game
            else{
                //Add game to bundle
                Intent intent = new Intent(this, Game.class);
                intent.putExtra(Home.GAME, new Gson().toJson(game));
                startActivity(intent);
            }

            Home.saveState();

            //Clear back stack
            Intent intent = new Intent(this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }

    /**
     * End of game. Win, draw, resign, etc
     * Prompts user to save game
     * @param endGameCode
     */
    public void endGame(int endGameCode){

    }
}