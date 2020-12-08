package com.example.androidchess;

import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveFinishedGame extends AppCompatActivity {

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

        //read ArrayList of current games from Json String
        String jsonGames = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            jsonGames = extras.getString("game");
        }
        game = new Gson().fromJson(jsonGames, Game.class);

        game.isSaved = true;
    }

    public void cancel(View v){
        finish();
    }

    public void save(){


        finish();
    }
}