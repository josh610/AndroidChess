package com.example.androidchess;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GameList implements Serializable {
    private static final long serialVersionUID = 5460574734623466101L;
    @SerializedName("GameList")
    private ArrayList<Game> gameList;
    public ArrayList<Game> getGameList() {
        return gameList;
    }
    public void setGameList(ArrayList<Game> games) {
        gameList = games;
    }
}
