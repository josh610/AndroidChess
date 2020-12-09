package com.example.androidchess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

    private String playerMove = "White's move";

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPlayerMove(){
        return playerMove;
    }

    public void setPlayerMove(String playerMove){
        this.playerMove = playerMove;
    }
}

/**
 * Game screen.
 * User can move pieces, request a draw, resign, or quit.
 * The quit option prompts the user to save the in-progress game.
 * Once the game is over, the user is prompted to save the finished game.
 */

public class Chess extends AppCompatActivity {

    private Game game;

    TextView playersMove, gameValue;
    Button increment, resign, quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        playersMove = findViewById(R.id.playersMove);
        gameValue = findViewById(R.id.gameValue);
        increment = findViewById(R.id.increment);
        resign = findViewById(R.id.resign);
        quit = findViewById(R.id.quit);

        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.value++;
                if(game.getPlayerMove().equals("White's move")) game.setPlayerMove("Black's move");
                else game.setPlayerMove("White's move");
                playersMove.setText(game.getPlayerMove());
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
        quit.setOnClickListener(v -> showQuitDialog());
    }

    /**
     * Launches AlertDialog popup asking whether or not to save game
     */
    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext()); //I'm not sure if getApplicationContext() is the correct context here
        builder.setTitle("Would you like to save this game?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            saveInProgressGame();
            returnToHome();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
            returnToHome();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Called by showQuitDialog() if user saves game
     */
    private void saveInProgressGame(){
        if(game.isSaved){
            CurrentGames.updateGame(game);
        }

        //save new game
        else{
            //Add game to bundle
            Intent intent = new Intent(this, SaveNewGame.class);
            intent.putExtra(Home.GAME, new Gson().toJson(game));
            startActivity(intent);
        }

        //Saves state of the application after saving the game
        Home.saveState();
    }

    /**
     * End of game. Win, draw, resign, etc
     * Launches AlertDialog popup asking whether or not to save game
     *
     * Optimally I'd like to make this a fragment instead of an AlertDialog so that
     * I can have text up top saying who won, and then another window asking whether
     * or not to save the game
     */
    public void endGame(int endGameCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext()); //I'm not sure if getApplicationContext() is the correct context here
        builder.setTitle("You won! Would you like to save this game?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            saveFinishedGame();
            returnToHome();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
            returnToHome();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Saves a finished game to PreviousGames
     * Also removes game from CurrentGames if it's there, since it's no longer
     * an in-progress game
     */
    private void saveFinishedGame(){
        if(CurrentGames.containsName(game.getName())){
            CurrentGames.deleteGame(game);
        }

        PreviousGames.addGame(game);

        //Saves state of the application after saving the game
        Home.saveState();
    }

    /**
     * Brings user back to home screen
     */
    private void returnToHome(){
        //Clear back stack (?)
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}