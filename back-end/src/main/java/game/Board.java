package game;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
    private final Player[] cells;

    public Board() {
        this(IntStream.range(0, 9).mapToObj(i -> null)
                .collect(Collectors.toList()).toArray(new Player[0]));
    }

    public Board(Player[] cells) {
        this.cells = cells;
    }

    public Player getCell(int x, int y) {
        // Check bounds to prevent errors
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            return null; // Or throw an exception, depending on desired behavior
        }
        return this.cells[y * 3 + x];
    }

    public Board updateCell(int x, int y, Player player) {
         // Add bounds check here as well
        if (x < 0 || x > 2 || y < 0 || y > 2) {
             return this; // Return current board if coordinates are invalid
        }
        Player[] newCells = Arrays.copyOf(this.cells, this.cells.length);
        newCells[y * 3 + x] = player;
        return new Board(newCells);
    }

    // Add a method to check if the board is full
    public boolean isFull() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == null) {
                return false; // Found an empty cell
            }
        }
        return true; // All cells are filled
    }
}