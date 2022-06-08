/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("G null");
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int ans = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(this.G, w);
        for (int i = 0; i < this.G.V(); i++) {
            int dist = Integer.MAX_VALUE;
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) dist = bfsv.distTo(i) + bfsw.distTo(i);
            if (dist < ans) ans = dist;
        }
        if (ans == Integer.MAX_VALUE) return -1;
        return ans;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int ans = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(this.G, w);
        for (int i = 0; i < this.G.V(); i++) {
            int dist = Integer.MAX_VALUE;
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) dist = bfsv.distTo(i) + bfsw.distTo(i);
            if (dist < ans) {
                ans = dist;
            }
        }
        if (ans == Integer.MAX_VALUE) return -1;
        return ans;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int val = Integer.MAX_VALUE;
        int pos = -1;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(this.G, w);
        for (int i = 0; i < this.G.V(); i++) {
            int dist = Integer.MAX_VALUE;
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                dist = bfsv.distTo(i) + bfsw.distTo(i);
            }
            if (dist < val) {
                val = dist;
                pos = i;
            }
        }
        return pos;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("iterable null");
        int val = Integer.MAX_VALUE;
        int pos = -1;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(this.G, w);
        for (int i = 0; i < this.G.V(); i++) {
            int dist = Integer.MAX_VALUE;

            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                dist = bfsv.distTo(i) + bfsw.distTo(i);
            }
            if (dist < val) {
                val = dist;
                pos = i;
            }
        }
        return pos;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
