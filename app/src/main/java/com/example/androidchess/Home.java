package com.example.androidchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    public static final String SAVED_GAMES = "saved_games";

    private static ArrayList<Game> saved_games;

    Button newGame, savedGames;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //read current_games and saved_games
        try {
            readApp();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        newGame = findViewById(R.id.new_game);
        savedGames = findViewById(R.id.saved_games);

        if(saved_games == null){
            saved_games = new ArrayList<Game>();
        }

        //Match buttons with their ActionEvents
        newGame.setOnClickListener(v -> startNewGame());
        savedGames.setOnClickListener(v -> showSavedGames());
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
     * Show list of previous games
     * Starts PreviousGames activity
     */
    private void showSavedGames(){
        //Pass saved_games ArrayList to PreviousGames as a Json String
        Intent intent = new Intent(this, SavedGames.class);
        intent.putExtra(SAVED_GAMES, new Gson().toJson(saved_games));
        startActivity(intent);
    }

    /**
     * Saves current state of application
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
     * Saves saved_games to savedGames.txt
     * .txt files are located in res/raw
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void writeApp() throws IOException, ClassNotFoundException{
        FileOutputStream fos = new FileOutputStream("savedGames.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(saved_games);
        oos.flush();
        oos.close();
    }

    /**
     * Loads saved_games from savedGames.txt
     * .txt files are located in res/raw
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void readApp() throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream("savedGames.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        saved_games = (ArrayList<Game>) ois.readObject();
        ois.close();
    }
}