/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.LinkedList;

public class FastCollinearPoints {
    private int noOfSegs;
    private LinkedList<LineSegment> segs;

    public FastCollinearPoints(Point[] points) {
        this.noOfSegs = 0;
        this.segs = new LinkedList<LineSegment>();
        if (points == null) throw new IllegalArgumentException();
        LinkedList<Point> pss = new LinkedList<Point>();
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
            pss.add(p);
        }
        for (Point p : points) {
            pss.sort(p.slopeOrder());
            int currIndex = 1;
            while (currIndex < pss.size()) {
                double currSlope = p.slopeTo(pss.get(currIndex));
                int noOfPoints = 0;
                LinkedList<Point> ls = new LinkedList<Point>();
                if (p.slopeTo(pss.get(currIndex)) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("Duplicate point");
                while (currIndex < pss.size() && p.slopeTo(pss.get(currIndex)) == currSlope) {
                    ls.add(pss.get(currIndex));
                    currSlope = p.slopeTo(pss.get(currIndex));
                    currIndex++;
                    noOfPoints++;
                }
                if (noOfPoints >= 3) {
                    ls.add(p);
                    Collections.sort(ls);
                    if (p.compareTo(ls.get(0)) == 0) {
                        this.segs.add(new LineSegment(ls.get(0), ls.get(ls.size() - 1)));
                        this.noOfSegs++;
                    }
                }
                if (noOfPoints == 0) currIndex++;
            }
        }


    }

    public int numberOfSegments() {
        return this.noOfSegs;
    }

    public LineSegment[] segments() {
        return this.segs.toArray(new LineSegment[this.noOfSegs]);
    }

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
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}
