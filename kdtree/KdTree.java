/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {
    private class PointNode {
        boolean isVert;//line drawn is it horizontal(compares the vertical aspect of points)
        PointNode left;
        PointNode right;
        Point2D pt;

        public PointNode(boolean v, PointNode l, PointNode r, Point2D p) {
            this.isVert = v;
            this.left = l;
            this.right = r;
            this.pt = p;
        }
    }

    private int size;
    private PointNode root;

    public KdTree() {
        this.size = 0;
        this.root = null;
    }                               // construct an empty set of points


    public boolean isEmpty() {
        return size == 0;
    }                      // is the set empty?

    public int size() {
        return this.size;
    }                         // number of points in the set

    private void insertRecur(PointNode currPn, Point2D p) {
        if (currPn.pt.equals(p)) return;
        if (currPn.isVert) {
            if (p.y() < currPn.pt.y()) {
                if (currPn.left == null) {
                    this.size++;
                    currPn.left = new PointNode(false, null, null, p);
                    return;
                }
                this.insertRecur(currPn.left, p);
            }
            else {
                if (currPn.right == null) {
                    this.size++;
                    currPn.right = new PointNode(false, null, null, p);
                    return;
                }
                this.insertRecur(currPn.right, p);
            }
        }
        else {
            if (p.x() < currPn.pt.x()) {
                if (currPn.left == null) {
                    this.size++;
                    currPn.left = new PointNode(true, null, null, p);
                    return;
                }
                this.insertRecur(currPn.left, p);
            }
            else {
                if (currPn.right == null) {
                    this.size++;
                    currPn.right = new PointNode(true, null, null, p);
                    return;
                }
                this.insertRecur(currPn.right, p);
            }
        }
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("insert point is null");
        if (this.root == null) {
            this.size++;
            this.root = new PointNode(false, null, null, p);
        }
        else this.insertRecur(this.root, p);

    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("contains point is null");
        PointNode c = this.root;
        while (c != null) {
            if (c.pt.equals(p)) return true;
            else {
                if (c.isVert) {
                    if (p.y() < c.pt.y()) c = c.left;
                    else c = c.right;
                }
                else {
                    if (p.x() < c.pt.x()) c = c.left;
                    else c = c.right;
                }
            }
        }
        return false;
    }

    private void drawRecur(PointNode node, double minx, double maxx, double miny, double maxy) {
        //debug
        //StdDraw.setPenRadius(0.01);
        //StdDraw.setPenColor(StdDraw.BLUE);
        //StdDraw.point(node.pt.x(), node.pt.y());
        StdDraw.setPenRadius(0.0005);
        StdDraw.setPenColor(StdDraw.RED);
        if (node.isVert) StdDraw.line(minx, node.pt.y(), maxx, node.pt.y());
        else StdDraw.line(node.pt.x(), miny, node.pt.x(), maxy);
        //end of debug
        if (node.left != null) {
            //debug
            if (node.isVert) this.drawRecur(node.left, minx, maxx, miny, node.pt.y());
            else this.drawRecur(node.left, minx, node.pt.x(), miny, maxy);
        }
        if (node.right != null) {
            //debug
            if (node.isVert) this.drawRecur(node.right, minx, maxx, node.pt.y(), maxy);
            else this.drawRecur(node.right, node.pt.x(), maxx, miny, maxy);

        }
    }

    public void draw() {
        this.drawRecur(this.root, 0.0, 1.0, 0.0, 1.0);
    } // draw all points to standard draw

    private void rangeRecur(LinkedList<Point2D> ll, RectHV range, RectHV r, PointNode node) {
        if (node == null) return;
        if (range.contains(node.pt)) ll.add(node.pt);
        if (node.isVert) {
            RectHV tr = new RectHV(r.xmin(), node.pt.y(), r.xmax(), r.ymax());
            RectHV br = new RectHV(r.xmin(), r.ymin(), r.xmax(), node.pt.y());
            if (tr.intersects(range) && node.right != null)
                this.rangeRecur(ll, range, tr, node.right);
            if (br.intersects(range) && node.left != null)
                this.rangeRecur(ll, range, br, node.left);
        }
        else {
            RectHV lr = new RectHV(r.xmin(), r.ymin(), node.pt.x(), r.ymax());
            RectHV rr = new RectHV(node.pt.x(), r.ymin(), r.xmax(), r.ymax());
            if (rr.intersects(range)) this.rangeRecur(ll, range, rr, node.right);
            if (lr.intersects(range)) this.rangeRecur(ll, range, lr, node.left);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Iterable rect is null");
        LinkedList<Point2D> ll = new LinkedList<Point2D>();
        this.rangeRecur(ll, rect, new RectHV(0, 0, 1, 1), this.root);
        return ll;
    }

    private void nearestRecur(Point2D p, PointNode node, PointNode[] curr, RectHV r) {
        if (node == null) return;
        if (node.pt.distanceSquaredTo(p) <= curr[0].pt.distanceSquaredTo(p)) curr[0] = node;
        if (node.isVert) {
            RectHV tr = new RectHV(r.xmin(), node.pt.y(), r.xmax(), r.ymax());
            RectHV br = new RectHV(r.xmin(), r.ymin(), r.xmax(), node.pt.y());
            if (tr.distanceSquaredTo(p) <= br.distanceSquaredTo(p)) {
                //top
                if (tr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.right, curr, tr);
                //bottom
                if (br.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.left, curr, br);
            }
            else {
                //bottom
                if (br.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.left, curr, br);
                //top
                if (tr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.right, curr, tr);
            }

        }
        else {
            RectHV lr = new RectHV(r.xmin(), r.ymin(), node.pt.x(), r.ymax());
            RectHV rr = new RectHV(node.pt.x(), r.ymin(), r.xmax(), r.ymax());
            if (rr.distanceSquaredTo(p) <= lr.distanceSquaredTo(p)) {
                //right
                if (rr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.right, curr, rr);
                //left
                if (lr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.left, curr, lr);

            }
            else {
                //left 54
                if (lr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.left, curr, lr);
                //right
                if (rr.distanceSquaredTo(p) <= p.distanceSquaredTo(curr[0].pt))
                    this.nearestRecur(p, node.right, curr, rr);
            }
        }
    }

    public Point2D nearest(Point2D p) {

        if (p == null) throw new IllegalArgumentException("nearest point is null");
        if (isEmpty()) return null;
        PointNode[] curr = { this.root };
        this.nearestRecur(p, this.root, curr, new RectHV(0, 0, 1, 1));
        return curr[0].pt;
    }// a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                  // unit testing of the methods (optional)
}
