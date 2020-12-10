package com.example.androidchess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Game screen.
 * User can move pieces, request a draw, resign, or quit.
 * The quit option prompts the user to save the in-progress game.
 * Once the game is over, the user is prompted to save the finished game.
 */
public class Chess extends AppCompatActivity {

    private Game game;

    TextView playersMove;
    Button resign, quit;

    //test buttons
    Button win, test;

    TableLayout chess_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        playersMove = findViewById(R.id.playersMove);
        resign = findViewById(R.id.resign);
        quit = findViewById(R.id.quit);

        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        resign.setOnClickListener(v -> endGame(2));
        quit.setOnClickListener(v -> quit());


        win = findViewById(R.id.win_game);
        test = findViewById(R.id.test);

        win.setOnClickListener(v -> win());
        test.setOnClickListener(v -> test());

        chess_board = findViewById(R.id.chess_board);
        initializeGame();

    }

    private void initializeGame() {
        game.initBoard();
        for (int i = 0; i < 8; i++) {
            TableRow currRow = (TableRow) chess_board.getChildAt(i);
            for (int j = 0; j < 8; j++) {
                ImageView currView = (ImageView) currRow.getChildAt(j);
                currView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        pieceClicked();
                    }
                });
            }
        }
    }
    private void pieceClicked() {

    }
    private void quit(){
        showQuitDialog();
    }

    /**
     * Launches AlertDialog popup asking whether or not to save game
     */
    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this); //I'm not sure if getApplicationContext() is the correct context here
        builder.setTitle("Would you like to save this game?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            System.out.println("This should print after the dialogue window is closed");
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
            System.out.println("Updating current game");
            CurrentGames.updateGame(game);
        }

        //save new game
        else{
            System.out.println("Add new game");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this); //I'm not sure if getApplicationContext() is the correct context here
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
        System.out.println("Returning to home screen");
        //Clear back stack (?)
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void win(){
        endGame(1);
    }

    private void test(){
        System.out.println("test");
        int n = Integer.parseInt(test.getText().toString());
        n++;
        test.setText(Integer.toString(n));
    }
}