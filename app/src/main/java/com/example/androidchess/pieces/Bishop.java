package com.example.androidchess.pieces;

import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * A representation of a bishop
 *
 * @author Fionna Zhang
 * @author Joshua Zimmerman
 */
public class Bishop extends PlayerPiece {
    /**
     * Determines the valid moves for this bishop
     */
    BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> p = (p, board) -> {
        int[] coords = p.getCoords();
        int F = coords[0];
        int R = coords[1];
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int f = F + 1, r = R + 1; f < 8 && r < 8; f++, r++) {
            if (board[f][r] != null && !board[f][r].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { f, r });
                break;
            }
            if (board[f][r] != null && board[f][r].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { f, r });
        }
        for (int f = F + 1, r = R - 1; f < 8 && r >= 0; f++, r--) {
            if (board[f][r] != null && !board[f][r].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { f, r });
                break;
            }
            if (board[f][r] != null && board[f][r].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { f, r });
        }
        for (int f = F - 1, r = R - 1; f >= 0 && r >= 0; f--, r--) {
            if (board[f][r] != null && !board[f][r].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { f, r });
                break;
            }
            if (board[f][r] != null && board[f][r].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { f, r });
        }
        for (int f = F - 1, r = R + 1; f >= 0 && r < 8; f--, r++) {
            if (board[f][r] != null && !board[f][r].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { f, r });
                break;
            }
            if (board[f][r] != null && board[f][r].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { f, r });
        }
        return moves;
    };

    /**
     * Creates a Bishop with the specified color and coordinates
     *
     * @param color the color of the bishop
     * @param f     the initial file of the bishop
     * @param r     the initial rank of the bishop
     */
    public Bishop(String color, int f, int r) {
        super(color, f, r);
    }

    /**
     * Converts the Bishop to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wB";
        else
            return "bB";
    }

    /**
     * Retrieves the function that determines which moves are valid for a particular
     * piece
     *
     * @return a BiFunction interface that gets the valid moves for a piece
     */
    public BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> getFunc() {
        return p;
    }
}
