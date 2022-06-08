/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class PointSET {
    private SET<Point2D> pts;

    public PointSET() {
        this.pts = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return this.pts.isEmpty();
    }                      // is the set empty?

    public int size() {
        return this.pts.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("insert point is null");
        this.pts.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("contains point is null");
        return this.pts.contains(p);
    }         // does the set contain point p?

    public void draw() {
        for (Point2D p : this.pts)
            StdDraw.point(p.x(), p.y());
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("range rect is null");
        LinkedList<Point2D> ll = new LinkedList<Point2D>();
        for (Point2D p : this.pts) if (rect.contains(p)) ll.add(p);
        return ll;
    }// all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("nearest point is null");
        if (this.isEmpty()) return null;
        Point2D n = null;
        for (Point2D i : this.pts) {
            if (n == null) n = i;
            else {
                if (i.distanceSquaredTo(p) < n.distanceSquaredTo(p)) n = i;
            }
        }
        return n;
    }// a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                  // unit testing of the methods (optional)
}
