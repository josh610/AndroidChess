package com.example.androidchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Home screen for the chess app.
 * Contains options to start a new game, choose from an existing
 * game, or view a previous game.
 */

public class Home extends AppCompatActivity implements Serializable{

    public static final String GAME = "game";
    public static final String CURRENT_GAMES = "current_games";
    public static final String PREVIOUS_GAMES = "previous_games";

    private static ArrayList<Game> current_games;
    private static ArrayList<Game> previous_games;

    Button newGame, contGame, prevGames;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //read current_games and previous_games
        try {
            readApp();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        newGame = findViewById(R.id.new_game);
        contGame = findViewById(R.id.cont_game);
        prevGames = findViewById(R.id.prev_games);

        if(current_games == null){
            current_games = new ArrayList<Game>();
        }

        if(previous_games == null){
            previous_games = new ArrayList<Game>();
        }

        //Match buttons with their ActionEvents
        newGame.setOnClickListener(v -> startNewGame());
        contGame.setOnClickListener(v -> showCurrentGames());
        prevGames.setOnClickListener(v -> showPreviousGames());
    }

    /**
     * Begin a new game.
     * Starts Chess activity
     */
    private void startNewGame() {
        //Create new Game and add it to bundle
        Intent intent = new Intent(this, Chess.class);
        intent.putExtra(GAME, new Gson().toJson(new Game()));
        startActivity(intent);
    }

    /**
     * Show list of in-progress games
     * Starts CurrentGames activity
     */
    private void showCurrentGames(){
        //Pass current_games ArrayList to CurrentGames as a Json String
        Intent intent = new Intent(this, CurrentGames.class);
        intent.putExtra(CURRENT_GAMES, new Gson().toJson(current_games));
        startActivity(intent);
    }

    /**
     * Show list of previous games
     * Starts PreviousGames activity
     */
    private void showPreviousGames(){
        //Pass previous_games ArrayList to PreviousGames as a Json String
        Intent intent = new Intent(this, PreviousGames.class);
        intent.putExtra(PREVIOUS_GAMES, new Gson().toJson(previous_games));
        startActivity(intent);
    }

    /**
     * Saves current state of current and previous games
     */
    public static void saveState(){
        try {
            writeApp();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves current_games and previous_games to curGames.txt and prevGames.txt, respectively
     * .txt files are located in res/raw
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void writeApp() throws IOException, ClassNotFoundException{
        FileOutputStream cur_fos = new FileOutputStream("R.raw.cur_games.txt");
        ObjectOutputStream cur_oos = new ObjectOutputStream(cur_fos);
        cur_oos.writeObject(current_games);
        cur_oos.flush();
        cur_oos.close();

        FileOutputStream prev_fos = new FileOutputStream("R.raw.prev_games.txt");
        ObjectOutputStream prev_oos = new ObjectOutputStream(prev_fos);
        prev_oos.writeObject(previous_games);
        prev_oos.flush();
        prev_oos.close();
    }

    /**
     * Loads current_games and previous_games from curGames.txt and prevGames.txt, respectively
     * .txt files are located in res/raw
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void readApp() throws IOException, ClassNotFoundException{
        FileInputStream cur_fis = new FileInputStream("R.raw.cur_games.txt");
        ObjectInputStream cur_ois = new ObjectInputStream(cur_fis);
        current_games = (ArrayList<Game>) cur_ois.readObject();
        cur_ois.close();

        FileInputStream prev_fis = new FileInputStream("R.raw.prev_games.txt");
        ObjectInputStream prev_ois = new ObjectInputStream(prev_fis);
        previous_games = (ArrayList<Game>) prev_ois.readObject();
        prev_ois.close();
    }
}