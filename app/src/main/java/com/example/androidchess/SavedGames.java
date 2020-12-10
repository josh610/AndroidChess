package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * List of in-progress games.
 * Allows user to choose a game to play/edit/delete
 */

public class SavedGames extends AppCompatActivity{

    private ListView listView;
    //ArrayList of finished games
    private static ArrayList<Game> games;

    public static final int RENAME_GAME_CODE = 1;
    public static final int PLAY_GAME_CODE = 2;

    /**
     * I read somewhere that this class needs to have a declared
     * constructor for something (I think serialization?) to work.
     * This constructor might not need to be here.
     */
    public SavedGames(){}

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        //activate up arrow to Home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.previous_games_list);

        //read ArrayList of current games from Json String
        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("previous_games");
        }
        games = new Gson().fromJson(jsonGames, ArrayList.class);

        listView = (ListView) findViewById(R.id.previous_games_list);
        listView.setAdapter(new ArrayAdapter<Game>(this, R.layout.game, games));

        // show game for possible edit when tapped
        listView.setOnItemClickListener((p, V, pos, id) -> editGame(pos));
    }

    /**
     * Shows info for selected game including option to play, rename, or delete
     * Starts EditGame activity
     * @param pos position of game in ArrayList/ListView
     */
    private void editGame(int pos){
        Bundle bundle = new Bundle();
        Game game = games.get(pos);
        bundle.putInt(EditGame.GAME_INDEX, pos);
        bundle.putString(EditGame.GAME_NAME, game.getName());
        Intent intent = new Intent(this, EditGame.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RENAME_GAME_CODE);
    }

    /**
     * Search list for game of the same name.
     * @param name
     * @return
     */
    public static boolean containsName(String name){
        for(int i = 0; i<games.size(); i++){
            if(games.get(i).getName().equals(name)){
                return true;
            }
        }

        return false;
    }

    /**
     * Static method to be called by other classes to add a new saved game,
     * or to update the saved state of a game
     * @param game
     */
    public static void addGame(Game game){
        games.add(game);
    }

    public static void deleteGame(Game game){
        for(int i = 0; i<games.size(); i++){
            if(games.get(i).getName().equals(game.getName())){
                games.remove(i);
            }
        }
    }
}