package com.example.androidchess;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GameList {
    @SerializedName("gameList")
    private ArrayList<Game> gameList;
    public ArrayList<Game> getGameList() {
        return gameList;
    }
    public void setGameList(ArrayList<Game> games) {
        gameList = games;
    }
}
