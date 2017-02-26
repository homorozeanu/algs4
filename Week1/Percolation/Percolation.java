/******************************************************************************
 * This class can be used to build and test if a percolation system percolates. 
 * 
 * @author George Homorozeanu
 * created on 2017/27/01
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.In;

/**
 * Use this class to build and work with a percolation system.
 */
public class Percolation {
    // The number of virtual nodes.
    private static final int VIRTUAL_NODES_NUMBER = 2;
    // The size of one side in the n-by-n grid.
    private final int n;
    // The size of the grid (n*n).
    private final int size;
    // The index of the top and bottom virtual nodes in the 
    // union-find representation.
    private final int top, bottom;
    // The union-find algorithm used.
    private final WeightedQuickUnionUF qu;
    // Internal structure to mark all open sites.
    private final boolean[] openSites;
    // Count all open sites.
    private int openSitesCounter;
    
    /**
     * Creates a n-by-n grid (where n > 0), with all sites blocked, 
     * used to modelate a percolation system.
     * 
     * @param n The size of the n-by-n grid.
     * @throws IllegalArgumentException if n <= 0.
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException(
          "Value of n should be greather than 0");
    
        this.n = n;
        size = n * n;
        
        qu = new WeightedQuickUnionUF(size + VIRTUAL_NODES_NUMBER);
        openSites = new boolean[size + VIRTUAL_NODES_NUMBER];
        openSitesCounter = 0;
        
        // set and mark the virtual nodes as open
        top = size;  // the index of the top virtual node
        bottom = size + 1; // the index of the bottom virtual node
        
        // mark the top and bottom virtual nodes as open
        openSites[top] = true;
        openSites[bottom] = true;
        
        // connect the top and bottom virtual nodes with the grid
        for (int i = 1; i <= n; i++) {
            qu.union(top, xyTo1D(1, i));
            qu.union(bottom, xyTo1D(n, i));
        } 
    }
    
    /**
     * Open site (row, col) if it is not open already.
     * 
     * @param row The row of the n-by-n grid.
     * @param col The column of the n-by-n grid.
     * @throws IndexOutOfBoundsException if row and col are not valid 
     * grid indexes.
     */
    public void open(int row, int col) {
        validate(row);
        validate(col);
        
        int i = xyTo1D(row, col);
        openSites[i] = true;
        openSitesCounter++;
        
        int up = xyTo1D(row-1, col);
        int down = xyTo1D(row+1, col);
        int left = xyTo1D(row, col-1);
        int right = xyTo1D(row, col+1);        
        
        if (row > 1 && isOpen(row-1, col)) {
            if (!qu.connected(i, up)) qu.union(i, up);
        }
        
        if (row < n && isOpen(row+1, col)) {
            if (!qu.connected(i, down)) qu.union(i, down);
        }
        
        if (col > 1 && isOpen(row, col-1)) {
            if (!qu.connected(i, left)) qu.union(i, left);
        }
        
        if (col < n && isOpen(row, col+1)) {
            if (!qu.connected(i, right)) qu.union(i, right);
        }     
    }
    
    /**
     * Is site (row, col) open?
     * 
     * @param row The row of the n-by-n grid.
     * @param col The column of the n-by-n grid.
     * @return True if the site at (row, col) is already open, False otherwise.
     * @throws IndexOutOfBoundsException if row and col are not valid 
     * grid indexes.
     */
    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return openSites[xyTo1D(row, col)];
    }
    
    /**
     * Is site (row, col) full?
     * A site is full if it is open and can be connected to an open site in the 
     * top row via a chain of neighboring (left, right, up, down) open sites.
     * 
     * @param row The row of the n-by-n grid.
     * @param col The column of the n-by-n grid.
     * @return True if the site at (row, col) is full, False otherwise.
     * 
     * @throws IndexOutOfBoundsException if row and col are not valid 
     * grid indexes.
     */
    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);

        boolean isfull = false;
        int i = xyTo1D(row, col);
        
        if (isOpen(row, col) && qu.connected(i, top)) isfull = true;
        
        return isfull;
    }
    
    /**
     * Number of open sites.
     * 
     * @return The number of open sites in the grid.
     */
    public int numberOfOpenSites() {
        return openSitesCounter;
    }
    
    /**
     * Does the system percolate?
     * A system percolates if there is a full site in the bottom row.
     * 
     * @return True if the system percolates, False otherwise.
     */
    public boolean percolates() {
        return qu.connected(top, bottom);
    }
   
    /**
     * Transforms a 2D coordinate to 1D.
     * 
     * @param row The row number in a n-by-n grid.
     * @param col The column number in a n-by-n grid.
     * @return The 1D coordinate used by the union-find algorithm.
     */
    private int xyTo1D(int row, int col) {
        return (row - 1) * this.n + (col - 1);
    }
    
    /**
     * Checks if the given index is in the bound of the n-by-n grid.
     * 
     * @param i The index to be checked.
     * @throws IndexOutOfBoundsException if the index is not between the 
     * allowed grid bounds.
     */
    private void validate(int i) {
        if (i < 1 || i > this.n) throw new IndexOutOfBoundsException(
          "Index " + i + " is out of bounds ([" + 1 + ", " + this.n + "]).");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system
        
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            System.out.print("(" + i + ", " + j + "): ");
            System.out.println(perc.isFull(i, j));
        }

    }
} 