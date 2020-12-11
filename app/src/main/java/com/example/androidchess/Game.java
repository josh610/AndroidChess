package com.example.androidchess;

import com.example.androidchess.pieces.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Game implements Serializable {
    private String name;
    private LocalDateTime date;
    private ArrayList<String> moves; //Records all moves in the game ("White D2->E4", "Black B1->G7 Queen", "White Resign")
    private String playerMove = "White's move";

    /** Getter/Setter Methods **/

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPlayerMove(){
        return playerMove;
    }

    public void setPlayerMove(String playerMove){
        this.playerMove = playerMove;
    }
    /**
     * The chessboard
     */
    private PlayerPiece[][] board;
    public PlayerPiece[][] getBoard() { return board; }

    private int gameStatus;
    public void setGameStatus(int status) { gameStatus = status; }
    /**
     * The white king
     */
    private PlayerPiece wKing;
    public PlayerPiece getwKing() { return wKing; }
    /**
     * The black king
     */
    private PlayerPiece bKing;
    public PlayerPiece getbKing() {return bKing; }
    /**
     * ArrayList that holds the spaces that put the white king in check
     */
    private ArrayList<int[]> wCheckSpaces = new ArrayList<int[]>(); // spaces that put white's king in check
    public ArrayList<int[]> getwCheckSpaces() { return wCheckSpaces; }
    /**
     * ArrayList that holds the spaces that put the black king in check
     */
    private ArrayList<int[]> bCheckSpaces = new ArrayList<int[]>(); // spaces that put black's king in check
    public ArrayList<int[]> getbCheckSpaces() { return bCheckSpaces; }
    /**
     * Scanner to take command line arguments
     */
    //static Scanner sc;

    private String currPlayer = "White";
    public String getCurrPlayer() { return currPlayer; }
    public void setCurrPlayer(String player) { currPlayer = player; }

    /**
     * Initializes the board (places pieces where they go for beginning of game)
     */
    public void initBoard(PlayerPiece[][] board) {
        // Black pieces
        board[0][7] = new Rook("Black", 0, 7);
        board[1][7] = new Knight("Black", 1, 7);
        board[2][7] = new Bishop("Black", 2, 7);
        board[3][7] = new Queen("Black", 3, 7);
        bKing = new King("Black", 4, 7);
        board[4][7] = bKing;
        board[5][7] = new Bishop("Black", 5, 7);
        board[6][7] = new Knight("Black", 6, 7);
        board[7][7] = new Rook("Black", 7, 7);
        for (int f = 0; f < 8; f++) {
            board[f][6] = new Pawn("Black", f, 6);
        }
        // White pieces
        for (int f = 0; f < 8; f++) {
            board[f][1] = new Pawn("White", f, 1);
        }
        board[0][0] = new Rook("White", 0, 0);
        board[1][0] = new Knight("White", 1, 0);
        board[2][0] = new Bishop("White", 2, 0);
        board[3][0] = new Queen("White", 3, 0);
        wKing = new King("White", 4, 0);
        board[4][0] = wKing;
        board[5][0] = new Bishop("White", 5, 0);
        board[6][0] = new Knight("White", 6, 0);
        board[7][0] = new Rook("White", 7, 0);
    }

    /**
     * Prints the current state of the board
     */
    public void printBoard(PlayerPiece[][] board) {
        System.out.println();
        for (int r = 7; r >= 0; r--) {
            for (int f = 0; f < 8; f++) {
                PlayerPiece p = board[f][r];
                if (p == null) {
                    if ((f + r) % 2 == 0)
                        System.out.print("## ");
                    else
                        System.out.print("   ");
                } else
                    System.out.print(p + " ");
            }
            System.out.println(r + 1);
        }
        char c = 'a';
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + c++ + " ");
        }
        System.out.println();
    }

    /**
     * Fills the respective CheckSpaces ArrayLists with the spaces that will put the
     * respective kings in check
     */
    public void scanCheckSpaces(PlayerPiece[][] board, ArrayList<int[]> wCheckSpaces, ArrayList<int[]> bCheckSpaces) {
        wCheckSpaces.clear();
        bCheckSpaces.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PlayerPiece p = board[i][j];
                if (p == null)
                    continue;
                /*
                 * if (p instanceof Pawn) { ((Pawn) p).incrMovedTwo(); }
                 */
                if (p.getColor().contentEquals("White")) {
                    bCheckSpaces.addAll(p.getMoves(p, board));
                } else if (p.getColor().contentEquals("Black")) {
                    wCheckSpaces.addAll(p.getMoves(p, board));
                }
            }
        }
    }

    /**
     * Determines what category the user input falls under (draw, resign, move, or
     * invalid)
     *
     * @param input the user input
     * @return -1 if input is invalid, 0 if input has "draw?" at the end, 1 if
     *         "resign", 2 if a valid move (includes promotion)
     */

    public static int parseInput(String input) {
        if (input.endsWith("draw?")) {
            return 0;
        } else if (input.equals("resign")) {
            return 1;
        }
        if (Character.isLetter(input.charAt(0)) && Character.isDigit(input.charAt(1)) && input.charAt(2) == ' '
                && Character.isLetter(input.charAt(3)) && Character.isDigit(input.charAt(4))) {

            int test1;
            int test2;
            test1 = input.charAt(0);
            test2 = input.charAt(3);
            boolean letter = (97 <= test1 && test1 <= 104 && 97 <= test2 && test2 <= 104);
            test1 = input.charAt(1);
            test2 = input.charAt(4);
            boolean number = (49 <= test1 && test1 <= 56 && 49 <= test2 && test2 <= 56);
            if (letter && number && input.length() == 5)
                return 2;
            else if (input.length() == 7) {
                if (input.charAt(5) != ' ')
                    return -1;
                if (letter && number) {
                    if (input.endsWith("Q") || input.endsWith("N") || input.endsWith("B") || input.endsWith("R")) {
                        if (test2 != 49 && test2 != 56)
                            return -1;
                        return 2;
                    }
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * Translates a valid move into an int array
     *
     * @param input the user input
     * @return the input as an int array of length 5 - i.e. if user input is "a1 b2"
     *         then the int[] will be {0},{0},{1},{1}, if the move is a promotion,
     *         then the last int in the array indicates which piece the current
     *         piece will be promoted to
     */
    public static int[] parseMove(String input) {
        String move = "";
        for (int i = 0; i < 5; i++) {
            if (i != 2)
                move += input.charAt(i);
        }
        move = move.toLowerCase();
        int[] result = new int[5];
        result[0] = move.charAt(0) - 97;
        result[1] = move.charAt(1) - 48 - 1;
        result[2] = move.charAt(2) - 97;
        result[3] = move.charAt(3) - 48 - 1;

		/*char promotion = '0';
		if (input.length() == 7) {
			promotion = input.charAt(6);
			if (promotion == 'Q') result[4] = 1;
			else if (promotion == 'N') result[4] = 2;
			else if (promotion == 'B') result[4] = 3;
			else if (promotion == 'R') result[4] = 4;
			else result[4] = 0;
			return result;
		}*/


        if (input.endsWith("Q")) result[4] = 1;
        else if (input.endsWith("N")) result[4] = 2;
        else if (input.endsWith("B")) result[4] = 3;
        else if (input.endsWith("R")) result[4] = 4;
        else result[4] = 0;
        return result;
    }

    /**
     * Executes the given player's move
     *
     * @param color the color of the current player
     * @param move  the string containing the player's move
     * @return true if move is successful, false if move is unsuccessful
     */

    public boolean playerMove(PlayerPiece[][] board, ArrayList<int[]> wCheckSpaces, ArrayList<int[]> bCheckSpaces, PlayerPiece wKing, PlayerPiece bKing, String color, String move) {
        int[] input = parseMove(move);
        int f1 = input[0];
        // System.out.print("f1 = " + f1 + ", ");
        int r1 = input[1];
        // System.out.print("r1 = " + r1 + ", ");
        int f2 = input[2];
        // System.out.print("f2 = " + f2 + ", ");
        int r2 = input[3];
        // System.out.println("r2 = " + r2);
        String promotion = "";
        int promotionInt = input[4];
		/*switch (promotionInt) {
		case 1:
			promotion = "Q";
		case 2:
			promotion = "N";
		case 3:
			promotion = "B";
		case 4:
			promotion = "R";
		default:
			promotion = "";
		}*/

        int[] newCoords = { f2, r2 };
        boolean bypass = false;

        PlayerPiece p = board[f1][r1];
        PlayerPiece q = board[f2][r2];

        // check if piece exists at coords
        if (p == null) {
            // System.out.print("Not a piece: ");
            return false;
        }

        // check if piece is the right color
        if (!p.getColor().contentEquals(color)) {
            // System.out.print("Wrong color: ");
            return false;
        }

        // check if move would put king in check
        PlayerPiece temp = q;
        board[f1][r1] = null;
        board[f2][r2] = p;
        p.setCoords(f2, r2);
        // int[] tempCoords = q.getCoords();
        scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
        if (p.getColor().equals("White") && isInList(wCheckSpaces, wKing.getCoords())) {
            board[f1][r1] = p;
            board[f2][r2] = temp;
            q = board[f2][r2];
            // System.out.print("Move puts king in check ");
            return false;
        } else if (p.getColor().equals("Black") && isInList(bCheckSpaces, bKing.getCoords())) {
            board[f1][r1] = p;
            board[f2][r2] = temp;
            q = board[f2][r2];
            // System.out.print("Move puts king in check ");
            return false;
        }
        board[f1][r1] = p;
        board[f2][r2] = temp;
        p.setCoords(f1, r1);
        q = board[f2][r2];

        scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
        // check if destination contains a piece of the same color
        /*
         * if (q != null && q.getColor().contentEquals(color)) { // check for castling
         * if (p instanceof King && !p.hasItMoved() && q instanceof Rook &&
         * !q.hasItMoved()) { boolean b = true; if (Arrays.equals(q.getCoords(), new
         * int[] { 0, 7 })) { for (int i = 0; i < 4; i++) { if (board[i][7] != null) b =
         * false; } if (b) { board[3][7] = q; bypass = true; } else {
         * System.out.print("Cannot castle, there are pieces in the way: "); return
         * false; } } else { for (int i = 4; i < 8; i++) { if (board[i][7] != null) b =
         * false; } if (b) { board[5][7] = q; bypass = true; } else {
         * System.out.print("Cannot castle, there are pieces in the way: "); return
         * false; } } } }
         */

        // check for castling
        if (p == wKing) {
            PlayerPiece wRook = null;
            if (f1 == 4 && r1 == 0 && f2 == 6 && r2 == 0) {
                if (board[5][0] != null || board[6][0] != null || wKing.hasItMoved() || !(board[7][0] instanceof Rook)
                        || board[7][0].hasItMoved()) {
                    // System.out.print("Cannot castle: ");
                    return false;
                }
                if (isInList(wCheckSpaces, wKing.getCoords()) || isInList(wCheckSpaces, new int[] { 5, 0 })
                        || isInList(wCheckSpaces, new int[] { 6, 0 }) || isInList(wCheckSpaces, new int[] { 7, 0 })) {
                    // System.out.print("Cannot castle, king is in check or will be in check: ");
                    return false;
                }
                wRook = board[7][0];
                board[6][0] = wKing;
                wKing.setCoords(6, 0);
                board[5][0] = wRook;
                board[7][0] = null;
                bypass = true;
            } else if (f1 == 4 && r1 == 0 && f2 == 2 && r2 == 0) {
                if (board[1][0] != null || board[2][0] != null || board[3][0] != null || wKing.hasItMoved()
                        || !(board[0][0] instanceof Rook) || board[0][0].hasItMoved()) {
                    // System.out.print("Cannot castle: ");
                    return false;
                }
                if (isInList(wCheckSpaces, wKing.getCoords()) || isInList(wCheckSpaces, new int[] { 3, 0 })
                        || isInList(wCheckSpaces, new int[] { 2, 0 }) || isInList(wCheckSpaces, new int[] { 1, 0 })
                        || isInList(wCheckSpaces, new int[] { 0, 0 })) {
                    // System.out.print("Cannot castle, king is in check or will be in check: ");
                    return false;
                }
                wRook = board[0][0];
                board[2][0] = wKing;
                wKing.setCoords(2, 0);
                board[3][0] = wRook;
                board[0][0] = null;
                bypass = true;
            }
        } else if (p == bKing) {
            PlayerPiece bRook = null;
            if (f1 == 4 && r1 == 7 && f2 == 6 && r2 == 7) {
                if (board[5][7] != null || board[6][7] != null || bKing.hasItMoved() || !(board[7][7] instanceof Rook)
                        || board[7][7].hasItMoved()) {
                    // System.out.print("Cannot castle: ");
                    return false;
                }
                if (isInList(bCheckSpaces, bKing.getCoords()) || isInList(bCheckSpaces, new int[] { 5, 7 })
                        || isInList(bCheckSpaces, new int[] { 6, 7 }) || isInList(bCheckSpaces, new int[] { 7, 7 })) {
                    // System.out.print("Cannot castle, king is in check or will be in check: ");
                    return false;
                }
                bRook = board[7][7];
                board[6][7] = bKing;
                bKing.setCoords(6, 7);
                board[5][7] = bRook;
                board[7][7] = null;
                bypass = true;
            } else if (f1 == 4 && r1 == 7 && f2 == 2 && r2 == 7) {
                if (board[1][7] != null || board[2][7] != null || board[3][7] != null || bKing.hasItMoved()
                        || !(board[0][7] instanceof Rook) || board[0][7].hasItMoved()) {
                    // System.out.print("Cannot castle: ");
                    return false;
                }
                if (isInList(bCheckSpaces, bKing.getCoords()) || isInList(wCheckSpaces, new int[] { 3, 7 })
                        || isInList(wCheckSpaces, new int[] { 2, 7 }) || isInList(wCheckSpaces, new int[] { 1, 7 })
                        || isInList(wCheckSpaces, new int[] { 0, 7 })) {
                    // System.out.print("Cannot castle, king is in check or will be in check: ");
                    return false;
                }
                bRook = board[0][7];
                board[2][7] = bKing;
                bKing.setCoords(2, 0);
                board[3][7] = bRook;
                board[0][7] = null;
                bypass = true;
            }
        }

        if (p instanceof Pawn) {
            // check for en passant
            if (p.getColor().contentEquals("White")) {
                if (f2 == f1 + 1 && r2 == r1 + 1) {
                    PlayerPiece ep = board[f1 + 1][r1];
                    if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                        board[f1 + 1][r1] = null;
                        bypass = true;
                    }
                } else if (f2 == f1 - 1 && r2 == r1 + 1) {
                    PlayerPiece ep = board[f1 - 1][r1];
                    if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                        board[f1 - 1][r1] = null;
                        bypass = true;
                    }
                }
            } else {
                if (f2 == f1 + 1 && r2 == r1 - 1) {
                    PlayerPiece ep = board[f1 + 1][r1];
                    if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                        board[f1 + 1][r1] = null;
                        bypass = true;
                    }
                } else if (f2 == f1 - 1 && r2 == r1 - 1) {
                    PlayerPiece ep = board[f1 - 1][r1];
                    if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                        board[f1 - 1][r1] = null;
                        bypass = true;
                    }
                }
            }
        }
        // check for valid move
        if (!isInList(p.getMoves(p, board), newCoords) && !bypass) {
            /*
             * for (int i = 0; i < p.getMoves(p, board).size(); i++) {
             * System.out.print(Arrays.toString(p.getMoves(p, board).get(i))); }
             */
            // System.out.println();
            // System.out.print("Inappropriate piece movement: ");
            return false;
        }
        // check for Pawn promotion
        if (p instanceof Pawn) {
            // check if it's moving two spaces
            if (!p.hasItMoved() && (r2 == r1 + 2 || r2 == r1 - 2)) {
                ((Pawn) p).setJustMovedTwo(true);
            }
            if ((p.getColor().equals("Black") && r2 == 0) || (p.getColor().equals("White") && r2 == 7)) {
                if (promotionInt == 0)
                    p = new Queen(p.getColor(), f2, r2);
                else if (promotionInt == 1)
                    p = new Queen(p.getColor(), f2, r2);
                else if (promotionInt == 2)
                    p = new Knight(p.getColor(), f2, r2);

                else if (promotionInt == 3)
                    p = new Bishop(p.getColor(), f2, r2);

                else if (promotionInt == 4)
                    p = new Rook(p.getColor(), f2, r2);

            } else if (!promotion.contentEquals("")) {
                // System.out.print("Invalid promotion: ");
                return false;
            }
        }
        // move the piece
        board[f2][r2] = p;
        board[f1][r1] = null;
        p.setCoords(f2, r2);
        p.itHasMoved();
        scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
        updateCheckStatus(board, wCheckSpaces, bCheckSpaces, wKing, bKing, p.getColor());

        // set justMovedTwo to false for all pawns that aren't the current piece
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PlayerPiece piece = board[i][j];
                if (piece != null && piece instanceof Pawn) {
                    if (piece != p) {
                        ((Pawn) piece).setJustMovedTwo(false);
                    }
                }
            }
        }
        return true;

    }
    public void checkForEnPassant(PlayerPiece[][] board, PlayerPiece p, int currFile, int currRank) {
        if (p instanceof Pawn) {
            // check for en passant
            if (p.getColor().contentEquals("White")) {
                PlayerPiece ep = board[currFile + 1][currRank];
                if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                    p.getMoves(p, board).add(new int[] {currFile + 1, currRank + 1});
                }
                ep = board[currFile - 1][currRank];
                if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                    p.getMoves(p, board).add(new int[] {currFile - 1, currRank + 1});
                }
            } else {
                PlayerPiece ep = board[currFile + 1][currRank];
                if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                    p.getMoves(p, board).add(new int[] {currFile + 1, currRank - 1});
                }
                ep = board[currFile - 1][currRank];
                if (ep != null && ep instanceof Pawn && ((Pawn) ep).getJustMovedTwo() == true) {
                    p.getMoves(p, board).add(new int[] {currFile - 1, currRank - 1});
                }
            }
        }
    }
    public void checkForCastle(PlayerPiece[][] board, ArrayList<int[]> wCheckSpaces, ArrayList<int[]> bCheckSpaces, PlayerPiece wKing, PlayerPiece bKing) {
        if (!((board[5][0] != null || board[6][0] != null || wKing.hasItMoved() || !(board[7][0] instanceof Rook)
                || board[7][0].hasItMoved()) || (isInList(wCheckSpaces, wKing.getCoords()) || isInList(wCheckSpaces, new int[] { 5, 0 })
                || isInList(wCheckSpaces, new int[] { 6, 0 }) || isInList(wCheckSpaces, new int[] { 7, 0 }))) && (wKing.getCoords()[0] == 4 && wKing.getCoords()[1] == 0)) {
            wKing.getMoves(wKing, board).add(new int[] {6, 0});
        } else if (!((board[1][0] != null || board[2][0] != null || board[3][0] != null || wKing.hasItMoved()
                || !(board[0][0] instanceof Rook) || board[0][0].hasItMoved() || isInList(wCheckSpaces, wKing.getCoords()) || isInList(wCheckSpaces, new int[] { 3, 0 })
                || isInList(wCheckSpaces, new int[] { 2, 0 }) || isInList(wCheckSpaces, new int[] { 1, 0 })
                || isInList(wCheckSpaces, new int[] { 0, 0 }))) && (wKing.getCoords()[0] == 4 && wKing.getCoords()[1] == 0)) {
            wKing.getMoves(wKing, board).add(new int[] {2, 0});
        }
        if (!(board[5][7] != null || board[6][7] != null || bKing.hasItMoved() || !(board[7][7] instanceof Rook)
                || board[7][7].hasItMoved() || isInList(bCheckSpaces, bKing.getCoords()) || isInList(bCheckSpaces, new int[] { 5, 7 })
                || isInList(bCheckSpaces, new int[] { 6, 7 }) || isInList(bCheckSpaces, new int[] { 7, 7 })) && (bKing.getCoords()[0] == 4 && bKing.getCoords()[1] == 7)) {
            bKing.getMoves(bKing, board).add(new int[] {6, 7});
        } else if (!(board[1][7] != null || board[2][7] != null || board[3][7] != null || bKing.hasItMoved()
                || !(board[0][7] instanceof Rook) || board[0][7].hasItMoved() || isInList(bCheckSpaces, bKing.getCoords()) || isInList(wCheckSpaces, new int[] { 3, 7 })
                || isInList(wCheckSpaces, new int[] { 2, 7 }) || isInList(wCheckSpaces, new int[] { 1, 7 })
                || isInList(wCheckSpaces, new int[] { 0, 7 })) && (bKing.getCoords()[0] == 4 && bKing.getCoords()[1] == 7)) {
            bKing.getMoves(bKing, board).add(new int[] {2, 7});
        }
    }

    /**
     * Updates the check status of the opposing side's king
     *
     * @param color the player that just moved
     */
    public void updateCheckStatus(PlayerPiece[][] board, ArrayList<int[]> wCheckSpaces, ArrayList<int[]> bCheckSpaces, PlayerPiece wKing, PlayerPiece bKing, String color) {
        boolean wIsCheckmate = false;
        boolean bIsCheckmate = false;
        int wKingMoves = ((King) wKing).getMoves(wKing, board).size();
        int bKingMoves = ((King) bKing).getMoves(bKing, board).size();
        int wKingUnsafe = 0;
        int bKingUnsafe = 0;

        PlayerPiece checkPiece1 = null;
        PlayerPiece checkPiece2 = null;
        PlayerPiece tempPiece = null;

        // single check if only one piece can capture the king
        boolean singleCheck = false;
        // double check if two pieces can capture the king
        boolean doubleCheck = false;

        if (color.equals("White")) {
            if (isInList(bCheckSpaces, bKing.getCoords())) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempPiece = board[i][j];
                        if (tempPiece != null && tempPiece.getColor().contentEquals("White")) {
                            if (checkPiece1 == null) {
                                // check if the coordinates of the opposing king are in the move list of the current piece
                                if (isInList(tempPiece.getMoves(tempPiece, board), bKing.getCoords())) {
                                    checkPiece1 = tempPiece;
                                }
                            } else {
                                checkPiece2 = tempPiece;
                            }
                        }
                    }
                }
            }
        } else {
            if (isInList(wCheckSpaces, wKing.getCoords())) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempPiece = board[i][j];
                        if (tempPiece != null && tempPiece.getColor().contentEquals("Black")) {
                            if (checkPiece1 == null) {
                                // check if the coordinates of the opposing king are in the move list of the current piece
                                if (isInList(tempPiece.getMoves(tempPiece, board), wKing.getCoords())) {
                                    checkPiece1 = tempPiece;
                                }
                            } else {
                                checkPiece2 = tempPiece;
                            }
                        }
                    }
                }
            }
        }

        if (checkPiece2 != null) {
            doubleCheck = true;
            singleCheck = false;
        } else {
            singleCheck = true;
            doubleCheck = false;
        }

        // if the king is in double check, then the only way to get out of check is moving the king, otherwise the king is in checkmate
        if (doubleCheck) {
            if (color.equals("White")) {
                for (int i = 0; i < bKing.getMoves(bKing, board).size(); i++) {
                    if (isInList(bCheckSpaces, bKing.getMoves(bKing, board).get(i))) {
                        bKingUnsafe++;
                    }
                }
                if (bKingUnsafe == bKingMoves && isInList(bCheckSpaces, bKing.getCoords())) {
                    // no moves to get the king out of check
                    bIsCheckmate = true;
                }
                if (bIsCheckmate) {
                    ((King) bKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                } else {
                    // there is at least one move to get the king out of check
                    ((King) bKing).setCheckStatus("Check");
                    System.out.println("Check");
                }
            } else {
                for (int i = 0; i < wKing.getMoves(wKing, board).size(); i++) {
                    if (isInList(wCheckSpaces, wKing.getMoves(wKing, board).get(i))) {
                        wKingUnsafe++;
                    }
                }
                if (wKingUnsafe == wKingMoves && isInList(wCheckSpaces, wKing.getCoords())) {
                    // no moves to get the king out of check
                    wIsCheckmate = true;
                }
                if (wIsCheckmate) {
                    ((King) wKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                } else {
                    // there is at least one move to get the king out of check
                    ((King) wKing).setCheckStatus("Check");
                    System.out.println("Check");
                }
            }
        } else if (singleCheck && checkPiece1 != null) {
            if (color.contentEquals("White")) {
                // see if the piece that has the king in check can be captured
                if (isInList(bCheckSpaces, checkPiece1.getCoords())) {
                    ((King) bKing).setCheckStatus("Check");
                    System.out.println("Check");
                    return;
                }
                for (int i = 0; i < bKing.getMoves(bKing, board).size(); i++) {
                    if (isInList(bCheckSpaces, bKing.getMoves(bKing, board).get(i))) {
                        bKingUnsafe++;
                    }
                }
                boolean bSimulation = pieceProtectSimulation(board, wCheckSpaces, bCheckSpaces, wKing, bKing, "Black");
                if (bKingUnsafe == bKingMoves && isInList(bCheckSpaces, bKing.getCoords()) && !bSimulation) {
                    ((King) bKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                    return;
                }
                if (bSimulation) {
                    ((King) bKing).setCheckStatus("Check");
                    System.out.println("Check");
                    return;
                } else {
                    ((King) bKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                    return;
                }
            } else {
                // see if the piece that has the king in check can be captured
                if (isInList(wCheckSpaces, checkPiece1.getCoords())) {
                    ((King) wKing).setCheckStatus("Check");
                    System.out.println("Check");
                    return;
                }
                for (int i = 0; i < wKing.getMoves(wKing, board).size(); i++) {
                    if (isInList(wCheckSpaces, wKing.getMoves(wKing, board).get(i))) {
                        wKingUnsafe++;
                    }
                }
                boolean wSimulation = pieceProtectSimulation(board, wCheckSpaces, bCheckSpaces, wKing, bKing, "White");
                if (wKingUnsafe == wKingMoves && isInList(wCheckSpaces, wKing.getCoords()) && !wSimulation) {
                    ((King) wKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                    return;
                }
                if (wSimulation) {
                    ((King) wKing).setCheckStatus("Check");
                    System.out.println("Check");
                } else {
                    ((King) wKing).setCheckStatus("Checkmate");
                    System.out.println("Checkmate");
                    return;
                }

            }
        }
    }

    /**
     * Simulates all possible moves of one color to see if there are any moves that
     * can be made to get the king out of check
     *
     * @param color the color of the king that is trying to avoid check
     * @return true if it is possible to protect the current color king from being
     *         captured, false if the king cannot be protected
     */
    public boolean pieceProtectSimulation(PlayerPiece[][] board, ArrayList<int[]> wCheckSpaces, ArrayList<int[]> bCheckSpaces, PlayerPiece wKing, PlayerPiece bKing, String color) {
        ArrayList<PlayerPiece> colorPieces = new ArrayList<PlayerPiece>();
        // store all pieces of the current color in the ArrayList colorPieces
        if (color.contentEquals("White")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null) {
                        if (board[i][j].getColor().contentEquals(color) && !(board[i][j] instanceof King)) {
                            colorPieces.add(board[i][j]);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null) {
                        if (board[i][j].getColor().contentEquals(color) && !(board[i][j] instanceof King)) {
                            colorPieces.add(board[i][j]);
                        }
                    }
                }
            }
        }

        PlayerPiece protectorPiece;
        PlayerPiece tempPiece;
        int finitial;
        int rinitial;
        int ftemp;
        int rtemp;
        ArrayList<int[]> protectorPieceMoves;
        // iterate through every piece of the current color
        for (int i = 0; i < colorPieces.size(); i++) {
            protectorPiece = colorPieces.get(i);
            finitial = protectorPiece.getCoords()[0];
            rinitial = protectorPiece.getCoords()[1];
            board[finitial][rinitial] = null;
            protectorPieceMoves = protectorPiece.getMoves(protectorPiece, board);
            // attempt all possible moves and see if any of them remove the current color king from the list of check spaces
            for (int j = 0; j < protectorPieceMoves.size(); j++) {
                ftemp = protectorPieceMoves.get(j)[0];
                rtemp = protectorPieceMoves.get(j)[1];
                tempPiece = board[ftemp][rtemp];
                board[ftemp][rtemp] = protectorPiece;
                protectorPiece.setCoords(ftemp, rtemp);
                scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
                if (color.contentEquals("White")) {
                    if (!isInList(wCheckSpaces, wKing.getCoords())) {
                        board[finitial][rinitial] = protectorPiece;
                        board[ftemp][rtemp] = tempPiece;
                        protectorPiece.setCoords(finitial, rinitial);
                        scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
                        return true;
                    }
                } else {
                    if (!isInList(bCheckSpaces, bKing.getCoords())) {
                        board[finitial][rinitial] = protectorPiece;
                        board[ftemp][rtemp] = tempPiece;
                        protectorPiece.setCoords(finitial, rinitial);
                        scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
                        return true;
                    }
                }
                board[finitial][rinitial] = protectorPiece;
                board[ftemp][rtemp] = tempPiece;
                protectorPiece.setCoords(finitial, rinitial);
                scanCheckSpaces(board, wCheckSpaces, bCheckSpaces);
            }
        }
        return false;
    }

    /**
     * Helper method to determine if an int[] is in a given ArrayList of int[]s
     *
     * @param arr    the given ArrayList of int[]s
     * @param target the target int[]
     * @return true if the int[] is in the ArrayList, false if not
     */
    public static boolean isInList(ArrayList<int[]> arr, int[] target) {
        for (int i = 0; i < arr.size(); i++) {
            if (Arrays.equals(arr.get(i), target))
                return true;
        }
        return false;
    }
    public static String intToMove(int prevRow, int prevCol, int nextRow, int nextCol) {
        char prevFile = (char) (prevCol + 97);
        int prevRank = prevRow + 1;
        char nextFile = (char) (nextCol + 97);
        int nextRank = nextRow + 1;
        System.out.println(prevFile + prevRank + " " + nextFile + nextRank);
        return prevFile + prevRank + " " + nextFile + nextRank;
    }


    public String moveToString(String player, String move) {
        return player + " " + move;
    }
}
