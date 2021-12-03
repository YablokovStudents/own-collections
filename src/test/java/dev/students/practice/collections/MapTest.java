package dev.students.practice.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MapTest {
    @DataProvider(name = "maps")
    public Object[][] getMaps() {
        return new Object[][]{
                // ToDo: экземпляры реализаций интерфейса Map
        };
    }

    @Test(dataProvider = "maps")
    public void isEmpty(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void size(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void put(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void remove(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void get(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void containsKey(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }

    @Test(dataProvider = "maps")
    public void containsValue(Map<Integer, Integer> map) {
        // ToDo: написать тест
    }
}