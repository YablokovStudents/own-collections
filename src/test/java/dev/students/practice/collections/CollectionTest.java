package dev.students.practice.collections;

import dev.students.practice.collections.impl.ArrayList;
import dev.students.practice.collections.impl.LinkedList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link Collection}.
 */
public class CollectionTest {
    @DataProvider(name = "collections")
    public Object[][] getCollections() {
        return new Object[][]{
                {new ArrayList<Integer>()},
                {new LinkedList<Integer>()}
        };
    }

    @Test(dataProvider = "collections")
    public void isEmpty(Collection<Integer> collection) {
        assertTrue(collection.isEmpty());
        collection.add(1);
        assertFalse(collection.isEmpty());
    }

    @Test(dataProvider = "collections")
    public void size(Collection<Integer> collection) {
        assertEquals(collection.size(), 0);
        collection.add(1);
        assertEquals(collection.size(), 1);
        collection.add(2);
        collection.add(3);
        collection.add(4);
        assertEquals(collection.size(), 4);
        collection.add(null);
        collection.add(null);
        assertEquals(collection.size(), 6);
    }

    @Test(dataProvider = "collections")
    public void contains(Collection<Integer> collection) {
        assertFalse(collection.contains(1));
        collection.add(1);
        collection.add(2);
        collection.add(4);
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(4));
        assertFalse(collection.contains(3));
    }

    @Test(dataProvider = "collections")
    public void add(Collection<Integer> collection) {
        assertFalse(collection.contains(1));
        assertTrue(collection.add(1));
        assertTrue(collection.contains(1));
        assertTrue(collection.add(null));
        assertTrue(collection.contains(null));
    }

    @Test(dataProvider = "collections")
    public void remove(Collection<Integer> collection) {
        assertFalse(collection.remove(1));
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);
        collection.add(5);
        assertTrue(collection.remove(1));
        assertFalse(collection.contains(1));
        assertEquals(collection.size(), 4);
        assertTrue(collection.remove(3));
        assertFalse(collection.contains(3));
        assertEquals(collection.size(), 3);
        collection.add(null);
        assertTrue(collection.remove(null));
    }

    @Test(dataProvider = "collections")
    public void clear(Collection<String> collection) {
        collection.clear();
        collection.add("value1");
        collection.add("value2");
        collection.add("value3");
        collection.add("value3");
        collection.add("value4");
        collection.add(null);
        collection.clear();
        assertTrue(collection.isEmpty());
    }
}