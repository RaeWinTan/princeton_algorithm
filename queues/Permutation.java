/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        double i = 0;
        if (n == 0) return;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            i++;
            if (i > n) {
                rq.enqueue(s);
                rq.dequeue();

                /*
                if ((double) n / i <= StdRandom.uniform()) {
                    rq.dequeue();
                    rq.enqueue(s);
                }*/
            }
            else {
                rq.enqueue(s);
            }
        }
        for (i = 0; i < n; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
