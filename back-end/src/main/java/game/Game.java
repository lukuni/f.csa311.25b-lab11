package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // For immutable history in constructor

enum Player {
    PLAYER0(0), PLAYER1(1);

    final int value;

    Player(int value) {
        this.value = value;
    }
}

public class Game {
    private final Board board;
    private final Player player; // Current player to move
    private final List<Game> history; // History of previous game states

    public Game() {
        this(new Board(), Player.PLAYER0, Collections.emptyList()); // Start with empty history
    }

    // Constructor for creating a new game state from previous
    public Game(Board board, Player nextPlayer, List<Game> history) {
        this.board = board;
        this.player = nextPlayer;
        // Create an immutable copy of the history list
        this.history = Collections.unmodifiableList(new ArrayList<>(history));
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getPlayer() {
        return this.player;
    }

    // Getter for current player as String for GameState
    public String getCurrentPlayerString() {
         if (this.player == Player.PLAYER0) return "X";
         if (this.player == Player.PLAYER1) return "O";
         return null; // Should not happen
    }

    // Play method remains largely the same, creates a new Game object
    public Game play(int x, int y) {
        // Check if the move is valid (within bounds and cell is empty)
        if (x < 0 || x > 2 || y < 0 || y > 2 || this.board.getCell(x, y) != null) {
            return this; // Invalid move, return current game state
        }
        // Check if the game is already over
        if (this.isGameOver()) {
            return this; // Game is over, no more moves
        }

        // Create a new history list including the current game state
        List<Game> newHistory = new ArrayList<>(this.history);
        newHistory.add(this); // Add the current state before making the move

        // Determine the next player
        Player nextPlayer = this.player == Player.PLAYER0 ? Player.PLAYER1 : Player.PLAYER0;

        // Return a new Game object with the updated board, next player, and new history
        return new Game(this.board.updateCell(x, y, this.player), nextPlayer, newHistory);
    }

    // Method to check for a winner
    public Player getWinner() {
        // Check rows
        for (int row = 0; row < 3; row++)
            if (board.getCell(row, 0) != null && board.getCell(row, 0) == board.getCell(row, 1)
                    && board.getCell(row, 1) == board.getCell(row, 2))
                return board.getCell(row, 0);
        // Check columns
        for (int col = 0; col < 3; col++)
            if (board.getCell(0, col) != null && board.getCell(0, col) == board.getCell(1, col)
                    && board.getCell(0, col) == board.getCell(2, col))
                return board.getCell(0, col);
        // Check diagonals
        if (board.getCell(1, 1) != null && board.getCell(0, 0) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 2))
            return board.getCell(1, 1);
        if (board.getCell(1, 1) != null && board.getCell(0, 2) == board.getCell(1, 1)
                && board.getCell(1, 1) == board.getCell(2, 0))
            return board.getCell(1, 1);

        return null; // No winner
    }

     // Getter for winner as String for GameState
     public String getWinnerString() {
        Player winner = getWinner();
        if (winner == Player.PLAYER0) return "X";
        if (winner == Player.PLAYER1) return "O";
        return null; // No winner
    }


    // Method to check if the game is a draw
    public boolean isDraw() {
        // Game is a draw if there is no winner AND the board is full
        return getWinner() == null && board.isFull(); // Assuming Board has an isFull() method
    }

    // Method to check if the game is over (win or draw)
    public boolean isGameOver() {
        return getWinner() != null || isDraw();
    }


    // --- New Undo Method ---
    public Game undo() {
        // Check if history is not empty
        if (!this.history.isEmpty()) {
            // Get the last game state from history
            Game previousGame = this.history.get(this.history.size() - 1);

            // The history of the previous game is already stored correctly
            // within that Game object (due to immutable approach).
            // We just need to return that previous Game object.
            return previousGame;

        } else {
            // If history is empty, cannot undo. Return the current game state.
            return this;
        }
    }
    // --- End of New Undo Method ---
}