/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    // constructor takes the name of the two input files
    private Digraph wordnet;
    private ST<Integer, SET<String>> id_noun;
    private ST<String, SET<Integer>> noun_id;
    private SAP s;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("constructor null");
        this.id_noun = new ST<Integer, SET<String>>();
        this.noun_id = new ST<String, SET<Integer>>();
        In synIn = new In(synsets);
        In hypIn = new In(hypernyms);
        while (synIn.hasNextLine()) {
            String[] str = synIn.readLine().split(",");
            int id = Integer.parseInt(str[0]);
            SET<String> nouns = new SET<String>();
            for (String s : str[1].split(" ")) {
                nouns.add(s);
                if (!this.noun_id.contains(s)) {
                    SET<Integer> inside = new SET<Integer>();
                    inside.add(id);
                    this.noun_id.put(s, inside);
                }
                else {
                    SET<Integer> inside = this.noun_id.get(s);
                    inside.add(id);
                }
            }
            id_noun.put(id, nouns);
        }

        this.wordnet = new Digraph(this.id_noun.size());
        while (hypIn.hasNextLine()) {
            int i = 0;
            int key = 0;
            for (String s : hypIn.readLine().split(",")) {
                int curr = Integer.parseInt(s);
                if (i == 0) key = curr;
                else this.wordnet.addEdge(key, curr);
                i++;
            }
        }
        this.checkRootedDAG();
        this.s = new SAP(this.wordnet);
    }

    private void checkRootedDAG() {
        DirectedCycle dc = new DirectedCycle(this.wordnet);
        if (dc.hasCycle()) throw new IllegalArgumentException("has cycle");
        int noOfRoot = 0;
        for (int i = 0; i < this.wordnet.V(); i++) {
            if (this.wordnet.indegree(i) > 0 && this.wordnet.outdegree(i) == 0) noOfRoot++;
            else if (this.wordnet.outdegree(i) == 0)
                throw new IllegalArgumentException("ancestor of its own");
            if (noOfRoot > 1) throw new IllegalArgumentException("more than one root");
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.noun_id.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("word is illegal");
        return this.noun_id.contains(word);
    }

    private void checkWord(String word) {
        if (word == null || !this.noun_id.contains(word))
            throw new IllegalArgumentException("word is illegal");
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        this.checkWord(nounA);
        this.checkWord(nounB);
        int ancestor = this.s.ancestor(this.noun_id.get(nounA), this.noun_id.get(nounB));
        int dist = Integer.MAX_VALUE;
        for (int id : this.noun_id.get(nounA)) {

            int currD = this.s.length(id, ancestor);
            if (currD != -1 && currD < dist) dist = currD;
        }
        dist = Integer.MAX_VALUE;
        for (int id : this.noun_id.get(nounB)) {
            int currD = this.s.length(id, ancestor);
            if (currD != -1 && currD < dist) dist = currD;
        }
        return this.s.length(this.noun_id.get(nounA), this.noun_id.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        this.checkWord(nounA);
        this.checkWord(nounB);
        String ans = "";
        int i = 0;
        SET<String> s = this.id_noun
                .get(this.s.ancestor(this.noun_id.get(nounA), this.noun_id.get(nounB)));
        for (String w : s) {
            if (i == s.size() - 1) ans += w;
            else ans += w + " ";
            i++;
        }
        return ans;
    }

    public static void main(String[] args) {
    }
}
