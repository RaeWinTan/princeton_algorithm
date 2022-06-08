/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class Percolation {
    private boolean[][] sites;
    private int[] uf;
    private int[] treeSize;
    private int n;
    private int open;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n less than equal to 0");
        this.n = n;
        this.open = 0;
        this.sites = new boolean[n][n];
        this.uf = new int[n * n];
        this.treeSize = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.sites[i][j] = false;
                this.treeSize[this.getNum(i, j)] = 1;
                this.uf[this.getNum(i, j)] = this.getNum(i, j);
            }
        }
    }

    private int getNum(int r, int c) {
        return r * this.n + c;
    }

    private int[] getCoor(int num) {
        int a[] = new int[2];
        a[0] = num / this.n;
        a[1] = num % this.n;
        return a;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row -= 1;
        col -= 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0)
            throw new IllegalArgumentException("OPEN() out of range");
        if (this.sites[row][col]) return;
        this.sites[row][col] = true;
        this.open++;
        this.unionSides(this.getNum(row, col));
    }

    private void unionSides(int currPos) {

        if (this.checkUp(this.getCoor(currPos)[0], this.getCoor(currPos)[1])) {
            this.union(currPos, currPos - this.n);
        }
        if (this.checkDown(this.getCoor(currPos)[0], this.getCoor(currPos)[1])) {
            this.union(currPos, currPos + this.n);
        }
        if (this.checkLeft(this.getCoor(currPos)[0], this.getCoor(currPos)[1])) {
            this.union(currPos, currPos - 1);
        }
        if (this.checkRight(this.getCoor(currPos)[0], this.getCoor(currPos)[1])) {
            this.union(currPos, currPos + 1);
        }
    }

    private int root(int num) {
        int ans = num;
        while (this.uf[ans] != ans) {
            this.uf[ans] = this.uf[this.uf[ans]];
            ans = this.uf[ans];
        }
        return ans;
    }

    private void union(int curr, int other) {

        if (this.treeSize[this.root(curr)] > this.treeSize[this.root(other)]) {
            this.uf[this.root(other)] = this.root(curr);
            this.treeSize[this.root(curr)] += this.treeSize[this.root(other)];
        }
        else {
            this.uf[this.root(curr)] = this.root(other);
            this.treeSize[this.root(other)] += this.treeSize[this.root(curr)];
        }
    }

    private boolean checkUp(int row, int col) {
        row -= 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0) return false;
        return sites[row][col];
    }

    private boolean checkDown(int row, int col) {
        row += 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0) return false;
        return sites[row][col];
    }

    private boolean checkLeft(int row, int col) {
        col -= 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0) return false;
        return sites[row][col];
    }

    private boolean checkRight(int row, int col) {
        col += 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0) return false;
        return sites[row][col];
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row -= 1;
        col -= 1;
        if (row >= this.n || col >= this.n || row < 0 || col < 0)
            throw new IllegalArgumentException("ISOPEN() out of range");
        return this.sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row -= 1;
        col -= 1;
        for (int i = 0; i < this.n; i++) {
            if (this.sites[0][i]) {
                if (this.root(this.getNum(row, col)) == this.root(i)) {

                    return true;
                }
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return this.open;
    }

    public boolean percolates() {
        for (int i = 1; i <= this.n; i++) {
            if (this.isFull(this.n, i)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }

}
