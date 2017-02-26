import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Finds all line segments containing 4 or more collinear points.
 */
public class FastCollinearPoints {
    private final List<LineSegment> lineSegments;
    private final Point[] arr;

    /**
     * Create an instance of {@code FastCollinearPoints} and start searching for line segments containing at least
     * 4 collinear points.
     *
     * @param points The array containing all points that form line segments with at least 4 collinear points.
     */
    public FastCollinearPoints(Point[] points) {
        //---- Guards: check for null parameter and for null values in the array parameter
        if (points == null) throw new NullPointerException("argument cannot be null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new NullPointerException("argument cannot be null");
        }

        arr = points.clone();
        Arrays.sort(arr);

        lineSegments = new ArrayList<>();
        LineSegment pToS = null;
        int index;

        for (Point p : points) {
            index = maxCollinearPointIndexTo(p);
            if (index != -1) {
                pToS = new LineSegment(p, arr[index]);
                lineSegments.add(pToS);
            }
        }
    }

    /**
     * If at least 3 points are collinear with the given point-argument, then return the index in the points array
     * of the greatest of the points (excluding the point in the argument).
     *
     * @param p The starting point to which at least 3 points should be collinear with.
     * @return The index of the greatest of the found collinear points or -1 if there are no collinear points with the
     * given point-argument.
     * @throws IllegalArgumentException if duplicate points are found.
     */
    private int maxCollinearPointIndexTo(Point p) {
        Comparator<Point> comparator = p.slopeOrder();
        Arrays.sort(arr, comparator);

        int i = 0;
        boolean blnFound = false;
        for (i = 0; i < arr.length - 1; i++) {
            if (p.slopeTo(arr[i]) == p.slopeTo(arr[i + 1])) {

                if (p.slopeTo(arr[i]) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("duplicate points not allowed");

                blnFound = true;
                break;
            }
        }

        int cnt = 2;
        if (blnFound) {
            while ((i < arr.length - 1)
                    && (p.slopeTo(arr[i]) == p.slopeTo(arr[i + 1]))) {
                i++;
                cnt++;
            }
        }

        if (cnt > 3) {
            return i;
        }
        else {
            return -1;
        }
    }

    /**
     * Returns the number of line segments with 4 or more collinear points found in the array given to the constructor.
     *
     * @return The number of line segments with at least 4 collinear points.
     */
    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.size();
    }

    /**
     * Returns line segments that have at least 4 collinear points. Each line segment is defined by the smallest and
     * greatest point from the collinear points.
     *
     * @return An array of line segments that contain at least 4 collinear points.
     */
    public LineSegment[] segments() {
        // the line segments
        LineSegment[] result = new LineSegment[lineSegments.size()];
        return lineSegments.toArray(result);
    }

    /**
     * Driver client for the {@code FastCollinearPoints} class.
     *
     * @param args
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}