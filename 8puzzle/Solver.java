/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

public class Solver {
    private boolean solved;
    private int ms;
    private LinkedStack<Board> steps;

    private class SearchNode {
        public SearchNode prevNode;
        private int moveNo;
        public Board board;
        private int priority;

        public SearchNode(SearchNode p, Board b) {
            if (p == null) this.moveNo = 0;
            else this.moveNo = p.moveNo + 1;
            this.prevNode = p;
            this.board = b;
            this.priority = this.board.manhattan() + this.moveNo;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode that, SearchNode other) {
            return that.priority - other.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.solved = false;
        this.steps = new LinkedStack<Board>();
        MinPQ<SearchNode> pq1 = new MinPQ<SearchNode>(new SearchNodeComparator());
        MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>(new SearchNodeComparator());

        SearchNode sn = new SearchNode(null, initial);
        pq1.insert(sn);
        pq2.insert(new SearchNode(null, initial.twin()));

        while (true) {
            SearchNode curr1 = pq1.delMin();
            SearchNode curr2 = pq2.delMin();
            if (curr1.board.isGoal()) {
                this.solved = true;
                this.ms = curr1.moveNo;
                do {
                    this.steps.push(curr1.board);
                    curr1 = curr1.prevNode;
                } while (curr1 != null && curr1.prevNode != null);
                if (curr1 != null) this.steps.push(curr1.board);
                break;
            }
            if (curr2.board.isGoal()) {
                this.solved = false;
                this.ms = -1;
                break;
            }
            for (Board i : curr1.board.neighbors())
                if (curr1.prevNode == null || !i.equals(curr1.prevNode.board)) {
                    pq1.insert(new SearchNode(curr1, i));
                }

            for (Board i : curr2.board.neighbors())
                if (curr2.prevNode == null || !i.equals(curr2.prevNode.board))
                    pq2.insert(new SearchNode(curr2, i));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.ms;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return this.steps;
    }

    public static void main(String[] args) {

    }
}
