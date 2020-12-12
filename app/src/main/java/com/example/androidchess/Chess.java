package com.example.androidchess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.androidchess.Home.CURRENT_GAME;
import static com.example.androidchess.Home.SAVED_GAMES;

/**
 * Game screen.
 * User can move pieces, request a draw, resign, or quit.
 * The quit option prompts the user to save the in-progress game.
 * Once the game is over, the user is prompted to save the finished game.
 */
public class Chess extends AppCompatActivity {

    private GameList list;
    private ArrayList<Game> gameList;
    private String gameName;
    private Game game;
    private PlayerPiece prevPiece;
    private PlayerPiece selectedPiece;
    private ArrayList<String> currentGameMoves = new ArrayList<String>();
    private PlayerPiece[][] currentGameBoard = new PlayerPiece[8][8];
    private boolean pieceSelected;
    private char promotionChar;
    private int[] promotionCoords;
    private boolean undoAllowed;

    TextView playersMove;
    Button resign, draw;
    Button undo, ai_move;

    //test buttons
    Button win;

    TableLayout chess_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        playersMove = findViewById(R.id.playersMove);
        resign = findViewById(R.id.resign);
        draw = findViewById(R.id.draw);
        undo = findViewById(R.id.undo);
        ai_move = findViewById(R.id.ai_move);


        String jsonGame = "";
        String jsonGameList = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGame = extras.getString(CURRENT_GAME);
            jsonGameList = extras.getString(SAVED_GAMES);
        }
        game = new Gson().fromJson(jsonGame, Game.class);
        list = new Gson().fromJson(jsonGameList, GameList.class);
        gameList = list.getGameList();

        resign.setOnClickListener(v -> resignGame());
        draw.setOnClickListener(v -> drawGamePrompt());
        undo.setOnClickListener(v -> undoMove());
        ai_move.setOnClickListener(v -> aiMove());


        chess_board = findViewById(R.id.chess_board);
        initializeGame();

    }

    private void initializeGame() {
        game.setDate(LocalDateTime.now());
        game.initBoard(currentGameBoard);
        game.setMovesList(currentGameMoves);
        prevPiece = null;
        selectedPiece = null;
        pieceSelected = false;
        undoAllowed = false;
        promotionChar = '0';
        promotionCoords = new int[2];
        updateBoardPieces();
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
        prevPiece = selectedPiece;
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
        currRank = 7 - chess_board.indexOfChild(currRow);
        //System.out.println("currFile: " + currFile + ", currRank: " + currRank);
        selectedPiece = currentGameBoard[currFile][currRank];

        //add castle to moveset
        game.checkForCastle(currentGameBoard, game.getwCheckSpaces(), game.getbCheckSpaces(), game.getwKing(), game.getbKing());


        if (prevPiece == null && selectedPiece == null) {
            Toast.makeText(Chess.this, "Not a valid piece", Toast.LENGTH_SHORT).show();
        } else {
            if (!pieceSelected && selectedPiece == null) {
                Toast.makeText(Chess.this, "Not a valid piece", Toast.LENGTH_SHORT).show();
            }
            if (!pieceSelected && selectedPiece != null) {
                if (!selectedPiece.getColor().equals(game.getCurrPlayer())) {
                    Toast.makeText(Chess.this, "Wrong color", Toast.LENGTH_SHORT).show();
                } else {
                    //add en passant to moveset
                    game.checkForEnPassant(currentGameBoard, selectedPiece, currFile, currRank);
                    pieceSelected = true;
                    updateMoves(true);
                }
            } else {
                //pieceSelected == true

                //same piece - put the piece back down
                if (prevPiece == selectedPiece) {
                    pieceSelected = false;
                    updateMoves(false);
                } else {
                    //prevPiece != selectedPiece
                    //check for valid move
                    //prevPiece is the location of the currently selected piece
                    //selectedPiece is the coordinates of the desired move

                    if (prevPiece != null) {
                        //selected destination not in moveset
                        if (!Game.isInList(prevPiece.getMoves(prevPiece, currentGameBoard), new int[] {currFile, currRank})) {
                            selectedPiece = prevPiece;
                            pieceSelected = true;
                            Toast.makeText(Chess.this, "Invalid move", Toast.LENGTH_SHORT).show();
                        }
                        //check for promotion
                        if (prevPiece instanceof Pawn) {

                            //if promotionChar == '0', do nothing

                            if (prevPiece.getColor().equals("White") && currRank == 7) {
                                if (Game.isInList(prevPiece.getMoves(prevPiece, currentGameBoard), new int[] {currFile, currRank})) {
                                    promotionCoords[0] = currFile;
                                    promotionCoords[1] = currRank;
                                    showPromotion();
                                    return;
                                }
                            } else if (prevPiece.getColor().equals("Black") && currRank == 0) {
                                if (Game.isInList(prevPiece.getMoves(prevPiece, currentGameBoard), new int[] {currFile, currRank})) {
                                    promotionCoords[0] = currFile;
                                    promotionCoords[1] = currRank;
                                    showPromotion();
                                    return;
                                }
                            }
                        }

                            movePiece(prevFile, prevRank, currFile, currRank);
                            pieceSelected = false;
                            updateMoves(false);
                            updateBoardPieces();

                    }
                }
                pieceSelected = false;
                updateMoves(false);
                King wKing = (King) game.getwKing();
                King bKing = (King) game.getbKing();
                if (wKing.getCheckStatus().equals("Check") || bKing.getCheckStatus().equals("Check")) {
                    Toast.makeText(Chess.this, "Check", Toast.LENGTH_SHORT).show();
                } else if (wKing.getCheckStatus().equals("Checkmate")) {
                    game.setGameStatus(2);
                    endGame(2);
                } else if (bKing.getCheckStatus().equals("Checkmate")) {
                    game.setGameStatus(1);
                    endGame(1);
                }
            }
        }

    }
    private void showPromotion() {
        char[] promotion = new char[1];
        promotion[0] = 'Q';
        String[] promotionOptions = new String[] {
                "Queen",
                "Knight",
                "Bishop",
                "Rook"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this);
        builder.setTitle("Promotion");

        builder.setSingleChoiceItems(promotionOptions, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                promotion[0] = 'Q';
                            case 1:
                                promotion[0] = 'N';
                            case 2:
                                promotion[0] = 'B';
                            case 3:
                                promotion[0] = 'R';
                        }
                    }
                })
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        executePromotion(promotion[0]);
                        dialog.dismiss();
                    }
                })
                ;
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void executePromotion(char promotion) {
        promotionChar = promotion;
        int prevFile = prevPiece.getCoords()[0];
        int prevRank = prevPiece.getCoords()[1];
        int currFile = promotionCoords[0];
        int currRank = promotionCoords[1];
        String move = Game.intToMove(prevFile, prevRank, currFile, currRank, promotionChar);
        boolean moved = game.playerMove(currentGameBoard, game.getwCheckSpaces(), game.getbCheckSpaces(), game.getwKing(), game.getbKing(), game.getCurrPlayer(), move);
        if (!moved) {
            Toast.makeText(Chess.this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            if (game.getCurrPlayer().equals("White")) {
                game.setCurrPlayer("Black");
                playersMove.setText("Black's Move");
            } else {
                game.setCurrPlayer("White");
                playersMove.setText("White's Move");
            }
            undoAllowed = true;
        }
    }
    private void movePiece(int prevFile, int prevRank, int currFile, int currRank) {
        //System.out.println("movePiece");
        ArrayList<int[]> wCheckSpaces = game.getwCheckSpaces();
        ArrayList<int[]> bCheckSpaces = game.getbCheckSpaces();
        PlayerPiece wKing = game.getwKing();
        PlayerPiece bKing = game.getbKing();
        String currColor = game.getCurrPlayer();
        String currMove = Game.intToMove(prevFile, prevRank, currFile, currRank, '0');
        boolean pieceMoved = game.playerMove(currentGameBoard, wCheckSpaces, bCheckSpaces, wKing, bKing, currColor, currMove);
        if (!pieceMoved) {
            Toast.makeText(Chess.this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            if (game.getCurrPlayer().equals("White")) {
                game.setCurrPlayer("Black");
                playersMove.setText("Black's Move");
            } else {
                game.setCurrPlayer("White");
                playersMove.setText("White's Move");
            }
            undoAllowed = true;
        }
    }

    private void updateBoardPieces() {
        for (int i = 0; i < 8; i++) {
            TableRow currRow = (TableRow) chess_board.getChildAt(7-i);
            for (int j = 0; j < 8; j++) {
                ImageView currView = (ImageView) currRow.getChildAt(j);
                PlayerPiece p = currentGameBoard[j][i];
                if (p == null) {
                    currView.setImageResource(android.R.color.transparent);
                } else if (p instanceof Pawn) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_pawn));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_pawn));
                    }
                } else if (p instanceof Rook) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_rook));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_rook));
                    }
                } else if (p instanceof Knight) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_knight));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_knight));
                    }
                } else if (p instanceof Bishop) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_bishop));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_bishop));
                    }
                } else if (p instanceof Queen) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_queen));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_queen));
                    }
                } else if (p instanceof King) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.white_king));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(Chess.this, R.drawable.black_king));
                    }
                }
            }
        }
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
                TableRow currRow = (TableRow) chess_board.getChildAt(7-i);
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

    private void undoMove() {
        if (undoAllowed) {
            game.undoMove(currentGameBoard);
            String displayMove = game.getCurrPlayer() + "'s Move";
            playersMove.setText(displayMove);
            updateBoardPieces();
            updateMoves(false);
            pieceSelected = false;
            undoAllowed = false;
        } else {
            pieceSelected = false;
            updateMoves(false);
            Toast.makeText(Chess.this, "Undo not allowed", Toast.LENGTH_SHORT).show();
        }
    }
    private void aiMove() {
        boolean moved = game.randomMove(currentGameBoard);
        int moveCounter = 0;
        if (!moved) {
            Toast.makeText(Chess.this, "Couldn't find legal move", Toast.LENGTH_SHORT).show();
        } else {
            updateBoardPieces();
            if (game.getCurrPlayer().equals("White")) {
                game.setCurrPlayer("Black");
            } else {
                game.setCurrPlayer("White");
            }
            String displayMove = game.getCurrPlayer() + "'s Move";
            playersMove.setText(displayMove);
            undoAllowed = true;
        }
    }
    private void resignGame() {
        pieceSelected = false;
        updateMoves(false);
        game.resignGame();
        if (game.getCurrPlayer().equals("White")) {
            endGame(2);
        } else {
            endGame(1);
        }
    }
    private void drawGamePrompt() {
        pieceSelected = false;
        updateMoves(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this);
        builder.setTitle("Draw?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();
            game.setGameStatus(-1);
            game.drawGame();
            endGame(-1);
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
    private void endGame(int endGameCode){
        game.setDate(LocalDateTime.now());
        System.out.println(game.getMovesList());
        String prompt = "";
        if (endGameCode == 1) {
            prompt = "White wins! ";
        } else if (endGameCode == 2) {
            prompt = "Black wins! ";
        } else if (endGameCode == -1) {
            prompt = "Draw game. ";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this);
        builder.setTitle(prompt+ "Would you like to save this game?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            saveGame();
            //returnToHome();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Chess.this);
        builder.setTitle("Enter game name");

        final EditText input = new EditText(Chess.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameName = input.getText().toString();
                if (gameName.isEmpty()) {
                    Toast.makeText(Chess.this, "Please enter a game name", Toast.LENGTH_SHORT).show();
                } else {
                    game.setName(gameName);
                    //gameList.add(game);
                    list.getGameList().add(game);
                    dialog.dismiss();
                    returnToHome();
                    try {
                        Home.writeApp(getApplicationContext(), list);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Brings user back to home screen
     */
    private void returnToHome(){
        System.out.println("Returning to home screen");
        try {
            Home.writeApp(getApplicationContext(), list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Clear back stack (?)
        Intent intent = new Intent(this, Home.class);
        intent.putExtra(SAVED_GAMES, new Gson().toJson(list));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}