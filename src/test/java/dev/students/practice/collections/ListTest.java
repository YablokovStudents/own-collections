package dev.students.practice.collections;

import dev.students.practice.collections.impl.ArrayList;
import dev.students.practice.collections.impl.LinkedList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link List}.
 */
public class ListTest {
    @DataProvider(name = "lists")
    public Object[][] getLists() {
        return new Object[][]{
                {new ArrayList<Integer>()},
                {new LinkedList<Integer>()}
        };
    }

    @Test(dataProvider = "lists")
    public void add(List<Integer> list) {
        list.add(0, null);
        assertEquals(list.get(0), null);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1, 5);
        assertEquals(list.get(1).intValue(), 5);
    }

    @Test(dataProvider = "lists")
    public void set(List<Integer> list) {
        list.set(0, 1);
        assertEquals(list.get(0).intValue(), 1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.set(1, null);
        list.set(5, 7);
        assertNull(list.get(1));
        assertEquals(list.get(5).intValue(), 7);
    }

    @Test(dataProvider = "lists")
    public void subList(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        List<Integer> subList = list.subList(0, 5);
        assertEquals(subList.size(), 5);
    }

    @Test(dataProvider = "lists")
    public void indexOf(List<Integer> list) {
        assertEquals(list.indexOf(null), List.INDEX_NOT_FOUND);
        list.add(1);
        list.add(2);
        list.add(null);
        list.add(3);
        list.add(4);
        assertEquals(list.indexOf(1), 0);
        assertEquals(list.indexOf(2), 1);
        assertEquals(list.indexOf(null), 2);
        assertEquals(list.indexOf(3), 3);
    }

    @Test(dataProvider = "lists")
    public void lastIndexOf(List<Integer> list) {
        assertEquals(list.lastIndexOf(1), List.INDEX_NOT_FOUND);
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(null);
        list.add(4);
        assertEquals(list.lastIndexOf(1), 0);
        assertEquals(list.lastIndexOf(2), 1);
        assertEquals(list.lastIndexOf(null), 3);
        assertEquals(list.lastIndexOf(4), 4);
    }

    @Test(dataProvider = "lists")
    public void remove(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(null);
        list.add(4);
        list.remove(3);
        assertEquals(list.size(), 4);
    }
}
