/**
 * Created by homogeo on 10.02.2017.
 */
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a 8-puzzle an used as basis to solve the puzzle.
 */
public class Board {
    private final int[][] blocks;
    private final int n;
    //---- Cache hamming and manhattan difference calculation for this board
    private int hamming = -1;
    private int manhattan = -1;

    /**
     * Create a board with n-by-n blocks derived from the given argument {@code blocks}
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks a n-by-n board with numbers. The number 0 represents the blank square.
     */
    public Board(int[][] blocks) {
        //---- Guards
        if (blocks == null) throw new NullPointerException("The n-by-n board cannot be null!");
        if (blocks.length != blocks[0].length) throw new IllegalArgumentException("Not a n-by-n board!");

        n = blocks.length;

        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }

    /**
     * Board dimension n.
     * @return The dimension of the board.
     */
    public int dimension() {
        return n;
    }

    /**
     * Calculates the number of blocks out of place by using the Hamming priotity function.
     * @return The number of blocks out of place in the current board.
     */
    public int hamming() {
        if (hamming != -1) return hamming;

        int nWrongPosition = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((blocks[i][j] != 0) &&
                        (i * n + j + 1) != blocks[i][j]) nWrongPosition++;
            }
        }
        hamming = nWrongPosition;
        return nWrongPosition;
    }

    /**
     * Calculates the Manhanttan distance between the blocks in the current board and the goal board.
     * @return The Manhattan distance.
     */
    public int manhattan() {
        if (manhattan != -1) return manhattan;

        int nWrongPosition = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    nWrongPosition += calcManhattanDiff(i, j, blocks[i][j]);
                }
            }
        }
        manhattan = nWrongPosition;
        return nWrongPosition;
    }

    /**
     * Calculate the Manhattan difference between two blocks for the current board and the goal board.
     * @param i Index of the row from the current board.
     * @param j Index of the column from the current board.
     * @param x The number stored in the block[i][j].
     * @return The calculated Manhattan difference between the given block and the block with the same number in the
     * goal board.
     */
    private int calcManhattanDiff(int i, int j, int x) {
        int iGoal = (x % n == 0) ? (x / n - 1) : (x / n);
        int jGoal = (x % n == 0) ? (n - 1) : (x % n) - 1;
        return Math.abs((i - iGoal)) + Math.abs((j - jGoal));
    }

    /**
     * Is this biard the goal board?
     * @return {@code true} if the board is the goal board, {@code false} otherwise.
     */
    public boolean isGoal() {
        return (hamming != -1) ? (hamming == 0) : (hamming() == 0);
    }

    /**
     * A board that is obtained by exchanging any pair of blocks.
     * @return A twin board that is obtained by exchanging any pair of blocks.
     */
    public Board twin() {
//        int[][] twin = new int[n][n];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                twin[i][j] = blocks[i][j];
//            }
//        }

        int row1 = StdRandom.uniform(n);
        int col1 = StdRandom.uniform(n);
        int row2 = StdRandom.uniform(n);
        int col2 = StdRandom.uniform(n);
        while ((row1 + col1 == row2 + col2) ||
                (blocks[row1][col1] == 0) ||
                (blocks[row2][col2] == 0)) {
            row1 = StdRandom.uniform(n);
            col1 = StdRandom.uniform(n);
            row2 = StdRandom.uniform(n);
            col2 = StdRandom.uniform(n);
        }
//        exch(twin, row1, col1, row2, col2);
//        Board twinBoard = new Board(twin);

        exch(blocks, row1, col1 ,row2, col2);
        Board twinBoard = new Board(blocks);
        exch(blocks, row2, col2, row1, col1);
        return twinBoard;
    }

    @Override
    public boolean equals(Object y) {
        // does this board equal y?
        if (y == this) return true;
        if (!(y instanceof Board)) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        boolean same = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                same = same && (this.blocks[i][j] == that.blocks[i][j]);
            }
        }
        return same;
    }

    /**
     * Return all neighbor boards from the current board.
     * @return An {@code Iterable<Board>} with all possible neighbors starting from the current board.
     */
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        boolean found = false;

        //---- Find index i and j for the empty (0) block
        int i = 0, j = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (blocks[i][j] == 0){
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        //---- Indexes for the empty block neighbors
        int up = i - 1;
        int down = i + 1;
        int left = j - 1;
        int right = j + 1;

        //----- Create neighbor boards
        if (up >= 0) {
            exch(blocks, i, j, up, j);
            neighbors.add(new Board(blocks));
            exch(blocks, up, j, i, j);
        }
        if (down < n) {
            exch(blocks, i, j, down, j);
            neighbors.add(new Board(blocks));
            exch(blocks, down, j, i, j);
        }
        if (left >= 0) {
            exch(blocks, i, j, i, left);
            neighbors.add(new Board(blocks));
            exch(blocks, i, left, i, j);
        }
        if (right < n) {
            exch(blocks, i, j, i, right);
            neighbors.add(new Board(blocks));
            exch(blocks, i, right, i, j);
        }

        return neighbors;
    }

    /**
     * Exchange two blocks in the given array.
     * @param arr The array with blocks.
     * @param i Index of the source row.
     * @param j Index of the source column.
     * @param k Index of the destination row.
     * @param l Index of the source column.
     */
    private void exch(int[][] arr, int i, int j, int k, int l) {
        int t = arr[i][j];
        arr[i][j] = arr[k][l];
        arr[k][l] = t;
    }

    /**
     * String representation of this board.
     * @return A String representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%1$2d", blocks[i][j]));
                if (j < n - 1) sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Main method used to unit test the methods in this class.
     * For intrnal usage only.
     * @param args
     */
    public static void main(String[] args) {
        int[][] initial = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}
        };
        Board board = new Board(initial);

        System.out.println(board.toString());

        int[][] four = {
                {0, 1, 2, 3},
                {3, 4, 5, 5},
                {6, 7, 8, 8},
                {9, 1, 2, 2}
        };
        Board fourBoard = new Board(four);

        System.out.println("Equals for 2 boards: " + board.equals(fourBoard));
        System.out.println("board dimension: " + board.dimension());
        System.out.println("fourBoard dimension: " + fourBoard.dimension());

        int[][] hamming = {
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
        };
        Board hammingBoard = new Board(hamming);
        System.out.println("hamming: " + hammingBoard.hamming());
        System.out.println("manhattan: " + hammingBoard.manhattan());
        System.out.println("Is goal board: " + hammingBoard.isGoal());

        int[][] goal = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board goalBoard = new Board(goal);
        System.out.println("Should be goal board: " + goalBoard.isGoal());
        System.out.println("hamming: " + goalBoard.hamming());
        System.out.println("manhattan: " + goalBoard.manhattan());

        System.out.println("A twin board of: ");
        System.out.println(hammingBoard.toString());
        System.out.println("can be: ");
        System.out.println(hammingBoard.twin().toString());
        System.out.println("or: ");
        System.out.println(hammingBoard.twin().toString());

        int[][] neighborsArr = {
                {8, 1, 3},
                {4, 2, 0},
                {7, 6, 5}
        };
        Board neighborsBoard = new Board(neighborsArr);
        for (Board b : neighborsBoard.neighbors()) {
            System.out.println("Neighbor board:");
            System.out.println(b.toString());
        }
    }

}
