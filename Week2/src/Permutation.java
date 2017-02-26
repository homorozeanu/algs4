/******************************************************************************
 * @author George Homorozeanu
 * created on 2017/02/02
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * A client program that takes a command-line integer k;
 * reads in a sequence of strings from standard input
 * and prints exactly k of them, uniformly at random.
 *
 * Prints each item from the sequence at most once.
 * Assumes that 0 ≤ k ≤ n, where n is the number of string
 * on standard input.
 */
public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: java Permutation <k>");
            return;
        }

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readString());
        }

        for (int i = 1; i <= k; i++) {
            StdOut.println(q.dequeue());
        }
    }
}
