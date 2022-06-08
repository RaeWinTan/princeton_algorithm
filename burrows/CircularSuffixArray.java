/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    // circular suffix array of s
    private String s;
    private CircularSuffix[] suffixArr;

    public CircularSuffixArray(String s) {
        this.s = s;
        this.suffixArr = new CircularSuffix[s.length()];
        this.getSA(s);

    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        public String suffix;
        public int pos;

        public CircularSuffix(String suffix, int pos) {
            this.suffix = suffix;
            this.pos = pos;
        }

        public char charAt(int i) {
            return this.suffix.charAt((i + pos) % this.suffix.length());
        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < this.suffix.length(); i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return 1;
            }
            return 0;
        }
    }

    private void getSA(String s) {
        for (int i = 0; i < s.length(); i++) {
            this.suffixArr[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(this.suffixArr);
    }


    // length of s
    public int length() {
        return this.s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return this.suffixArr[i].pos;
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray c = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) StdOut.println(c.index(i));
    }
}
