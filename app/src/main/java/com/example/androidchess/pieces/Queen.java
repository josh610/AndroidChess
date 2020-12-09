package com.example.androidchess.pieces;


import java.util.ArrayList;
import java.util.function.BiFunction;

public class Queen extends PlayerPiece {
    /**
     * Determines the valid moves for this queen
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
     * Creates a Queen with the specified color and coordinates
     *
     * @param color the color of the queen
     * @param f     the initial file of the queen
     * @param r     the initial rank of the queen
     */
    public Queen(String color, int f, int r) {
        super(color, f, r);
    }

    /**
     * Converts the Queen to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wQ";
        else
            return "bQ";
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
