/**
 * Created by homogeo on 10.02.2017.
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Stack;

/**
 * A program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm
 */
public class Solver {

    //---- Minimum Priority queue used to solve the 8-puzzle problem for the initial board.
    private MinPQ<SearchNode> queue;
    //---- Minimum Priority queue used to solve the 8-puzzle problem for a twin of the initial board.
    private MinPQ<SearchNode> queueTwin;
    //---- Saves the solution-SearchNode.
    private SearchNode foundSolution;
    //---- Stores true if the puzzle is solvable, false otherwise.
    private boolean isSolvable = false;

    /**
     * Represents a search node in the A* search algorithm.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        public int priority() {
            return this.board.manhattan() + this.moves;
        }

        @Override
        public int compareTo(SearchNode o) {
            int thisKey = this.priority();
            int thatKey = o.priority();
            if (thisKey < thatKey) return -1;
            if (thisKey > thatKey) return 1;
            return 0;
        }
    }

    /**
     * Searches for possible solutions for solving the given initial-Board.
     * @param initial The initial board representing a 8-puzzle.
     */
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("the initial board cannot be null");

        queue = new MinPQ<>();
        queueTwin = new MinPQ<>();

        //---- Use 2 queues in lockstep to determine if the puzzle is solvable.
        SearchNode startNode = new SearchNode(initial, 0, null);
        SearchNode startNodeTwin = new SearchNode(initial.twin(), 0, null);
        queue.insert(startNode);
        queueTwin.insert(startNodeTwin);

        SearchNode newNode;
        SearchNode minNode = queue.delMin();
        SearchNode minNodeTwin = queueTwin.delMin();
        Iterable<Board> neighbors, neighborsTwin;

        while (!minNode.board.isGoal() && !minNodeTwin.board.isGoal()) {
            neighbors = minNode.board.neighbors();
            for (Board neighbor : neighbors) {
                if ((minNode.prev == null) || (minNode.prev != null && !minNode.prev.board.equals(neighbor))) {
//                    newNode = new SearchNode(neighbor, minNode.moves + 1, minNode);
//                    queue.insert(newNode);
                    queue.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
            }
            neighborsTwin = minNodeTwin.board.neighbors();
            for (Board neighbor : neighborsTwin) {
                if ((minNodeTwin.prev == null) || (minNodeTwin.prev != null) && !minNodeTwin.prev.board.equals(neighbor)) {
//                    newNode = new SearchNode(neighbor, minNodeTwin.moves+1, minNodeTwin);
//                    queueTwin.insert(newNode);
                    queueTwin.insert(new SearchNode(neighbor, minNodeTwin.moves+1, minNodeTwin));
                }
            }
            minNode = queue.delMin();
            minNodeTwin = queueTwin.delMin();
        }

        if (minNodeTwin.board.isGoal()){
           isSolvable = false;
           foundSolution = null;
        }
        if (minNode.board.isGoal()){
            isSolvable = true;
            foundSolution = minNode;
        }
    }

    /**
     * Is the initial board solvable?
     * @return {@code true} if the board is solvable, {@code false} otherwise.
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable.
     * @return The number of moves to solve the puzzle.
     */
    public int moves() {
        if (!isSolvable()) return -1;
        return foundSolution.moves;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable.
     * @return A sequence of board leading to the goal solution. Null if the puzzle is not solvable.
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        LinkedStack<Board> solution = new LinkedStack<>();
        SearchNode current = foundSolution;

        do{
            solution.push(current.board);
            current = current.prev;
        } while (current != null);

        return solution;
    }

    /**
     * Solve a slider puzzle (to unit test the Solver class).
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
