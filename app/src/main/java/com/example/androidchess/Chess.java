package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidchess.pieces.*;
import com.example.androidchess.Game;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Game screen.
 * User can move pieces, request a draw, resign, or quit.
 * The quit option prompts the user to save the in-progress game.
 * Once the game is over, the user is prompted to save the finished game.
 */
public class Chess extends AppCompatActivity {

    private Game game;
    private PlayerPiece selectedPiece;
    private PlayerPiece[][] currentGameBoard = new PlayerPiece[8][8];
    private boolean pieceSelected;

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
        game.initBoard(currentGameBoard);
        pieceSelected = false;
        for (int i = 0; i < 8; i++) {
            TableRow currRow = (TableRow) chess_board.getChildAt(i);
            for (int j = 0; j < 8; j++) {
                ImageView currView = (ImageView) currRow.getChildAt(j);
                currView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        boardClicked(v);
                    }
                });
            }
        }
    }

    private void boardClicked(View v) {
        PlayerPiece prevPiece = selectedPiece;
        int prevFile = -1;
        int prevRank = -1;
        if (prevPiece != null) {
            prevFile = prevPiece.getCoords()[0];
            prevRank = prevPiece.getCoords()[1];
        }
        int currFile = 0;
        int currRank = 0;
        TableRow currRow = (TableRow) v.getParent();
        currFile = currRow.indexOfChild(v);
        currRank = chess_board.indexOfChild(currRow);
        selectedPiece = currentGameBoard[currFile][currRank];
        if (selectedPiece == null) {
            Toast.makeText(Chess.this, "Not a valid piece", Toast.LENGTH_SHORT).show();
        } else {
            if (!pieceSelected) {
                pieceSelected = true;
                updateMoves(true);
            } else {
                //pieceSelected == true
                if (prevPiece == selectedPiece) {
                    pieceSelected = false;
                    updateMoves(false);
                } else {
                    //prevPiece != selectedPiece
                    if (prevPiece != null) {
                        //movePiece(prevFile, prevRank, currFile, currRank);
                    }
                }
                pieceSelected = false;
                updateMoves(false);
            }
        }

    }

    private void movePiece(int prevFile, int prevRank, int currFile, int currRank) {
        ArrayList<int[]> wCheckSpaces = game.getwCheckSpaces();
        ArrayList<int[]> bCheckSpaces = game.getbCheckSpaces();
        PlayerPiece wKing = game.getwKing();
        PlayerPiece bKing = game.getbKing();
        String currColor = game.getCurrPlayer();
        //boolean pieceMoved = game.playerMove(currentGameBoard, wCheckSpaces, bCheckSpaces, wKing, bKing, );
    }

    private void updateMoves(boolean showMoves) {
        if (!showMoves) {
            for (int i = 0; i < 8; i++) {
                TableRow currRow = (TableRow) chess_board.getChildAt(i);
                for (int j = 0; j < 8; j++) {
                    ImageView currView = (ImageView) currRow.getChildAt(j);
                    if ((i + j + 1) % 2 == 0) {
                        currView.setBackgroundColor(ContextCompat.getColor(Chess.this, R.color.brown_tile));

                    } else {
                        currView.setBackgroundColor(ContextCompat.getColor(Chess.this, R.color.white_tile));
                    }
                }
            }
        } else {
            ArrayList<int[]> currMoves = selectedPiece.getMoves(selectedPiece, currentGameBoard);
            for (int i = 0; i < 8; i++) {
                TableRow currRow = (TableRow) chess_board.getChildAt(i);
                for (int j = 0; j < 8; j++) {
                    ImageView currView = (ImageView) currRow.getChildAt(j);
                    int[] temp = {j, i};
                    if (Game.isInList(currMoves, temp)) {
                        if ((i + j + 1) % 2 == 0) {
                            currView.setBackgroundColor(ContextCompat.getColor(Chess.this, R.color.brown_tile_valid));
                        } else {
                            currView.setBackgroundColor(ContextCompat.getColor(Chess.this, R.color.white_tile_valid));
                        }
                    }
                }
            }
        }
    }

    private void quit(){
        showQuitDialog();
    }

    /**
     * bhdebjcebrfnc
     */
    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            returnToHome();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            saveGame();
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
     * Saves game
     */
    private void saveGame(){
        Intent intent = new Intent(this, SaveGame.class);
        intent.putExtra(Home.GAME, new Gson().toJson(game));
        startActivity(intent);

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