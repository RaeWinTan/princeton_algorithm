/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wn;

    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("wordnet null");
        this.wn = wordnet;
    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException("nouns null");
        int maxD = 0;
        String ans = "";
        for (String s : nouns) {
            if (s == null) throw new IllegalArgumentException("noun null");
            int tmp = 0;
            for (String c : nouns) {
                tmp += this.wn.distance(s, c);
            }
            if (tmp > maxD) {
                maxD = tmp;
                ans = s;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        StdOut.println(args[0] + " " + args[1]);
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }  // see test client below
}
