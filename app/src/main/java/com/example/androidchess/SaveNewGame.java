package com.example.androidchess;

import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Saves a new game to CurrentGames
 */
public class SaveNewGame extends AppCompatActivity {

    Game game;
    EditText name;
    Button ok, nvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        name = findViewById(R.id.rename);
        ok = findViewById(R.id.ok);
        nvm = findViewById(R.id.nvm);

        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);
    }

    /**
     * Never mind
     * @param v
     */
    public void cancel(View v){
        finish();
    }

    /**
     * Saves new game with given name.
     * Raises a toast if no name is entered
     */
    public void save(){
        try{
            game.setName(name.getText().toString());
            game.isSaved = true;
            CurrentGames.addGame(game);

            finish();
        }
        catch(Exception e){
            Toast.makeText(SaveNewGame.this, "Please enter a valid name", Toast.LENGTH_LONG).show();
        }
    }
}