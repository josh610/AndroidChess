package com.example.androidchess.pieces;


import java.util.function.BiFunction;
import java.util.ArrayList;

/**
 * A representation of a chess piece
 *
 * @author Fionna Zhang
 * @author Joshua Zimmerman
 */
public abstract class PlayerPiece {
    /**
     * The coordinates of this piece
     */
    protected int[] coords;
    /**
     * The color of this piece
     */
    protected String color;
    /**
     * True if the piece has moved, false if the piece has not moved
     */
    protected boolean hasMoved;

    public PlayerPiece(){
    }
    /**
     * Creates a PlayerPiece with the specified color and coordinates
     *
     * @param color the color of the piece
     * @param f     the initial file of the piece
     * @param r     the initial rank of the piece sets hasMoved to false
     */
    public PlayerPiece(String color, int f, int r) {
        this.color = color;
        coords = new int[2];
        coords[0] = f;
        coords[1] = r;
        this.hasMoved = false;
    }

    /**
     * Converts the piece to a String
     */
    public abstract String toString();

    /**
     * Gets a list of the valid moves for a given piece
     *
     * @param p     the current piece
     * @param board the current game board
     * @return an ArrayList of int[]s containing all the valid moves for the current
     *         piece
     */
    public ArrayList<int[]> getMoves(PlayerPiece p, PlayerPiece[][] board) {
        return getFunc().apply(p, board);
    }

    /**
     * Gets this piece's color
     *
     * @return the color of this piece
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets this piece's coordinates
     *
     * @return the coordinates of this piece
     */
    public int[] getCoords() {
        return coords;
    }

    /**
     * Sets the coordinates of this piece
     *
     * @param f the new file of this piece
     * @param r the new rank of this piece
     */
    public void setCoords(int f, int r) {
        coords[0] = f;
        coords[1] = r;
    }

    /**
     * Retrieves the function that determines which moves are valid for a particular
     * piece
     *
     * @return a BiFunction interface that gets the valid moves for a piece
     */
    public abstract BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> getFunc();

    /**
     * Gets the hasMoved field for this piece
     *
     * @return true if the piece has moved, false if the piece has not moved
     */
    public boolean hasItMoved() {
        return hasMoved;
    }

    /**
     * Sets hasMoved to true
     */
    public void itHasMoved() {
        hasMoved = true;
    }
}