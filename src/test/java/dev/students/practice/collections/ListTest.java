package dev.students.practice.collections;

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
                // ToDo: экземпляры реализаций интерфейса List
        };
    }

    @Test(dataProvider = "lists")
    public void add(List<Integer> list) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "lists")
    public void set(List<Integer> list) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "lists")
    public void subList(List<Integer> list) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "lists")
    public void indexOf(List<Integer> list) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "lists")
    public void lastIndexOf(List<Integer> list) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "lists")
    public void remove(List<Integer> list) {
        // ToDo: написать тест
    }
}
