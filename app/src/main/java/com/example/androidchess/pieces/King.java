package com.example.androidchess.pieces;


import java.util.ArrayList;
import java.util.function.BiFunction;

public class King extends PlayerPiece {
    /**
     * Represents the current status of this king
     */
    String checkStatus = "";
    /**
     * Determines the valid moves for this king
     */
    BiFunction<PlayerPiece, PlayerPiece[][], ArrayList<int[]>> p = (p, board) -> {
        int[] coords = p.getCoords();
        int F = coords[0];
        int R = coords[1];
        ArrayList<int[]> moves = new ArrayList<int[]>();
        if (R + 1 < 8) {
            if (board[F][R + 1] == null || !board[F][R + 1].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { F, R + 1 });
            }
            if (F + 1 < 8
                    && (board[F + 1][R + 1] == null || !board[F + 1][R + 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 1, R + 1 });
            }
            if (F - 1 >= 0
                    && (board[F - 1][R + 1] == null || !board[F - 1][R + 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 1, R + 1 });
            }
        }
        if (R - 1 >= 0) {
            if (board[F][R - 1] == null || !board[F][R - 1].getColor().contentEquals(p.getColor())) {
                moves.add(new int[] { F, R - 1 });
            }
            if (F + 1 < 8
                    && (board[F + 1][R - 1] == null || !board[F + 1][R - 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F + 1, R - 1 });
            }
            if (F - 1 >= 0
                    && (board[F - 1][R - 1] == null || !board[F - 1][R - 1].getColor().contentEquals(p.getColor()))) {
                moves.add(new int[] { F - 1, R - 1 });
            }
        }
        if (F - 1 >= 0 && (board[F - 1][R] == null || !board[F - 1][R].getColor().equals(p.getColor()))) {
            moves.add(new int[] { F - 1, R });
        }
        if (F + 1 < 8 && (board[F + 1][R] == null || !board[F + 1][R].getColor().equals(p.getColor()))) {
            moves.add(new int[] { F + 1, R });
        }
        return moves;

    };

    /**
     * Creates a King with the specified color and coordinates
     *
     * @param color the color of the king
     * @param f     the initial file of the king
     * @param r     the initial rank of the king sets checkStatus to an empty string
     */
    public King(String color, int f, int r) {
        super(color, f, r);
        checkStatus = "";
    }

    /**
     * Converts the King to a String
     */
    public String toString() {
        if (this.getColor().contentEquals("White"))
            return "wK";
        else
            return "bK";
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

    /**
     * Get the status of this King (whether or not it's in check)
     *
     * @return "Checkmate" if the king is in checkmate, "Check" if the king is in
     *         check, an empty string otherwise
     */
    public String getCheckStatus() {
        return checkStatus;
    }

    /**
     * Sets checkStatus to a given string
     *
     * @param status the given status as a string
     */
    public void setCheckStatus(String status) {
        checkStatus = status;
    }
}