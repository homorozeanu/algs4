/******************************************************************************
 * This class uses the Monte Carlo simulation in order to estimate the
 * percolation threshold.
 * 
 * @author George Homorozeanu
 * created on 2017/27/01
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

/**
 * Use this class to estimate the percolation threshold using 
 * Monte Carlo simulation.
 */
public class PercolationStats {
    // Store the size of the n-by-n grid.
    private final int size;
    // Store the number of simulation trials.
    private final int trials;
    // The percolation object encapsulating the percolation algorithm.
    private Percolation p;
    // Store the result of each trial.
    private final double[] trialData;
    
    /**
     * Contructs and starts a Monte Carlo simulation for estimating the
     * percolation threshold.
     * 
     * @param n A value representing the one side of the n-by-n grid.
     * @param trials The number of trials used by the simulation.
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException(
              "n and/or trials must be greather than 0");

        this.size = n * n;
        this.trials = trials;
        this.trialData = new double[trials];
        
        int row, col;        
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            
            // Repeat until system percolates.
            do {
                // Generate a closed site.
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);            
                } while (p.isOpen(row, col));
                // Open the site.
                p.open(row, col);
            } while (!p.percolates());
            
            // Save the fraction of sites that are open after the system
            // percolates.
            trialData[i] = 
                ((double) p.numberOfOpenSites()) / ((double) this.size);
        }
    }
    
    /**
     * Sample mean of percolation threshold
     * 
     * @return The sample mean of the percolation threshold.
     */
    public double mean() {
        return StdStats.mean(trialData);
    }
    
    /**
     * Sample standard deviation of percolation threshold
     * 
     * @return The sample standard deviation of the percolation threshold.
     */
    public double stddev() {
        if (trials == 1) return Double.NaN;
        return StdStats.stddev(trialData);
    }
    
    /**
     * Low endpoint of 95% confidence interval.
     * 
     * @return The low endpoint of 95% confidence interval.
     */
    public double confidenceLo() {        
        return mean() - (1.96 * stddev() / Math.sqrt(this.trials));
    }
    
    /**
     * High endpoint of 95% confidence interval.
     * 
     * @return The high endpoint of 95% confidence interval.
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(this.trials));
    }

    /**
     * Test client that starts the Monte Carlo simulation by reading the 
     * gird size (first argument) and the number of trials (second argument).
     * Prints the results at the end of the simulation.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(
                "usage: java-algs4 Percolation " +
                "<n = grid size> <T = number of computational experiments>");
            return;
        }
        
        PercolationStats ps = new PercolationStats(
            Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        
        StdOut.printf("%1$-24s= %2$f\n", "mean", ps.mean());
        StdOut.printf("%1$-24s= %2$19.18f\n", "stddev", ps.stddev());
        StdOut.printf("%1$-24s= [%2$-17.16f, %3$-17.16f]\nn"
                          , "95% confidence interval"
                          , ps.confidenceLo(), ps.confidenceHi());
    }
}