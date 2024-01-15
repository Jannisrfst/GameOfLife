import java.io.*;
import java.util.*;

class Board {
    private boolean[][] board;

    /**
     * Represents a game board for Conway's Game of Life.
     * The board is initialized from a file containing the initial state of the game.
     * Each cell in the board can be either alive ('O') or dead ('.').
     */
    public Board(String filename) throws InvalidBoard {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();

            int rows = lines.size();
            int cols = lines.get(0).length();
            board = new boolean[rows][cols];  // boolean array creation same dimension as board

            for (int i = 0; i < rows; i++) {
                if (lines.get(i).length() != cols) throw new InvalidBoard(); // loop to check every character and throw invalid board exception if not equal to . or O
                for (int j = 0; j < cols; j++) {
                    char c = lines.get(i).charAt(j);
                    if (c == 'O') board[i][j] = true;
                    else if (c == '.') board[i][j] = false;
                    else throw new InvalidBoard();
                }
            }
        } catch (IOException e) {
            throw new InvalidBoard();
        }
    }

    public void nextGeneration() {
        boolean[][] newBoard = new boolean[board.length][board[0].length]; //This new board will store the next state of the game
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) { //it calculates the number of live neighbors and then decides whether the cell should be alive or dead in the next generation
                int liveNeighbors = 0; /// This line initializes a counter for the number of live neighbors
                for (int di = -1; di <= 1; di++) { // iterate over the 3x3 grid centered on the current cel
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) continue;
                        int ni = i + di; // These lines calculate the coordinates of the neighbor cell
                        int nj = j + dj;
                        if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length && board[ni][nj]) {
                            liveNeighbors++; // checks if out of bounds and if the neighbor is alive
                        }
                    }
                }
                if (board[i][j] && (liveNeighbors == 2 || liveNeighbors == 3)) {
                    newBoard[i][j] = true;
                } else if (!board[i][j] && liveNeighbors == 3) {
                    newBoard[i][j] = true;
                }
            }
        }
        board = newBoard; //replaces old board with new board
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (boolean[] row : board) {
            for (boolean cell : row) {
                builder.append(cell ? 'O' : '.');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) return false;
        Board other = (Board) obj;
        return Arrays.deepEquals(board, other.board);
    }
}

class InvalidBoard extends RuntimeException {
    public InvalidBoard() {
        super("Invalid board file");
    }
}

/**
 * The GameOfLife class represents the main entry point for running the Game of Life simulation.
 * It takes a file path and the number of generations as command line arguments, initializes a board,
 * and runs the simulation for the specified number of generations.
 */
public class GameOfLife {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java GameOfLife <file> <generations>");
            return;
        }
        try {
            Board board = new Board(args[0]);
            int generations = Integer.parseInt(args[1]);
            for (int i = 0; i < generations; i++) {
                System.out.println(board);
                board.nextGeneration();
            }
        } catch (InvalidBoard e) {
            System.out.println("Invalid board file");
        }
    }
}
