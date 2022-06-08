/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // construct an empty randomized queue
    private int size;
    private Item[] items;

    public RandomizedQueue() {
        this.size = 0;
        this.items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {//swap in enqueue
        if (item == null) throw new IllegalArgumentException();
        if (this.items.length < this.size + 1) {
            Item[] newItems = (Item[]) new Object[this.items.length * 2];
            for (int i = 0; i < this.items.length; i++) {
                newItems[i] = this.items[i];
            }
            newItems[this.size++] = item;
            this.items = newItems;
        }
        else {
            this.items[this.size++] = item;
        }

    }

    // remove and return a random item
    public Item dequeue() {
        if (this.size == 0) throw new NoSuchElementException();
        int ranNum = StdRandom.uniform(this.size);
        Item rtn = this.items[ranNum];
        if (ranNum == this.size - 1) {
            this.items[ranNum] = null;
        }
        else {
            this.items[ranNum] = this.items[this.size - 1];
            this.items[this.size - 1] = null;
        }
        if ((this.size - 1) <= this.items.length / 4) {
            Item[] newItems = (Item[]) new Object[(int) Math.ceil((double) this.items.length / 2)];
            for (int i = 0; i < this.size - 1; i++) newItems[i] = this.items[i];
            this.items = newItems;
        }
        this.size--;
        return rtn;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.size == 0) throw new NoSuchElementException();
        return this.items[StdRandom.uniform(this.size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] perm;
        private int index;

        public RandomIterator() {
            this.index = 0;
            this.perm = new int[size];
            this.perm = StdRandom.permutation(size);
        }

        public boolean hasNext() {
            return index < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException();
            return items[this.perm[this.index++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        StdOut.println(r.isEmpty());
        StdOut.println(r.size());
        r.enqueue(1);
        StdOut.println(r.sample());
        for (int i : r) StdOut.println(i);
        StdOut.println(r.dequeue());
    }
}
