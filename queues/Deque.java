/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // construct an empty deque
    private Node firstItem;
    private Node lastItem;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;

        public Node(Item i, Node n, Node p) {
            this.item = i;
            this.next = n;
            this.prev = p;
        }
    }

    public Deque() {
        this.firstItem = null;
        this.lastItem = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (this.isEmpty()) {
            Node newFirst = new Node(item, null, null);
            this.firstItem = newFirst;
            this.lastItem = newFirst;
        }
        else {
            Node newFirst = new Node(item, this.firstItem, null);
            this.firstItem.prev = newFirst;
            this.firstItem = newFirst;
        }
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newLast = new Node(item, null, this.lastItem);
        if (this.isEmpty()) {
            this.firstItem = new Node(item, null, null);
            this.lastItem = new Node(item, null, null);
        }
        else {
            this.lastItem.next = newLast;
            this.lastItem = newLast;
        }
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node oldFirst = this.firstItem;
        this.firstItem = oldFirst.next;
        Item oldFirstItem = oldFirst.item;
        oldFirst = null;
        this.size--;
        if (this.size == 0) this.lastItem = null;
        return oldFirstItem;

    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node oldLast = this.lastItem;
        this.lastItem = oldLast.prev;
        Item oldLastItem = oldLast.item;
        oldLast = null;
        if (this.lastItem != null) {
            this.lastItem.next = null;
        }
        size--;
        if (size == 0) this.firstItem = null;
        return oldLastItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            this.current = firstItem;
        }


        public boolean hasNext() {
            return this.current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Item oldCurr = this.current.item;
            this.current = this.current.next;
            return oldCurr;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        d.addFirst(1);
        d.addLast(2);
        StdOut.println(d.removeFirst());
        StdOut.println("size: " + d.size());
        for (Integer i : d) {
            StdOut.println("in for lootp");
            StdOut.println(i);
        }
        StdOut.println("remoe last " + d.removeLast());
        for (Integer i : d) {
            StdOut.println("in for lootp");
            StdOut.println(i);
        }
    }
}
