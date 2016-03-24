package com.emb.tictactoe.control;

import com.emb.tictactoe.util.LOG;

/**
 *
 * @author bu_000
 */
public class TicTacToe {

    private char[][] screen;
    private char playerMark;
    private String player = "";
    private static char computerPlayer = 'X';
    private static char humanPlayer = 'O';

    public TicTacToe() {
        screen = new char[3][3];
        resetScreen();
        playerMark = ' ';
    }

    /**
     * Clears all screen state
     */
    public void resetScreen() {
        // iterate through rows
        for (int y = 0; y < 3; y++) {
            // iterate through columns and remove marks
            for (int z = 0; z < 3; z++) {
                screen[y][z] = ' ';
            }
        }
    }

    /**
     * Display Screen
     */
    public void displayScreen() {
        LOG.info("**********************************************************");
        for (int y = 0; y < 3; y++) {
            LOG.info("| ");
            for (int z = 0; z < 3; z++) {
                LOG.info(new StringBuilder()
                        .append(screen[y][z])
                        .append(" | ").toString());
            }
        }
        LOG.info("**********************************************************");
    }

    /**
     *
     * @return state of cell availability
     */
    public boolean cellIsNotAvailable() {
        boolean result = true;
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                if (screen[y][z] == ' ') {
                    result = false;
                }
            }
        }
        return result;
    }

    public String getPlayer() {
        return player;
    }

    /**
     * Choose a player
     */
    public void setPlayer(String firstPlayer) {
        this.player = firstPlayer;
    }

    /**
     * Replaces current player with the next player
     * @return next player
     */
    public String switchPlayer() {
        if (playerMark == computerPlayer) {
            playerMark = humanPlayer;
            setPlayer("O");
            return player;
        }

        if (playerMark == humanPlayer) {
            playerMark = computerPlayer;
            setPlayer("X");
            return player;
        }
        setPlayer("");
        playerMark = ' ';
        return player;
    }

    /**
     *
     * @param player the first player choice
     */
    public void setFirstPlayer(String player) {
        if (player != null && player.equalsIgnoreCase("X")) {
            playerMark = computerPlayer;
        } else {
            playerMark = humanPlayer;
        }
        setPlayer(player);
    }

    /**
     * Append player mark to screen
     *
     * @param row
     * @param column
     * @return true or false
     */
    public boolean appendMark(int row, int column) {
        // Ensure we have valid row and column sizes
        if ((row >= 0) && (row < 3)) {
            if ((column >= 0) && (column < 3)) {
                if (screen[row][column] == ' ') {
                    screen[row][column] = playerMark;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean marksAreEqual(char c1, char c2, char c3) {
        return ((c1 != ' ') && (c1 == c2) && (c2 == c3));
    }

    /**
     *
     * @return a win state
     */
    public boolean isGameOver() {
        return (rowMarksAreEqual() || columnMarksAreEqual() || diagonalMarksAreEqual());
    }

    public boolean rowMarksAreEqual() {
        for (int y = 0; y < 3; y++) {
            if (marksAreEqual(screen[y][0], screen[y][1], screen[y][2]) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean columnMarksAreEqual() {
        for (int i = 0; i < 3; i++) {
            if (marksAreEqual(screen[0][i], screen[1][i], screen[2][i]) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean diagonalMarksAreEqual() {
        return ((marksAreEqual(screen[0][0], screen[1][1], screen[2][2]) == true)
                || (marksAreEqual(screen[0][2], screen[1][1], screen[2][0]) == true));
    }

}
