/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTreeVisualizer {

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        int d = 0;
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdDraw.text(p.x(), p.y(), "" + ++d);
            StdDraw.show();
            brute.insert(p);
        }
        kdtree.draw();
        kdtree.nearest(new Point2D(0.72, 0.56)).draw();
        StdDraw.text(0.72, 0.56, "Q");

        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        //StdDraw.enableDoubleBuffering();



        /*
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    //StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.text(p.x(), p.y(), "" + ++d);
                    StdDraw.show();
                }
            }
            StdDraw.pause(20);
            StdOut.println(kdtree.size());
        }*/

    }
}
