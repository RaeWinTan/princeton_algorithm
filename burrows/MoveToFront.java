/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    private static int ascii_length = 256;

    private static int[] permutation() {
        int[] ans = new int[ascii_length];
        for (int i = 0; i < ascii_length; i++) ans[i] = i;
        return ans;
    }

    public static void encode() {
        int[] perm = permutation();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i < ascii_length; i++) {
                if ((int) c == perm[i]) {
                    BinaryStdOut.write((byte) i);
                    System.arraycopy(perm, 0, perm, 1, i);
                    perm[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] perm = permutation();
        while (!BinaryStdIn.isEmpty()) {
            int i = (int) BinaryStdIn.readChar();
            int c = perm[i];
            BinaryStdOut.write((byte) c);
            System.arraycopy(perm, 0, perm, 1, i);
            perm[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
