/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    // create a seam carver object based on the given picture
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= this.width() || y < 0 || y >= this.height())
            throw new IllegalArgumentException();
        if (x == this.width() - 1 || x == 0 || y == this.height() - 1 || y == 0) return 1000;
        Color x1 = this.picture.get(x - 1, y);
        Color x2 = this.picture.get(x + 1, y);
        Color y1 = this.picture.get(x, y - 1);
        Color y2 = this.picture.get(x, y + 1);
        double xGrad = (x2.getRed() - x1.getRed()) * (x2.getRed() - x1.getRed()) +
                (x2.getBlue() - x1.getBlue()) * (x2.getBlue() - x1.getBlue()) +
                (x2.getGreen() - x1.getGreen()) * (x2.getGreen() - x1.getGreen());
        double yGrad = (y2.getRed() - y1.getRed()) * (y2.getRed() - y1.getRed()) +
                (y2.getBlue() - y1.getBlue()) * (y2.getBlue() - y1.getBlue()) +
                (y2.getGreen() - y1.getGreen()) * (y2.getGreen() - y1.getGreen());
        return Math.sqrt(xGrad + yGrad);
    }

    private double[][] energyMatrix() {
        double[][] energyMatrix = new double[this.height()][this.width()];
        for (int h = 0; h < this.height(); h++) {
            for (int w = 0; w < this.width(); w++) {
                energyMatrix[h][w] = this.energy(w, h);
            }

        }
        return energyMatrix;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (this.height() <= 2 || width() <= 2) {
            int[] ans = new int[this.width()];
            for (int i = 0; i < this.width(); i++) ans[i] = 0;
            return ans;
        }
        double[][] energyMatrix = this.energyMatrix();
        for (int w = this.width() - 2; w >= 0; w--) {
            for (int h = 0; h < this.height(); h++) {
                if (h == 0) {//right & right bottom
                    if (Double.compare(energyMatrix[h][w + 1], energyMatrix[h + 1][w + 1]) < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h][w + 1];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w + 1];

                }
                else if (h == this.height() - 1) { // right & right top
                    if (Double.compare(energyMatrix[h][w + 1], energyMatrix[h - 1][w + 1]) < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h][w + 1];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h - 1][w + 1];
                }
                else {
                    //right [h][w+1] //righttop [h-1][w+1] //rightbottom [h+1][w+1]
                    if (Double.compare(energyMatrix[h][w + 1], energyMatrix[h - 1][w + 1]) < 0
                            &&
                            Double.compare(energyMatrix[h][w + 1], energyMatrix[h + 1][w + 1])
                                    < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h][w + 1];
                    else if (Double.compare(energyMatrix[h - 1][w + 1], energyMatrix[h][w + 1]) < 0
                            &&
                            Double.compare(energyMatrix[h - 1][w + 1], energyMatrix[h + 1][w + 1])
                                    < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h - 1][w + 1];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w + 1];
                }
            }

        }

        int[] ans = new int[this.width()];
        int rootIdx = 0;
        double rootVal = energyMatrix[rootIdx][0];
        for (int h = 0; h < this.height(); h++) {
            if (Double.compare(energyMatrix[h][0], rootVal) < 0) {
                rootIdx = h;
                rootVal = energyMatrix[h][0];
            }
        }
        ans[0] = rootIdx;
        for (int w = 1; w < this.width(); w++) {
            double currVal = energyMatrix[rootIdx][w];
            //previous root is root Idx
            int currRoot = rootIdx;
            for (int h = rootIdx - 1; h <= rootIdx + 1; h++) {
                if (h < 0 || h >= this.height()) {
                    continue;
                }
                if (Double.compare(energyMatrix[h][w], currVal) < 0) {
                    currRoot = h;
                    currVal = energyMatrix[h][w];
                }
            }
            rootIdx = currRoot;
            ans[w] = currRoot;
        }
        return ans;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (this.height() <= 2 || width() <= 2) {
            int[] ans = new int[this.height()];
            for (int i = 0; i < this.height(); i++) ans[i] = 0;
            return ans;
        }
        //create energy matrix
        double[][] energyMatrix = this.energyMatrix();
        for (int h = this.height() - 2; h >= 0; h--) {
            for (int w = 0; w < this.width(); w++) {
                if (w == 0) {
                    if (Double.compare(energyMatrix[h + 1][w + 1], energyMatrix[h + 1][w]) < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w + 1];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w];

                }
                else if (w == this.width() - 1) {
                    if (Double.compare(energyMatrix[h + 1][w - 1], energyMatrix[h + 1][w]) < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w - 1];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w];
                }
                else {
                    if (Double.compare(energyMatrix[h + 1][w - 1], energyMatrix[h + 1][w]) < 0
                            &&
                            Double.compare(energyMatrix[h + 1][w - 1], energyMatrix[h + 1][w + 1])
                                    < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w - 1];
                    else if (Double.compare(energyMatrix[h + 1][w], energyMatrix[h + 1][w - 1]) < 0
                            && Double.compare(energyMatrix[h + 1][w], energyMatrix[h + 1][w + 1])
                            < 0)
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w];
                    else
                        energyMatrix[h][w] = energyMatrix[h][w] + energyMatrix[h + 1][w + 1];
                }
            }

        }
        int[] ans = new int[this.height()];
        int rootIdx = 0;
        double rootVal = energyMatrix[0][rootIdx];
        for (int w = 0; w < this.width(); w++) {
            if (Double.compare(energyMatrix[0][w], rootVal) < 0) {
                rootIdx = w;
                rootVal = energyMatrix[0][w];
            }
        }
        ans[0] = rootIdx;
        for (int h = 1; h < this.height(); h++) {
            double currVal = energyMatrix[h][rootIdx];
            //previous root is root Idx
            int currRoot = rootIdx;
            for (int w = rootIdx - 1; w <= rootIdx + 1; w++) {
                if (w < 0 || w >= this.width()) {
                    continue;
                }
                if (Double.compare(energyMatrix[h][w], currVal) < 0) {
                    currRoot = w;
                    currVal = energyMatrix[h][w];
                }
            }
            rootIdx = currRoot;
            ans[h] = currRoot;
        }
        return ans;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (this.picture.height() <= 1 || seam == null || seam.length != this.picture.width())
            throw new IllegalArgumentException();
        Picture newPic = new Picture(this.picture.width(), this.picture.height() - 1);
        for (int w = 0; w < this.picture.width(); w++) {
            if (seam[w] < 0 || seam[w] >= this.picture.height())
                throw new IllegalArgumentException();
            for (int h = 0; h < this.picture.height(); h++) {
                if (h < seam[w]) newPic.set(w, h, this.picture.get(w, h));
                else if (h > seam[w]) newPic.set(w, h - 1, this.picture.get(w, h));
                if (w > 0) {
                    if (!(seam[w - 1] - 1 <= seam[w] && seam[w - 1] + 1 >= seam[w]))
                        throw new IllegalArgumentException();
                }
            }
        }
        this.picture = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (this.picture.width() <= 1 || seam == null || seam.length != this.picture.height())
            throw new IllegalArgumentException();
        Picture newPic = new Picture(this.picture.width() - 1, this.picture.height());
        for (int h = 0; h < this.picture.height(); h++) {
            if (seam[h] < 0 || seam[h] >= this.picture.width())
                throw new IllegalArgumentException();
            for (int w = 0; w < this.picture.width(); w++) {
                if (w < seam[h]) newPic.set(w, h, this.picture.get(w, h));
                else if (w > seam[h]) newPic.set(w - 1, h, this.picture.get(w, h));
            }
        }
        this.picture = newPic;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.println();
        StdOut.println("horizontal seam stack trace : ");
        for (int i : sc.findHorizontalSeam()) {
            StdOut.print(i + " ");
        }
        StdOut.println();
    }
}
