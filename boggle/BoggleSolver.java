/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private TST<Integer> tst;
    //start from corner left, clockwise direction
    private final int[] rowShift = { -1, -1, -1, 0, 1, 1, 1, 0 };
    private final int[] colShift = { -1, 0, 1, 1, 1, 0, -1, -1 };

    public BoggleSolver(String[] dictionary) {
        //put the words in a trie for easy search
        this.tst = new TST<Integer>();
        int n = 0;
        for (String word : dictionary) this.tst.put(word, n++);


    }

    private boolean containsPrefix(String s) {
        for (String word : this.tst.keysWithPrefix(s)) return true;
        return false;
    }

    private boolean validGrid(int r, int c, BoggleBoard board) {
        if (r < 0 || r >= board.rows()) return false;
        if (c < 0 || c >= board.cols()) return false;
        return true;
    }


    private void getPaths(SET<String> ans, StringBuilder str, BoggleBoard board,
                          boolean[][] visited, int r, int c) {
        //prune out invalid prefixes
        if (str.length() != 0 && !this.containsPrefix(str.toString())) return;
        for (int i = 0; i < 8; i++) {
            int tmpR = r + this.rowShift[i];
            int tmpC = c + this.colShift[i];
            //dont revisit grid from same path
            if (this.validGrid(tmpR, tmpC, board) && !visited[tmpR][tmpC]) {
                StringBuilder newStr = new StringBuilder(str.toString());
                if (board.getLetter(tmpR, tmpC) == 'Q') newStr.append("QU");
                else newStr.append(board.getLetter(tmpR, tmpC));
                visited[tmpR][tmpC] = true;
                if (this.tst.contains(newStr.toString()) && newStr.length() >= 3)
                    ans.add(newStr.toString());
                this.getPaths(ans, newStr, board, visited, tmpR, tmpC);
                visited[tmpR][tmpC] = false;
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> ans = new SET<String>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) visited[r][c] = false;
        }
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                StringBuilder str = new StringBuilder();
                if (board.getLetter(r, c) == 'Q') str.append("QU");
                else str.append(board.getLetter(r, c));
                visited[r][c] = true;
                this.getPaths(ans, str, board, visited, r, c);
                visited[r][c] = false;
            }
        }
        return ans;

    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.tst.contains(word) || word.length() < 3) return 0;
        if (word.length() < 5) return 1;
        if (word.length() < 6) return 2;
        if (word.length() < 7) return 3;
        if (word.length() < 8) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Count = " + count);
    }

}

