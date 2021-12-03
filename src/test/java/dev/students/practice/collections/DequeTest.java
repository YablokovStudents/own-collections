package dev.students.practice.collections;

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
                // ToDo: экземпляры реализаций интерфейса Deque
        };
    }

    @Test(dataProvider = "deques")
    public void addFirst(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques")
    public void addLast(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getFirst(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getLast(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques")
    public void pollFirst(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques")
    public void pollLast(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeFirst(Deque<Integer> deque) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeLast(Deque<Integer> deque) {
        // ToDo: написать тест
    }
}