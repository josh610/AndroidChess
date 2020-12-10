package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Allows user to continue playing selected in-progress game,
 * rename selected game, or delete selected game.
 */

public class EditGame extends AppCompatActivity {

    public static final String GAME_INDEX = "gameIndex";
    public static final String GAME_NAME = "name";

    private Game game;
    private int gameIndex;
    private EditText rename;
    private Button replay, ok, delete;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        //activate up arrow to PreviousGames
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get fields
        replay = findViewById(R.id.replay);
        rename = findViewById(R.id.rename);
        ok = findViewById(R.id.ok);
        delete = findViewById(R.id.delete);

        //see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gameIndex = bundle.getInt(GAME_INDEX);
            rename.setText(bundle.getString(GAME_NAME));
        }

        //read Game object from Json String
        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        replay.setOnClickListener(v -> replayGame());
        ok.setOnClickListener(v -> saveName());
        delete.setOnClickListener(v -> delete());
    }

    /**
     * Cancels edit and returns to current games list
     * Not sure if this method corresponds to the up arrow or if I don't need it
     * @param v
     */
    private void cancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Continue playing selected game
     * Starts Game activity
     */
    private void replayGame(){
        Intent intent = new Intent(this, ReplayGame.class);
        intent.putExtra(Home.GAME, new Gson().toJson(game));
        startActivity(intent);
    }

    /**
     * Rename selected game
     */
    private void saveName(){
        try{
            game.setName(rename.getText().toString());
            SavedGames.addGame(game);

            finish();
        }
        catch(Exception e){
            Toast.makeText(EditGame.this, "Please enter a valid name", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called by delete button
     */
    private void delete(){
        showDeleteDialog();
    }

    private void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditGame.this); //I'm not sure if getApplicationContext() is the correct context here
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();;
            SavedGames.deleteGame(game);
        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}