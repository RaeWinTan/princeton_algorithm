import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private int noOfSegs;

    private LinkedList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Arrays.sort(points);
        this.noOfSegs = 0;
        this.segments = new LinkedList<LineSegment>();
        if (points.length < 4) {
            int m = 0;
            while (m < points.length - 1) {
                if (points[m] == null) throw new IllegalArgumentException("null point");
                if (points[m].compareTo(points[m + 1]) == 0) {
                    throw new IllegalArgumentException("duplicate point");
                }
                m++;
            }
            if (points[m] == null) throw new IllegalArgumentException("null point");
        }
        int i, j, k, l;
        for (i = 0; i < points.length - 3; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null poitn");
            for (j = i + 1; j < points.length - 2; j++) {
                if (points[j] == null) throw new IllegalArgumentException("null poitn");
                for (k = j + 1; k < points.length - 1; k++) {
                    if (points[k] == null) throw new IllegalArgumentException("null poitn");
                    for (l = k + 1; l < points.length; l++) {
                        if (points[l] == null) throw new IllegalArgumentException("null poitn");
                        Point[] pts = { points[i], points[j], points[k], points[l] };
                        
                        if (pts[0].compareTo(pts[1]) == 0 || pts[1].compareTo(pts[2]) == 0
                                || pts[2].compareTo(pts[3]) == 0)
                            throw new IllegalArgumentException("duplicate point");
                        int m = 1;
                        while (pts[0].slopeTo(pts[m]) == pts[0].slopeTo(pts[m + 1])) {
                            m++;
                            if (m == 3) {
                                this.noOfSegs++;
                                this.segments.add(new LineSegment(pts[0], pts[3]));
                                break;
                            }
                        }
                    }

                }
            }
        }
    }

    public int numberOfSegments() {
        return this.noOfSegs;
    }


    public LineSegment[] segments() {
        return this.segments.toArray(new LineSegment[this.noOfSegs]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
