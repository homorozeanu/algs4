import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import jdk.nashorn.internal.ir.IfNode;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final int HORIZONTAL = -1;
    private static final int VERTiCAL = 1;
    private Node root = null;
    private Point2D champion = null;

    private enum Direction {
        UNKNOWN,
        LEFT_OR_BOTTOM,
        RIGHT_OR_TOP
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int N;              // # nodes in subtree rooted here

        public Node(Point2D p, int N, Node parent, int parentNodeOrientation, Direction direction) {
            this.p = new Point2D(p.x(), p.y());
            this.N = N;

            //---- Set the axis-aligned rectangle for this node
            if (parent == null) { // root node
                this.rect = new RectHV(0, 0, 1, 1);
            } else { // not the root node
                if (parentNodeOrientation == VERTiCAL) {
                    if (direction == Direction.LEFT_OR_BOTTOM) {
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                    } else { // direction == RIGHT_OR_TOP
                        this.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                } else { // parentNodeOrientation == HORIZONTAL
                    if (direction == Direction.LEFT_OR_BOTTOM) {
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                    } else { // direction == RIGHT_OR_TOP
                        this.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
            }
        }
    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * Is the set empty?
     *
     * @return true if there are no point in the set; false otherwise
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * Number of points in the set
     *
     * @return the size of the points set
     */
    public int size() {
        if (root == null) return 0;
        return root.N;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    }

    /**
     * Add the point to the set (if it is not already in the set)
     *
     * @param p the Point to be added to the set.
     * @throws {@code NullPointerException} if null is passed as argument
     */
    public void insert(Point2D p) {
        throwIfNull(p);
        root = put(root, p, VERTiCAL, null, Direction.UNKNOWN);
//        if (root.rect == null) root.rect = new RectHV(0, 0, 1, 1);
    }

    private Node put(Node x, Point2D p, int orientation, Node parent, Direction direction) {
        if (x == null) return new Node(p, 1, parent, orientation * (-1), direction);
        int cmp = compareByOrientation(x, p, orientation);
        if (cmp < 0) {
            x.lb = put(x.lb, p, orientation * (-1), x, Direction.LEFT_OR_BOTTOM);
        } else if (cmp > 0) {
            x.rt = put(x.rt, p, orientation * (-1), x, Direction.RIGHT_OR_TOP);
        } else {
            x.p = p;
        }
        x.N = size(x.lb) + size(x.rt) + 1;
        return x;
    }

    private int compareByOrientation(Node x, Point2D p, int orientation) {
        int cmp = 0;
        if (orientation == VERTiCAL) {
            if (p.x() < x.p.x()) {
                cmp = -1;
            } else if ((p.x() > x.p.x()) || (p.x() == x.p.x() && p.y() != x.p.y())) {
                cmp = 1;
            } else {
                cmp = 0;
            }
        } else { // orientation == HORIZONATAL
            if (p.y() < x.p.y()) {
                cmp = -1;
            } else if ((p.y() > x.p.y()) || (p.y() == x.p.y() && p.x() != x.p.x())) {
                cmp = 1;
            } else {
                cmp = 0;
            }
        }

        return cmp;
    }

    /**
     * Does the set contain point p?
     *
     * @param p the point to be checked of its already in the set
     * @return true if the point is in the set; false otherwise
     * @throws {@code NullPointerException} if null is passed as argument
     */
    public boolean contains(Point2D p) {
        return contains(root, p, VERTiCAL);
    }

    private boolean contains(Node x, Point2D p, int orientation) {
        if (x == null) return false;
        int cmp = compareByOrientation(x, p, orientation);
        if (cmp < 0) {
            return contains(x.lb, p, orientation * (-1));
        } else if (cmp > 0) {
            return contains(x.rt, p, orientation * (-1));
        } else {
            return true;
        }
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        if (isEmpty()) return;
        draw(root, VERTiCAL);
    }

    private void draw(Node x, int orientation) {
        if (x == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        StdDraw.setPenRadius();
        if (orientation == VERTiCAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else { // orientation == HORIZONTAL
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        draw(x.lb, orientation * (-1));
        draw(x.rt, orientation * (-1));
    }

    /**
     * All points that are inside the rectangle
     *
     * @param rect search query (rectangle)
     * @return A list with all points found in the search query (rectangle)
     * @throws NullPointerException if argument is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        throwIfNull(rect);
        List<Point2D> result = new ArrayList<>();
        range(rect, root, result);
        return result;
    }

    private void range(RectHV rect, Node x, List<Point2D> result) {
        if (x == null) return;

        if (rect.contains(x.p)) result.add(new Point2D(x.p.x(), x.p.y()));

        if (x.lb != null && x.lb.rect.intersects(rect)) {
            range(rect, x.lb, result);
            range(rect, x.rt, result);
        } else if (x.rt != null && x.rt.rect.intersects(rect)) {
            range(rect, x.rt, result);
            range(rect, x.lb, result);
        }
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p search nearest neighbor of this point
     * @return the nearest neighbor if found, null otherwise
     */
    public Point2D nearest(Point2D p) {
        throwIfNull(p);
        if (isEmpty()) return null;
        nearest(p, root, VERTiCAL, Double.MAX_VALUE);
        return new Point2D(champion.x(), champion.y());
    }

    private void nearest(Point2D p, Node x, int orientation, double minDistance) {
        if (x == null) return;
        if (champion == null) champion = x.p;

        double actualDistance = p.distanceSquaredTo(x.p);
        if (actualDistance < minDistance) {
            minDistance = actualDistance;
            champion = x.p;
        }

        if (orientation == VERTiCAL) {
            if (p.x() < x.p.x()) { // go left in tree
                nearest(p, x.lb, orientation * (-1), minDistance);
            } else { // go right in tree
                nearest(p, x.rt, orientation * (-1), minDistance);
            }
        } else { // orientation == HORIZONTAL
            if (p.y() < x.p.y()) { // go left in tree
                nearest(p, x.lb, orientation * (-1), minDistance);
            } else { // go right in tree
                nearest(p, x.rt, orientation * (-1), minDistance);
            }
        }
    }

    private void throwIfNull(Object obj) {
        if (obj == null) throw new NullPointerException("Argument cannot be null");
    }

//    @Override
//    public String toString() {
//        return toStringRec(root);
//    }
//
//    private String toStringRec(Node h) {
//        if (h == null) return "";
//        String s = h.p.toString() + "\n";
//        s += toStringRec(h.lb);
//        s += toStringRec(h.rt);
//        return s;
//    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
//        KdTree tree = new KdTree();
//        assert tree.isEmpty();
//        assert tree.size() == 0;
//
//        tree.insert(new Point2D(0.7, 0.2));
//        tree.insert(new Point2D(0.5, 0.4));
//        tree.insert(new Point2D(0.2, 0.3));
//        tree.insert(new Point2D(0.4, 0.7));
//        tree.insert(new Point2D(0.9, 0.6));
//        assert tree.isEmpty() == false;
//        assert tree.size() == 5;
//
//        System.out.println(tree.toString());
//
//        System.out.println(tree.contains(new Point2D(0.4, 0.7)));
//        System.out.println(tree.contains(new Point2D(0.0, 0.0)));
//
//        System.out.println("Nearest to (8,5): " + tree.nearest(new Point2D(0.8, 0.5)));
//        Iterable<Point2D> result = tree.range(new RectHV(0.8, 0.1, 0.9, 0.2));
//
//        System.out.println("Query result: ");
//        for (Point2D point2D : result) {
//            System.out.print(point2D.toString() + ", ");
//        }

        double x, y;
        KdTree randTree = new KdTree();
        for (int i = 1; i <= 1000; i++) {
            x = (double) StdRandom.uniform(1000) / 1000;
            y = (double) StdRandom.uniform(1000) / 1000;
            randTree.insert(new Point2D(x, y));
            System.out.println("insert:" + x + ", " + y);
            System.out.println("\ti=" + i + ", size()=" + randTree.size());
            assert i == randTree.size();
        }

//        tree.draw();

//        randTree.insert(new Point2D(.7, 0.4));
//        randTree.insert(new Point2D(0.5, 0.8));
//        randTree.insert(new Point2D(0.6, 0.1));
//        randTree.insert(new Point2D(0.8, 0.4));
//        randTree.insert(new Point2D(0.6, 0.2));
//        assert 5 == randTree.size();
    }
}
