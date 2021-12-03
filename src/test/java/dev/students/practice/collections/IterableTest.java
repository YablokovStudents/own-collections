package dev.students.practice.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link Iterable}.
 */
public class IterableTest {
    @DataProvider(name = "emptyIterables")
    public Object[][] getEmptyIterables() {
        return new Object[][]{
                // ToDo: экземпляры реализаций интерфейса Iterable
        };
    }

    @DataProvider(name = "notEmptyIterables")
    public Object[][] getNotEmptyIterables() {
        return getNotEmptyLists();
    }

    @DataProvider(name = "notEmptyLists")
    public Object[][] getNotEmptyLists() {
        return new Object[][]{
                // ToDo: экземпляры реализаций интерфейса Iterable
        };
    }

    @Test(dataProvider = "emptyIterables", expectedExceptions = NoSuchElementException.class)
    public void iterator_emptyCase(Iterable<Integer> iterable) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "notEmptyIterables")
    public void iterator_notEmptyCase(Iterable<Integer> iterable) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "notEmptyLists", expectedExceptions = ConcurrentModificationException.class)
    public void iterator_concurrentModificationCase(List<Integer> list) {
        // ToDo: написать тест
    }
}