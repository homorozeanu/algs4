/******************************************************************************
 * @author George Homorozeanu
 * created on 2017/27/01
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a double-ended queue that supports adding and removing items
 * from either the front or the back of the data structure.
 *
 * <p>
 * Note: The iterator over this deque does not support the remove
 * functionality.
 * </p>
 *
 * @param <Item> Generic data type; substitute with concrete type.
 */
public class Deque<Item> implements Iterable<Item> {
    // Pointer to the first item in the list.
    private Node first = null;
    // Pointer to the last item in the list.
    private Node last = null;
    // Stores the size of the list (number of items in the list).
    private int size = 0;

    /**
     * Represents an item (as node) in the internal list representation.
     */
    private class Node {
        // The value of this node (item).
        private Item item;
        // Pointer to the next node in the list; null if none.
        private Node next;
        // Pointer to the previous node in the list; null if none.
        private Node prev;
    }

    /**
     * Iterator implementation for the deque.
     * <p>
     * <p>
     * Note: The remove functionality is not supported.
     * </p>
     */
    private class DequeIterator implements Iterator<Item> {
        // Pointer to the current node (item) in the underlying list.
        private Node current = first;

        /**
         * Checks if there is a next item in the list.
         *
         * @return true if there is at least one more item in the list.
         */
        @Override
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * Returns the current item and moves the pointer to the next item.
         *
         * @return The current item in the list.
         * @throws {@code NoSuchElementException} if there are no more
         * items in the list.
         */
        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        /**
         * This method is not supported in the current implementation.
         *
         * @throws {@code UnsupportedOperationException}.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructs an empty deque.
     */
    public Deque() {
    }

    /**
     * Checks if the deque is empty.
     *
     * @return true the deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Get the size of the deque.
     *
     * @return The number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * Add an item to the front.
     *
     * @param item The item to be added to the front of the deque.
     */
    public void addFirst(Item item) {
        validate(item);

        Node add = new Node();
        add.item = item;

        if (isEmpty()) {
            last = add;
            first = add;
        }
        else {
            first.prev = add;
            add.next = first;
            first = add;
        }
        size++;
    }

    /**
     * Add an item to the end.
     *
     * @param item The item to be added to the end of the deque.
     */
    public void addLast(Item item) {
        validate(item);

        Node add = new Node();
        add.item = item;

        if (isEmpty()) {
            first = add;
            last = add;
        }
        else {
            add.prev = last;
            last.next = add;
            last = add;
        }
        size++;
    }

    /**
     * Remove and return the item from the front.
     *
     * @return The item found at the front of the deque.
     */
    public Item removeFirst() {
        throwOnEmpty();

        Item result = first.item;

        if (first == last) { // aka. size = 1 -> one item
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev.next = null;
            first.prev = null;
        }

        size--;
        return result;
    }

    /**
     * Remove and return the item a the end of the deque.
     *
     * @return The item found at the end of the deque.
     */
    public Item removeLast() {
        throwOnEmpty();

        Item result = last.item;

        if (first == last) { // aka. size = 1 -> one item
            first = null;
            last = null;
        }
        else {
            last.prev.next = null;
            last = last.prev;
        }

        size--;
        return result;
    }


    /**
     * Return an iterator over items in order from front to end.
     *
     * @return An iterator over the deque items.
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
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
     * Check if deque is empty and throw {@code NoSuchElementException}.
     */
    private void throwOnEmpty() {
        if (isEmpty()) throw new NoSuchElementException(
                "Cannot remove from an empty collection");
    }

    /**
     * Main method. Not used in this implementation.
     *
     * @param args
     */
    public static void main(String[] args) {

    }

}
