/******************************************************************************
 * @author George Homorozeanu
 * created on 2017/27/01
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Implements a randomized queue.
 *
 * A randomized queue is similar to a queue, except that the item
 * removed is chosen uniformly at random from items in the data
 * structure.
 *
 * The iterator for this queue also returns items in random order.
 * Moreover, each iterator has its own random oder of returning items.
 *
 * <p>
 * Note: The iterator over this queue does not support the remove
 * functionality.
 * </p>
 *
 * @param <Item> Generic parameter, substitute with concrete type.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    //---- First empty position in the internal array
    private int tail = -1;
    //---- The size of the queue
    private int size = 0;
    //---- The internal representation used for the queue
    private Item[] q;

    /**
     * Implements an iterator over the {@code RandomizedQueue<Item>}.
     * Each instance of this iterator return items in its own random oder.
     */
    private class RandomizedQueueIterator implements Iterator<Item> {
        //---- Structure used to mark already visited indexes
        private final boolean[] visitedIndexes;
        //---- Counts the visited indexes
        private int counter;

        /**
         * Create an instance of the {@code RandomizedQueueIterator} that
         * returns items in random order.
         */
        public RandomizedQueueIterator() {
            visitedIndexes = new boolean[size];
            counter = 0;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return counter < visitedIndexes.length;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            counter++;

            int index = getRandomIndex();
            while (visitedIndexes[index]) index = getRandomIndex();
            visitedIndexes[index] = true;
            return q[index];
        }

        /**
         * This method is not supported.
         *
         * @throws UnsupportedOperationException if called.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Create an empty {@code RandomizedQueue<Item>}.
     */
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        tail = 0;
    }

    /**
     * Is the queue empty?
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Return the number of items on the queue.
     *
     * @return Number of items.
     */
    public int size() {
        return size;
    }

    /**
     * Adds and item into the queue.
     *
     * Adding null items is not supported and results in throwing a
     * {@code NullPointerException}.
     *
     * @param item The item to be added into the queue.
     * @throws NullPointerException if the item to be added is null.
     */
    public void enqueue(Item item) {
        validate(item);

        if (size == q.length) resize(2 * q.length);
        q[tail++] = item;
        size++;
    }

    /**
     * Return and remove a random item from the queue.
     *
     * @return The random removed item.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Item dequeue() {
        throwOnEmpty();

        int index = getRandomIndex();
        Item item = q[index];

        //---- Move last element into the index of the removed element
        tail--;
        if (tail > 0 && tail != index && q[tail] != null) {
            q[index] = q[tail];
            q[tail] = null;
        }
        else if (tail > 0 && tail == index) {
            q[tail] = null;
        }

        //---- Resize array if needed
        if (size > 0 && size == q.length / 4) {
            resize(q.length / 4);
        }

        size--;
        return item;
    }

    /**
     * Return a random item from the queue. This method does not
     * remove the element.
     *
     * @return A random item from the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Item sample() {
        throwOnEmpty();

        int index = getRandomIndex();
        return q[index];
    }

    /**
     * Return an independent iterator over items in random order.
     *
     * @return An iterator over the {@code RadomizedQueue<Item>}
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /**
     * Checks if the item is null and throws {@code NullPointerException}.
     *
     * @param item Item checked for null value.
     */
    private void validate(Item item) {
        if (item == null) throw new NullPointerException(
                "Null values are not allowed as items!");
    }

    /**
     * Check if the queue is empty and throw
     * {@code NoSuchElementException}.
     */
    private void throwOnEmpty() {
        if (isEmpty()) throw new NoSuchElementException(
                "Cannot remove from an empty collection");
    }

    /**
     * Resize the internal (array) representation to the given
     * capacity.
     *
     * @param capacity The new capacity for the unterlying (array)
     *                 queue representation.
     */
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < q.length; i++) {
            //---- copy only if there is a valid value at position i
            if (q[i] != null) copy[i] = q[i];
        }
        q = copy;
    }

    /**
     * Returns a random index in the array.
     *
     * @return A random index in the array.
     */
    private int getRandomIndex() {
        return StdRandom.uniform(0, tail);
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        // unit testing (optional)
    }
}
