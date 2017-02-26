import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by homogeo on 28.01.2017.
 */
public class DequeTest {
    @Test
    public void isEmpty_OnEmptyCollection() throws Exception {
        Deque<String> dq = new Deque<>();
        assertEquals(true, dq.isEmpty());
    }

    @Test
    public void isEmpty_CollectionWithOneItem() {
        Deque<String> dq = new Deque<>();
        dq.addFirst("one");
        assertEquals(false, dq.isEmpty());
    }

    @Test
    public void size_OnEmptyCollection() {
        Deque<String> dq = new Deque<>();
        assertEquals(0, dq.size());
    }

    @Test
    public void size_CollectionWithOneItem() {
        Deque<String> dq = new Deque<>();
        dq.addFirst("one");
        assertEquals(1, dq.size());
    }

    @Test
    public void size_CollectionWithTwoItems() {
        Deque<String> dq = new Deque<>();
        dq.addFirst("one");
        dq.addFirst("two");
        assertEquals(2, dq.size());
    }

    @Test
    public void size_CollectionWith101Items() {
        Deque<String> dq = new Deque<>();
        for (int i = 0; i < 101; i++) {
            dq.addFirst("item" + i);
        }
        assertEquals(101, dq.size());
    }

    @Test(expected = NullPointerException.class)
    public void addFirst_NullItemShouldThrow() {
        Deque<String> dq = new Deque<>();
        dq.addFirst(null);
    }

    @Test
    public void addFirst_addTenElements() {
        Deque<Integer> dq = new Deque<>();
        for (int i = 1; i <= 10; i++) {
            dq.addFirst(i);
        }
        assertEquals(false, dq.isEmpty());
        assertEquals(10, dq.size());

        for (int i = 10; i >= 1; i--) {
            assertEquals(i, (int) dq.removeFirst());
        }

        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeFirst_FromEmptyCollectionShouldThrow() {
        Deque<Integer> dq = new Deque<>();
        dq.removeFirst();
    }

    @Test
    public void removeFirst_CollectionWithOneItem() {
        Deque<Integer> dq = new Deque<>();
        dq.addFirst(42);
        assertEquals(42, (int) dq.removeFirst());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test
    public void addFirst_removeFirst_Alternating() {
        Deque<String> dq = new Deque<>();

        dq.addFirst("one");
        assertEquals(1, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals("one", dq.removeFirst());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());

        dq.addFirst("dog");
        dq.addFirst("cat");
        assertEquals(2, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals("cat", dq.removeFirst());
    }

    @Test(expected = NullPointerException.class)
    public void addLast_NullItemShouldThrow() {
        Deque<String> dq = new Deque<>();
        dq.addLast(null);
    }

    @Test
    public void addLast_AddOneItemToEmptyCollection() {
        Deque<String> dq = new Deque<>();
        dq.addLast("one");

        assertEquals(1, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals("one", dq.removeFirst());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test
    public void addLast_AddTenItemsToEmptyCollection() {
        Deque<Integer> dq = new Deque<>();
        for (int i = 1; i <= 10; i++) dq.addLast(i);

        assertEquals(10, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals(1, (int) dq.removeFirst());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeLast_OnEmptyCollectionShouldThrow() {
        Deque<String> dq = new Deque<>();
        String item = dq.removeLast();
    }

    @Test
    public void removeLast_CollectionWithOneItem() {
        Deque<String> dq = new Deque<>();
        dq.addLast("one");

        assertEquals("one", dq.removeLast());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test
    public void addLast_removeLast_Alternating() {
        Deque<String> dq = new Deque<>();
        dq.addLast("one");

        assertEquals(1, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals("one", dq.removeLast());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());

        dq.addLast("item 1");
        dq.addLast("item 2");
        dq.addLast("item 3");

        assertEquals(3, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals("item 3", dq.removeLast());
        assertEquals("item 2", dq.removeLast());
        assertEquals("item 1", dq.removeLast());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test
    public void addFirst_addLast_removeFirst_removeLast_Alternating() {
        Deque<Integer> dq = new Deque<>();
        for (int i = 1, j = 10; i <= 5; i++, j--) {
            dq.addFirst(i);
            dq.addLast(j);
        }

        assertEquals(10, dq.size());
        assertEquals(false, dq.isEmpty());

        assertEquals(5, (int) dq.removeFirst());
        assertEquals(4, (int) dq.removeFirst());
        assertEquals(8, dq.size());

        assertEquals(6, (int) dq.removeLast());
        assertEquals(7, (int) dq.removeLast());
        assertEquals(6, dq.size());

        assertEquals(3, (int) dq.removeFirst());
        assertEquals(8, (int) dq.removeLast());
        assertEquals(9, (int) dq.removeLast());
        assertEquals(2, (int) dq.removeFirst());
        assertEquals(10, (int) dq.removeLast());
        assertEquals(1, (int) dq.removeFirst());
        assertEquals(0, dq.size());
        assertEquals(true, dq.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iterator_remove_ShouldThrow() {
        Deque<String> dq = new Deque<>();
        dq.iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_next_OnEmptyCollectionShouldTrow() {
        Deque<String> dq = new Deque<>();
        dq.iterator().next();
    }

    @Test
    public void iterator_hasNext_OnEmptyCollection() {
        Deque<String> dq = new Deque<>();
        assertEquals(false, dq.iterator().hasNext());
    }

    @Test
    public void iterator_hasNext_CollectionWithOneItem() {
        Deque<String> dq = new Deque<>();
        dq.addFirst("one");

        Iterator<String> iterator = dq.iterator();

        assertEquals(true, iterator.hasNext());
        assertEquals("one", iterator.next());
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void iterator_next_hasNext_CollectionWithTenItems() {
        Deque<Integer> dq = new Deque<>();
        Integer expected = new Integer(55);
        Integer sum = new Integer(0);

        for (int i = 1; i <= 10; i++) {
            dq.addFirst(i);
        }

        Iterator<Integer> iterator = dq.iterator();
        while (iterator.hasNext()) sum += iterator.next();

        assertEquals(expected, sum);
    }

    @Test
    public void iterator_foreach_OnEmptyCollection() {
        Deque<String> dq = new Deque<>();
        String actual = "";

        for (String s : dq) {
            actual += s;
        }

        assertEquals("", actual);
    }

    @Test
    public void iterator_foreach_CollectionWithOneItem() {
        Deque<String> dq = new Deque<>();
        dq.addLast("one");
        String actual = "";

        for (String s : dq) {
            actual += s;
        }

        assertEquals("one", actual);
    }

    @Test
    public void iterator_twoNestedIteratorsOnSameDeque() {
        Deque<Integer> dq = new Deque<>();
        Integer sum1 = new Integer(0);
        Integer sum2 = new Integer(0);

        for (int i = 1; i <= 10; i++) {
            dq.addFirst(i);
        }

        for (Integer v1 : dq) {
            for (Integer v2 : dq) {
                sum2 += v2;
            }
            sum1 += v1;
        }

        assertEquals(55, (int) sum1);
        assertEquals(550, (int) sum2);
    }

    @Test
    public void iterator_afterCallingOnlyToAddFirst() {
        Deque<Integer> dq = new Deque<>();
        int count = 0;

        for (int i = 0; i < 1000; i++) {
            dq.addFirst(i);
        }

        for (Integer i : dq) {
            count++;
        }

        assertEquals(1000, count);
    }
}