package com.example.androidchess;

import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveGame extends AppCompatActivity {

    private Game game;

    EditText name;
    private Button save, nvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        name = findViewById(R.id.enter_new_name);
        save = findViewById(R.id.save);
        nvm = findViewById(R.id.nvm);

        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("previous_games");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        save.setOnClickListener(v -> save());
        nvm.setOnClickListener(v -> nvm());
    }

    /**
     * Save game
     */
    private void save(){
        try{
            game.setName(name.getText().toString());
            SavedGames.addGame(game);

            finish();
        }
        catch(Exception e){
            Toast.makeText(SaveGame.this, "Please enter a valid name", Toast.LENGTH_LONG).show();
        }

        finish();
    }

    /**
     * Never mind game
     */
    private void nvm(){
        finish();
    }
}