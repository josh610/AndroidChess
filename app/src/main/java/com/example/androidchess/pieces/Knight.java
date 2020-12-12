package com.example.androidchess.pieces;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class Knight extends PlayerPiece {
    private static final long serialVersionUID = 5097992202335570086L;
    /**
     * Determines the valid moves for this knight
     */
    BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> p = (BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> & Serializable) (p, board) -> {
        int[] coords = p.getCoords();
        int F = coords[0];
        int R = coords[1];
        ArrayList<int[]> moves = new ArrayList<int[]>();
        if (F + 2 < 8) {
            if (R + 1 < 8
                    && (board[F + 2][R + 1] == null || !board[F + 2][R + 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 2, R + 1 });
            }
            if (R - 1 >= 0
                    && (board[F + 2][R - 1] == null || !board[F + 2][R - 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 2, R - 1 });
            }
        }
        if (F - 2 >= 0) {
            if (R + 1 < 8
                    && (board[F - 2][R + 1] == null || !board[F - 2][R + 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 2, R + 1 });
            }
            if (R - 1 >= 0
                    && (board[F - 2][R - 1] == null || !board[F - 2][R - 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 2, R - 1 });
            }
        }
        if (R + 2 < 8) {
            if (F + 1 < 8
                    && (board[F + 1][R + 2] == null || !board[F + 1][R + 2].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 1, R + 2 });
            }
            if (F - 1 >= 0
                    && (board[F - 1][R + 2] == null || !board[F - 1][R + 2].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 1, R + 2 });
            }
        }
        if (R - 2 >= 0) {
            if (F + 1 < 8
                    && (board[F + 1][R - 2] == null || !board[F + 1][R - 2].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 1, R - 2 });
            }
            if (F - 1 >= 0
                    && (board[F - 1][R - 2] == null || !board[F - 1][R - 2].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 1, R - 2 });
            }
        }
        return moves;
    };

    /**
     * Creates a Knight with the specified color and coordinates
     *
     * @param color the color of the knight
     * @param f     the initial file of the knight
     * @param r     the initial rank of the knight
     */
    public Knight(String color, int f, int r) {
        super(color, f, r);
    }

    /**
     * Converts the Knight to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wN";
        else
            return "bN";
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
