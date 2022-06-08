import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int n;
    private final int[][] t;

    public Board(int[][] tiles) {
        this.t = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.t[i][j] = tiles[i][j];
            }
        }
        this.n = tiles.length;
    }


    // string representation of this board
    public String toString() {
        String f = "";
        if (this.dimension() * this.dimension() - 1 < 100) {
            f = "%2d";
        }
        else if (this.dimension() * this.dimension() - 1 < 1000) {
            f = "%3d";
        }
        else if (this.dimension() * this.dimension() - 1 < 10000) {
            f = "%4d";
        }
        else if (this.dimension() * this.dimension() - 1 < 100000) {
            f = "%5d";
        }
        String s = "" + this.dimension() + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == n - 1) s += String.format(f, this.t[i][j]);
                else s += String.format(f, this.t[i][j]) + " ";
            }
            if (i < n - 1) s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int h = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.t[i][j] != 0 && this.t[i][j] != i * this.n + j + 1) {
                    h++;
                }
            }
        }
        return h;
    }

    private int[] getCoor(int num) {
        num -= 1;
        int[] r = new int[2];
        r[0] = num / this.n;
        r[1] = num % this.n;
        return r;
    }

    private int getNum(int i, int j) {
        return i * this.n + j + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int m = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.t[i][j] != 0 && this.t[i][j] != this.getNum(i, j)) {
                    int[] correctCoor = this.getCoor(this.t[i][j]);
                    m += Math.abs(correctCoor[0] - i);
                    m += Math.abs(correctCoor[1] - j);
                }
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass())
            return false;
        Board temp = (Board) y;
        if (temp.dimension() != dimension()) {
            return false;
        }
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (temp.t[i][j] != t[i][j]) return false;
            }
        }
        return true;
    }

    private int[][] copyOft() {
        int[][] c = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                c[i][j] = this.t[i][j];
            }
        }
        return c;
    }

    private boolean corner(int[] b) {
        return (b[0] == 0 || b[0] == this.n - 1) && (b[1] == 0 || b[1] == this.n - 1);
    }

    private boolean sides(int[] b) {
        return !this.corner(b) && (b[0] == 0 || b[1] == 0 || b[0] == this.n - 1
                || b[1] == this.n - 1);
    }

    private void swp(int[][] curr, int[] b, int r, int c) {
        int s = curr[b[0]][b[1]];
        curr[b[0]][b[1]] = curr[r][c];
        curr[r][c] = s;
    }

    private int[][][] cornerNeigh(int[] b) {
        int row = b[0];
        int col = b[1];
        int[][][] r = new int[2][this.n][this.n];
        if (row == 0 && col == 0) {
            r[0] = copyOft();
            this.swp(r[0], b, 0, 1);
            r[1] = copyOft();
            this.swp(r[1], b, 1, 0);
        }
        else if (row == 0 && col == this.n - 1) {
            r[0] = copyOft();
            this.swp(r[0], b, 0, this.n - 2);
            r[1] = copyOft();
            this.swp(r[1], b, 1, this.n - 1);
        }
        else if (row == this.n - 1 && col == 0) {
            r[0] = copyOft();
            this.swp(r[0], b, this.n - 1, 1);
            r[1] = copyOft();
            this.swp(r[1], b, this.n - 2, 0);
        }
        else if (row == this.n - 1 && col == this.n - 1) {
            r[0] = copyOft();
            this.swp(r[0], b, this.n - 1, this.n - 2);
            r[1] = copyOft();
            this.swp(r[1], b, this.n - 2, this.n - 1);
        }
        return r;
    }

    private int[][][] sideNeigh(int[] b) {
        int row = b[0];
        int col = b[1];
        int[][][] r = new int[3][this.n][this.n];
        if (col == 0) {
            r[0] = this.copyOft();
            this.swp(r[0], b, b[0] - 1, b[1]);
            r[1] = this.copyOft();
            this.swp(r[1], b, b[0] + 1, b[1]);
            r[2] = this.copyOft();
            this.swp(r[2], b, b[0], b[1] + 1);
        }
        else if (col == this.n - 1) {
            r[0] = this.copyOft();
            this.swp(r[0], b, b[0] - 1, b[1]);
            r[1] = this.copyOft();
            this.swp(r[1], b, b[0] + 1, b[1]);
            r[2] = this.copyOft();
            this.swp(r[2], b, b[0], b[1] - 1);
        }
        else if (row == 0) {
            r[0] = this.copyOft();
            this.swp(r[0], b, b[0], b[1] + 1);
            r[1] = this.copyOft();
            this.swp(r[1], b, b[0], b[1] - 1);
            r[2] = this.copyOft();
            this.swp(r[2], b, b[0] + 1, b[1]);
        }
        else if (row == this.n - 1) {
            r[0] = this.copyOft();
            this.swp(r[0], b, b[0], b[1] + 1);
            r[1] = this.copyOft();
            this.swp(r[1], b, b[0], b[1] - 1);
            r[2] = this.copyOft();
            this.swp(r[2], b, b[0] - 1, b[1]);
        }

        return r;
    }

    private int[][][] middleNeigh(int[] b) {
        int row = b[0];
        int col = b[1];
        int[][][] r = new int[4][this.n][this.n];
        r[0] = this.copyOft();
        this.swp(r[0], b, row - 1, col);
        r[1] = this.copyOft();
        this.swp(r[1], b, row, col - 1);
        r[2] = this.copyOft();
        this.swp(r[2], b, row, col + 1);
        r[3] = this.copyOft();
        this.swp(r[3], b, row + 1, col);
        return r;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neigh = new LinkedList<Board>();
        int[] b = new int[2];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.t[i][j] == 0) {
                    b[0] = i;
                    b[1] = j;
                }
            }
        }
        if (this.corner(b)) {
            for (int[][] ns : this.cornerNeigh(b)) neigh.add(new Board(ns));
        }
        else if (this.sides(b)) {
            for (int[][] ns : this.sideNeigh(b)) {
                neigh.add(new Board(ns));
            }
        }
        else {
            for (int[][] ns : this.middleNeigh(b)) neigh.add(new Board(ns));
        }
        return neigh;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] nt = new int[this.n][this.n];
        for (int i = 0; i < this.t.length; i++) {
            for (int j = 0; j < this.t.length; j++) {
                nt[i][j] = this.t[i][j];
            }
        }
        if (nt[0][0] == 0) {
            int[] b = { 1, 1 };
            this.swp(nt, b, 0, 1);
        }
        else if (nt[0][1] == 0) {
            int[] b = { 0, 0 };
            this.swp(nt, b, 1, 0);
        }
        else {
            int[] b = { 0, 0 };
            this.swp(nt, b, 0, 1);
        }
        return new Board(nt);
    }

    public static void main(String[] args) {


    }
}
