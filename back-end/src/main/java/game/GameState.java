package game;

import java.util.Arrays;
// Import a JSON library if you want to use one, e.g., com.google.gson.Gson
// import com.google.gson.Gson;

public class GameState {

    private final Cell[] cells;
    private final String currentPlayer;
    private final String winner;
    private final boolean isDraw;

    // private static final Gson gson = new Gson(); // If using Gson

    // Constructor includes all relevant state information
    private GameState(Cell[] cells, String currentPlayer, String winner, boolean isDraw) {
        this.cells = cells;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.isDraw = isDraw;
    }

    // Static factory method to create GameState from a Game object
    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game); // Get cell states from the board

        // --- Get current player, winner, and draw status from the Game object ---
        // Use the new methods added to the Game class
        String currentPlayerString = game.getCurrentPlayerString();
        String winnerString = game.getWinnerString();
        boolean isDrawStatus = game.isDraw(); // Call the new isDraw() method

        // Pass the collected data to the constructor
        return new GameState(cells, currentPlayerString, winnerString, isDrawStatus);
    }

    // Helper method to get Cell array from the Board
    private static Cell[] getCells(Game game) {
        Cell cells[] = new Cell[9];
        Board board = game.getBoard();
        boolean isGameOver = game.isGameOver(); // Check game over status

        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                String text = "";
                boolean playable = false;
                Player player = board.getCell(x, y);
                if (player == Player.PLAYER0)
                    text = "X";
                else if (player == Player.PLAYER1)
                    text = "O";
                else if (player == null) {
                    // Only playable if the cell is empty AND the game is not over
                    if (!isGameOver) {
                       playable = true;
                    }
                }
                cells[3 * y + x] = new Cell(x, y, text, playable);
            }
        }
        return cells;
    }

    // --- Getter methods for JSON serialization (used by libraries or manual construction) ---
    public Cell[] getCells() {
        return this.cells;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return isDraw;
    }
    // --- End of Getter methods ---


    /**
     * toString() of GameState will return the string representing
     * the GameState in JSON format.
     * This method is used by App.java to send state to frontend.
     * Updated to include currentPlayer, winner, and isDraw
     */
    @Override
    public String toString() {
        // Manually construct the JSON string including new fields
        // Need to handle nulls for winner property in JSON (null in Java should be null in JSON, not "null")
        String winnerJson = (this.winner == null) ? "null" : "\"" + this.winner + "\"";

        // Manually construct the JSON string for the cells array
        StringBuilder cellsJson = new StringBuilder("[");
        for (int i = 0; i < cells.length; i++) {
            cellsJson.append(cells[i].toString());
            if (i < cells.length - 1) {
                cellsJson.append(",");
            }
        }
        cellsJson.append("]");


        return """
                {
                  "cells": %s,
                  "currentPlayer": "%s",
                  "winner": %s,
                  "isDraw": %b
                }
                """.formatted(cellsJson.toString(), this.currentPlayer, winnerJson, this.isDraw);

        // If using Gson library:
        // return gson.toJson(this);
    }
}

// Cell class, included in the same file as GameState.java
class Cell {
    private final int x;
    private final int y;
    private final String text; // X, O, or ""
    private final boolean playable;

    Cell(int x, int y, String text, boolean playable) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.playable = playable;
    }

    // --- Getter methods for JSON serialization ---
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return this.text;
    }

    public boolean isPlayable() {
        return this.playable;
    }
    // --- End of Getter methods ---

    @Override
    public String toString() {
        // Manually constructing JSON string for a Cell
        return """
                {
                    "x": %d,
                    "y": %d,
                    "content": "%s",
                    "playable": %b
                }
                """.formatted(this.x, this.y, this.text, this.playable);

        // If using Gson library:
        // return gson.toJson(this);
    }
}