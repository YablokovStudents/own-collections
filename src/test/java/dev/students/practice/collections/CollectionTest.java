package dev.students.practice.collections;

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
                // ToDo: экземпляры реализаций интерфейса Collection
        };
    }

    @Test(dataProvider = "collections")
    public void isEmpty(Collection<Integer> collection) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "collections")
    public void size(Collection<Integer> collection) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "collections")
    public void contains(Collection<Integer> collection) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "collections")
    public void add(Collection<Integer> collection) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "collections")
    public void remove(Collection<Integer> collection) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "collections")
    public void clear(Collection<String> collection) {
        // ToDo: написать тест
    }
}