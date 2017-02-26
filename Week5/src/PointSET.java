import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        points = new TreeSet<>();
    }

    /**
     * Is the set empty?
     *
     * @return true if there are no point in the set; false otherwise
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Number of points in the set
     *
     * @return the size of the points set
     */
    public int size() {
        return points.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     *
     * @param p the Point to be added to the set.
     * @throws {@code NullPointerException} if null is passed as argument
     */
    public void insert(Point2D p) {
        throwIfNull(p);
        points.add(p);
    }

    /**
     * Does the set contain point p?
     *
     * @param p the point to be checked of its already in the set
     * @return true if the point is in the set; false otherwise
     * @throws {@code NullPointerException} if null is passed as argument
     */
    public boolean contains(Point2D p) {
        throwIfNull(p);
        return points.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    /**
     * All points that are inside the rectangle
     *
     * @param rect the enclosing rectangle
     * @return a sequence of all points that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        throwIfNull(rect);

        List<Point2D> pointsInRect = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) pointsInRect.add(new Point2D(point.x(), point.y()));
        }
        return pointsInRect;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p point for hom to search the neatest point in the set
     * @return the nearest point to the specified point argument; null is the set is empty
     * @throws {@code NullPointerException} if null is passed as argument
     */
    public Point2D nearest(Point2D p) {
        throwIfNull(p);

        double minDistance = Double.MAX_VALUE;
        double distance = 0.0;
        Point2D nearestPoint = null;

        for (Point2D point : points) {
            distance = point.distanceTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }

        if (nearestPoint != null) {
            return new Point2D(nearestPoint.x(), nearestPoint.y());
        }
        return nearestPoint;
    }

    /**
     * Throws {@NullPointerException} if argument is null
     *
     * @param obj the argument to be checked against null
     */
    private void throwIfNull(Object obj) {
        if (obj == null) throw new NullPointerException("Argument cannot be null");
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        PointSET set = new PointSET();
        assert (set.isEmpty());
        assert (set.size() == 0);

        set.insert(new Point2D(0.1, 0.1));
        assert (!set.isEmpty());
        assert (set.size() == 1);

        set.insert(new Point2D(0.7, 0.7));
        set.insert(new Point2D(0.1, 0.9));
        set.insert(new Point2D(0.9, 0.7));
        System.out.println(set.nearest(new Point2D(0.5, 0.5)));

        RectHV rect = new RectHV(0.0, 0.0, 0.8, 0.8);
        Iterable<Point2D> allPoints = set.range(rect);
        System.out.println("Points in rectangle: ");
        for (Point2D p : allPoints) {
            System.out.println(p);
        }
    }
}
