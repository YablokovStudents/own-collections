package dev.students.practice.collections;

import dev.students.practice.collections.impl.LinkedList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link Deque}.
 */
public class DequeTest {
    @DataProvider(name = "deques")
    public Object[][] getDeques() {
        return new Object[][]{
                {new LinkedList<Integer>()}
        };
    }

    @Test(dataProvider = "deques")
    public void addFirst(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        deque.addFirst(5);
        assertEquals(deque.getFirst().intValue(), 5);
    }

    @Test(dataProvider = "deques")
    public void addLast(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        deque.addLast(5);
        assertEquals(deque.getLast().intValue(), 5);
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getFirst(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.getFirst().intValue(), 1);

        deque.clear();
        deque.getFirst();
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getLast(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.getLast().intValue(), 4);

        deque.clear();
        deque.getLast();
    }

    @Test(dataProvider = "deques")
    public void pollFirst(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.pollFirst().intValue(), 1);
        assertEquals(deque.pollFirst().intValue(), 2);
        assertEquals(deque.pollFirst().intValue(), 3);
        assertEquals(deque.pollFirst().intValue(), 4);
        assertNull(deque.pollFirst());
    }

    @Test(dataProvider = "deques")
    public void pollLast(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.pollLast().intValue(), 4);
        assertEquals(deque.pollLast().intValue(), 3);
        assertEquals(deque.pollLast().intValue(), 2);
        assertEquals(deque.pollLast().intValue(), 1);
        assertNull(deque.pollLast());
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeFirst(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.removeFirst().intValue(), 1);
        assertEquals(deque.removeFirst().intValue(), 2);
        assertEquals(deque.removeFirst().intValue(), 3);
        assertEquals(deque.removeFirst().intValue(), 4);
        deque.removeFirst();
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeLast(Deque<Integer> deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.removeLast().intValue(), 4);
        assertEquals(deque.removeLast().intValue(), 3);
        assertEquals(deque.removeLast().intValue(), 2);
        assertEquals(deque.removeLast().intValue(), 1);
        deque.removeLast();
    }
}