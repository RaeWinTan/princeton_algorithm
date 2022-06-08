/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(s.charAt(s.length() - 1));
            }
            else BinaryStdOut.write(s.charAt(csa.index(i) - 1));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int[] counter = new int[256];
        for (int i = 0; i < s.length(); i++) counter[s.charAt(i)]++;
        int[] offset = new int[256];
        int cum = 0;
        for (int i = 1; i < 256; i++) {
            offset[i] = cum + counter[i - 1];
            cum += counter[i - 1];
        }
        counter = new int[256];
        int[] next = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            next[offset[s.charAt(i)] + counter[s.charAt(i)]] = i;
            counter[s.charAt(i)]++;
        }
        int result = next[first];
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(result));
            result = next[result];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
