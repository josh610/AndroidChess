package com.example.androidchess.pieces;


import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * A representation of a rook
 *
 * @author Fionna Zhang
 * @author Joshua Zimmerman
 */
public class Rook extends PlayerPiece {
    /**
     * Determines the valid moves for this rook
     */
    BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> p = (p, board) -> {
        int[] coords = p.getCoords();
        int F = coords[0];
        int R = coords[1];
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = F + 1; i < 8; i++) {
            if (board[i][R] != null && !board[i][R].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { i, R });
                break;
            }
            if (board[i][R] != null && board[i][R].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { i, R });
        }
        for (int i = F - 1; i >= 0; i--) {
            if (board[i][R] != null && !board[i][R].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { i, R });
                break;
            }
            if (board[i][R] != null && board[i][R].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { i, R });
        }
        for (int i = R + 1; i < 8; i++) {
            if (board[F][i] != null && !board[F][i].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { F, i });
                break;
            }
            if (board[F][i] != null && board[F][i].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { F, i });
        }
        for (int i = R - 1; i >= 0; i--) {
            if (board[F][i] != null && !board[F][i].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { F, i });
                break;
            }
            if (board[F][i] != null && board[F][i].getColor().contentEquals(p.getColor()))
                break;
            moves.add(new int[] { F, i });
        }
        return moves;
    };

    /**
     * Creates a Rook with the specified color and coordinates
     *
     * @param color the color of the rook
     * @param f     the initial file of the rook
     * @param r     the initial rank of the rook
     */
    public Rook(String color, int f, int r) {
        super(color, f, r);
    }

    /**
     * Converts the Rook to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wR";
        else
            return "bR";
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
