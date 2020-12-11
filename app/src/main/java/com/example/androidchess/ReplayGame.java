package com.example.androidchess;

import android.os.Bundle;

import com.example.androidchess.pieces.Bishop;
import com.example.androidchess.pieces.King;
import com.example.androidchess.pieces.Knight;
import com.example.androidchess.pieces.Pawn;
import com.example.androidchess.pieces.PlayerPiece;
import com.example.androidchess.pieces.Queen;
import com.example.androidchess.pieces.Rook;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.androidchess.Home.CURRENT_GAME;
import static com.example.androidchess.Home.SAVED_GAMES;

public class ReplayGame extends AppCompatActivity {

    private Game game;
    private ArrayList<String> moves;
    private ArrayList<Game> gameList;
    private int moveIndex;
    TextView playerMove;
    Button nextMove;
    TableLayout replay_chess_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_game);

        //activate up arrow to ReplayEditGame
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playerMove = findViewById(R.id.player_move);
        nextMove = findViewById(R.id.next_move);
        replay_chess_board = findViewById((R.id.replay_chess_board));

        //read Game object from Json String
        String jsonGame = "";
        String jsonGameList = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGame = extras.getString(CURRENT_GAME);
            jsonGameList = extras.getString(SAVED_GAMES);
        }
        game = new Gson().fromJson(jsonGame, Game.class);
        GameList list = new Gson().fromJson(jsonGameList, GameList.class);
        gameList = list.getGameList();

        nextMove.setOnClickListener(v -> showNextMove());
        moveIndex = 0;
    }


    private void showNextMove(){
        if (moveIndex >= game.getMovesList().size()) {
            Toast.makeText(ReplayGame.this, "No moves left", Toast.LENGTH_SHORT).show();
            return;
        }
        updatePlayerMove();
        game.executeMove(game.getMovesList().get(moveIndex));
        updateBoard();
        moveIndex++;
    }

    private void updateBoard() {
        for (int i = 0; i < 8; i++) {
            TableRow currRow = (TableRow) replay_chess_board.getChildAt(7-i);
            for (int j = 0; j < 8; j++) {
                ImageView currView = (ImageView) currRow.getChildAt(j);
                PlayerPiece p = game.getBoard()[j][i];
                if (p == null) {
                    currView.setImageResource(android.R.color.transparent);
                } else if (p instanceof Pawn) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_pawn));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_pawn));
                    }
                } else if (p instanceof Rook) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_rook));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_rook));
                    }
                } else if (p instanceof Knight) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_knight));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_knight));
                    }
                } else if (p instanceof Bishop) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_bishop));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_bishop));
                    }
                } else if (p instanceof Queen) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_queen));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_queen));
                    }
                } else if (p instanceof King) {
                    if (p.getColor().equals("White")) {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.white_king));
                    } else {
                        currView.setImageDrawable(ContextCompat.getDrawable(ReplayGame.this, R.drawable.black_king));
                    }
                }
            }
        }
    }

    /**
     * Sets the playerMover TextView to the correct player's move
     */
    private void updatePlayerMove(){
        playerMove.setText(game.getCurrPlayer() + "'s Move");
    }

    /**
     * Updates chess board to current move
     */
    private void updateMove(){
        //TODO
    }
}