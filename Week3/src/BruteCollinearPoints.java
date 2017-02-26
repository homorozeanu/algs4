import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments. To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 */
public class BruteCollinearPoints {
    //---- list holding a line segment of 4 collinear points
    private final List<LineSegment> lineSegments;
    private final Point[] points;

    /**
     * Checks 4 points at a time from the supplied array whether they are collinear.
     *
     * @param points A list of points to be checked.
     */
    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points

        //---- Guards: check for null parameter and for null values in the array parameter
        if (points == null) throw new NullPointerException("argument cannot be null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new NullPointerException("argument cannot be null");
        }

        this.points = points.clone();
        Arrays.sort(this.points);

        lineSegments = new ArrayList<>();
        LineSegment pToS = null;

        for (int i = 0; i < this.points.length - 3; i++) {
            for (int j = i + 1; j < this.points.length - 2; j++) {
                for (int k = j + 1; k < this.points.length - 1; k++) {
                    for (int l = k + 1; l < this.points.length; l++) {
                        if (pointsAreCollinear(i, j, k, l)) {
                            pToS = new LineSegment(this.points[i], this.points[l]);
                            lineSegments.add(pToS);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the number of line segments with 4 collinear points found in the array given to the constructor.
     *
     * @return The number of line segments with 4 collinear points.
     */

    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.size();
    }

    /**
     * Returns line segments that have 4 collinear points. Each line segment is defined by the smallest and
     * greatest point from the 4 collinear points.
     *
     * @return An array of line segments that contain 4 collinear points.
     */
    public LineSegment[] segments() {
        // the line segments
        LineSegment[] result = new LineSegment[lineSegments.size()];
        return lineSegments.toArray(result);
    }

    /**
     * Checks if 4 points are collinear.
     *
     * @param p The index of point 1.
     * @param q The index of point 2.
     * @param r The index of point 3.
     * @param s The index of point 4.
     * @return {@code true} if all 4 points are collinear, {@code false} otherwise.
     * @throws IllegalArgumentException is a duplicate point is found.
     */
    private boolean pointsAreCollinear(int p, int q, int r, int s) {
        boolean collinear = false;
        double pqSlope = points[p].slopeTo(points[q]);
        double prSlope = points[p].slopeTo(points[r]);
        double psSlope = points[p].slopeTo(points[s]);

        if (pqSlope == Double.NEGATIVE_INFINITY
                || prSlope == Double.NEGATIVE_INFINITY
                || psSlope == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("duplicate points not allowed");
        }

        if (pqSlope == prSlope && pqSlope == psSlope) collinear = true;

        return collinear;
    }

    /**
     * Driver client for the {@code BruteCollinearPoints} class.
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
