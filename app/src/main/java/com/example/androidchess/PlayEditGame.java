package com.example.androidchess;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

/**
 * Allows user to continue playing selected in-progress game,
 * rename selected game, or delete selected game.
 */

public class PlayEditGame extends AppCompatActivity {

    public static final String GAME_INDEX = "gameIndex";
    public static final String GAME_NAME = "name";

    private int gameIndex;
    private EditText rename;
    private Button play, ok, delete;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_edit_game);

        //activate up arrow to CurrentGames
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get fields
        rename = findViewById(R.id.rename);
        ok = findViewById(R.id.ok);
        delete = findViewById(R.id.delete);

        //see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gameIndex = bundle.getInt(GAME_INDEX);
            rename.setText(bundle.getString(GAME_NAME));
        }
    }

    /**
     * Cancels edit and returns to current games list
     * Not sure if this method corresponds to the up arrow or if I don't need it
     * @param v
     */
    public void cancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Continue playing selected game
     * Starts Game activity
     * @param v
     */
    public void playGame(View v){

    }

    /**
     * Rename selected game
     * @param v
     */
    public void saveName(View v){
        //gather data from text field
        String newName = rename.getText().toString();

        if(newName == null || newName.length() == 0){

        }
    }

    /**
     * Delete selected game
     * @param v
     */
    public void delete(View v){

    }
}