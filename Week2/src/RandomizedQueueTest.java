import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by homogeo on 30.01.2017.
 */
public class RandomizedQueueTest {

    @Test
    public void isEmpty_OnEmptyQueue() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        assertEquals(true, q.isEmpty());
    }

    @Test
    public void isEmpty_OneItemQueue() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.enqueue("one");
        assertEquals(false, q.isEmpty());
    }

    @Test
    public void isEmpty_ThousandItemsQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }

        assertEquals(false, q.isEmpty());
    }

    @Test
    public void size_OnEmptyQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        assertEquals(0, q.size());
    }

    @Test
    public void size_OneItemQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(new Integer(10));

        assertEquals(1, q.size());
    }

    @Test
    public void size_ThousandItemsQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }
        assertEquals(1000, q.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void sample_OnEmptyQueueShouldThrow() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.sample();
    }

    @Test
    public void sample_OneItemQueue() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.enqueue("one");
        assertEquals("one", q.sample());
    }

    @Test
    public void sample_TenItemQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            q.enqueue(i + 1);
        }

        for (int i = 0; i < 5; i++) {
            assertNotNull(q.sample());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void deque_OnEmptyQueueShouldThrow() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.dequeue();
    }

    @Test
    public void deque_OneItemQueue() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.enqueue("one");

        assertEquals("one", q.dequeue());
        assertEquals(0, q.size());
        assertEquals(true, q.isEmpty());
    }

    @Test
    public void deque_TenItemsQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
        }

        assertNotNull(q.dequeue());
        assertEquals(9, q.size());
        assertEquals(false, q.isEmpty());
    }

    @Test
    public void deque_multipleTimesTenItemsQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
        }

        assertNotNull(q.dequeue());
        assertEquals(9, q.size());
        assertFalse(q.isEmpty());

        assertNotNull(q.dequeue());
        assertEquals(8, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 7; i++) {
            assertNotNull(q.dequeue());
            assertFalse(q.isEmpty());
        }

        assertNotNull(q.dequeue());
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
    }

    @Test
    public void deque_alternatingWithEnque() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 6; i++) {
            q.enqueue(i);
        }
        assertEquals(6, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 5; i++) {
            assertNotNull(q.dequeue());
        }
        assertEquals(1, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }
        assertEquals(1001, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 1001; i++) {
            assertNotNull(q.dequeue());
        }
        assertEquals(0, q.size());
        assertTrue(q.isEmpty());

        q.enqueue(42);
        assertEquals(1, q.size());
        assertFalse(q.isEmpty());
    }

    @Test
    public void deque_alternatingWithSample() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }
        assertEquals(1000, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 10; i++) {
            assertNotNull(q.sample());
        }
        assertEquals(1000, q.size());
        assertFalse(q.isEmpty());

        for (int i = 0; i < 10; i++) {
            assertNotNull(q.dequeue());
            assertNotNull(q.sample());
        }
        assertEquals(990, q.size());
        assertFalse(q.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iterator_remove_shouldThrow() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(42);
        q.iterator().remove();
    }

    @Test
    public void iterator_hasNext_onEmptyQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        assertFalse(q.iterator().hasNext());
    }

    @Test
    public void iterator_hasNext_onOneItemQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(42);

        assertTrue(q.iterator().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void interator_next_onEmptyQueueShouldThrow() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.iterator().next();
    }

    @Test
    public void iterator_next_onOneItemQueue() {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        q.enqueue("one");

        Iterator<String> iterator = q.iterator();

        assertEquals("one", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_next_onThousandItemsQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }

        Iterator<Integer> iterator = q.iterator();
        for (int i = 0; i < 999; i++) {
            assertNotNull(iterator.next());
        }
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_next_twoNestedIteratorsOverSameQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }

        int count = 0;

        for (Integer i : q) {
            for (Integer j : q) {
                if (i == j) count++;
            }
        }
        assertEquals(1000, count);
    }

    @Test
    public void iterator_next_twoSequentialIteratorsOverSameQueue() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.enqueue(i);
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for (int i : q) {
            sb1.append(i);
            sb1.append(";");
        }

        for (int i : q) {
            sb2.append(i);
            sb2.append(";");
        }

        assertFalse(sb1.toString().equals(sb2.toString()));
    }
}
