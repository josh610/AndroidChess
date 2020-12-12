package com.example.androidchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Home screen for the chess app.
 * Contains options to start a new game, choose from an existing
 * game, or view a previous game.
 */

public class Home extends AppCompatActivity implements Serializable{

    public static final String CURRENT_GAME = "game_in_progress";
    public static final String SAVED_GAMES = "saved_games";

    private static ArrayList<Game> saved_games = new ArrayList<>();
    private static GameList list = new GameList();

    Button newGame, savedGames;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //read saved_games
        try {
            readApp(getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Game game = new Game();
        game.setName("Game 1");
        game.setDate(LocalDateTime.now());
        saved_games.add(game);
        list.setGameList(saved_games);
        String jsonGameList = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGameList = extras.getString(SAVED_GAMES);
            list = new Gson().fromJson(jsonGameList, GameList.class);
            saved_games = list.getGameList();
        }

        newGame = findViewById(R.id.new_game);
        savedGames = findViewById(R.id.saved_games);

        //Match buttons with their ActionEvents
        newGame.setOnClickListener(v -> startNewGame());
        savedGames.setOnClickListener(v -> showSavedGames());
        try {
            System.out.println(this.getFilesDir().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Begin a new game.
     * Starts Chess activity
     */
    private void startNewGame() {
        //Create new Game and add it to bundle
        Intent intent = new Intent(this, Chess.class);
        intent.putExtra(CURRENT_GAME, new Gson().toJson(new Game()));
        intent.putExtra(SAVED_GAMES, new Gson().toJson(list));
        startActivity(intent);
    }

    /**
     * Show list of previous games
     * Starts PreviousGames activity
     */
    private void showSavedGames(){
        //Pass saved_games ArrayList to PreviousGames as a Json String

        Intent intent = new Intent(this, SavedGames.class);
        intent.putExtra(SAVED_GAMES, new Gson().toJson(list));
        startActivity(intent);
    }

    /**
     * Saves current state of application
     */
    public static void saveState(Context context){
        try {
            writeApp(context, list);
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
    public static void writeApp(Context context, GameList list) throws IOException, ClassNotFoundException{

        FileOutputStream fos = context.openFileOutput("saved_games.dat", MODE_APPEND);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(list.getGameList());
        oos.flush();
        oos.close();
    }

    /**
     * Loads saved_games from savedGames.txt
     * .txt files are located in res/raw
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readApp(Context context) throws IOException, ClassNotFoundException{
        FileInputStream fis = context.openFileInput("saved_games.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        saved_games = (ArrayList<Game>) ois.readObject();
        list.setGameList(saved_games);
        ois.close();
    }
}