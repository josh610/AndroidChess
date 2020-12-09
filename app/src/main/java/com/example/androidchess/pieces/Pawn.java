package com.example.androidchess.pieces;


import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * A representation of a pawn
 * @author Fionna Zhang
 * @author Joshua Zimmerman
 */
public class Pawn extends PlayerPiece {
    /**
     * Represents whether or not this pawn moved two spaces in the previous turn
     */
    boolean justMovedTwo;

    /**
     * Determines the valid moves for this pawn
     */
    BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> p = (p, board) -> {
        int[] coords = p.getCoords();
        int F = coords[0];
        int R = coords[1];
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int direction = 0;
        if (p.getColor().contentEquals("White"))
            direction = 1;
        else
            direction = -1;
        // can move two spaces on first move
        if (!p.hasMoved) {
            if (board[F][R + (2 * direction)] == null) {
                moves.add(new int[]{F, R + (2 * direction)});
            }
        }
        // can move one space forward (forward direction depends on color)
        if (direction == 1) {
            if (R + 1 < 8 && board[F][R + 1] == null) {
                moves.add(new int[]{F, R + 1});
            }
            if ((F - 1 >= 0 && R + 1 < 8)
                    && (board[F - 1][R + 1] != null && !board[F - 1][R + 1].color.contentEquals(p.getColor()))) {
                moves.add(new int[]{F - 1, R + 1});
            }
            if ((F + 1 < 8 && R + 1 < 8)
                    && (board[F + 1][R + 1] != null && !board[F + 1][R + 1].color.contentEquals(p.getColor()))) {
                moves.add(new int[]{F + 1, R + 1});
            }

        } else {
            if (R - 1 >= 0 && board[F][R - 1] == null) {
                moves.add(new int[]{F, R - 1});
            }
            if ((F - 1 >= 0 && R - 1 >= 0)
                    && (board[F - 1][R - 1] != null && !board[F - 1][R - 1].color.contentEquals(p.getColor()))) {
                moves.add(new int[]{F - 1, R - 1});
            }
            if ((F + 1 < 8 && R - 1 >= 0)
                    && (board[F + 1][R - 1] != null && !board[F + 1][R - 1].color.contentEquals(p.getColor()))) {
                moves.add(new int[]{F + 1, R - 1});
            }
        }
        return moves;
    };

    /**
     * Creates a Pawn with the specified color and coordinates
     *
     * @param color the color of the pawn
     * @param f     the initial file of the pawn
     * @param r     the initial rank of the pawn
     *              sets justMovedTwo to false
     */
    public Pawn(String color, int f, int r) {
        super(color, f, r);
        this.justMovedTwo = false;
    }

    /**
     * Converts the Pawn to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wp";
        else
            return "bp";
    }

    /**
     * Retrieves the function that determines which moves are valid for a particular piece
     *
     * @return a BiFunction interface that gets the valid moves for a piece
     */
    public BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> getFunc() {
        return p;
    }

    /**
     * Gets justMovedTwo
     *
     * @return true if this pawn just moved two spaces in the previous turn, false otherwise
     */
    public boolean getJustMovedTwo() {
        return justMovedTwo;
    }

    /**
     * Sets justMovedTwo to the given truth value
     *
     * @param value the given truth value
     */
    public void setJustMovedTwo(boolean value) {
        justMovedTwo = value;
    }
}
