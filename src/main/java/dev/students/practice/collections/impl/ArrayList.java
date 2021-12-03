package dev.students.practice.collections.impl;

import dev.students.practice.collections.List;

import java.util.*;

public class ArrayList<T> implements List<T> {
    private T[] array;
    private int size;
    private int modificationCount;

    public ArrayList() {
        this(10);
    }

    public ArrayList(int capacity) {
        init(Math.max(capacity, 0));
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        array = (T[]) new Object[capacity];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            for (int i = 0; i < size; ++i) {
                if (array[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; ++i) {
                if (item.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(T item) {
        add(size, item);
        return true;
    }

    @Override
    public boolean remove(T item) {
        boolean removedAtLeastOne = false;

        if (item == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    shiftItemsToLeft(i--);
                    removedAtLeastOne = true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(array[i])) {
                    shiftItemsToLeft(i--);
                    removedAtLeastOne = true;
                }
            }
        }
        return removedAtLeastOne;
    }

    private void shiftItemsToLeft(int startIndex) {
        for (int i = startIndex; i < size; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null;
        modificationCount++;
    }

    @Override
    public void clear() {
        init(10);
        modificationCount++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, T item) {
        checkRange(index);
        extendArrayIfFull();
        shiftItemsToRight(index);
        array[index] = item;
    }

    private void shiftItemsToRight(int startIndex) {
        for (int i = size; i > startIndex; --i) {
            array[i] = array[i - 1];
        }
        size++;
        modificationCount++;
        array[startIndex] = null;
    }

    @Override
    public void set(int index, T item) {
        if (index == size) {
            add(item);
        } else {
            checkRange(index);
            array[index] = item;
            modificationCount++;
        }
    }

    private void checkRange(int index) {
        if ((index < 0) || (index > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void extendArrayIfFull() {
        if (array.length == size) {
            array = Arrays.copyOf(array, calculateExtendedArrayLength());
        }
    }

    private int calculateExtendedArrayLength() {
        if (array.length == Integer.MAX_VALUE) {
            throw new OutOfMemoryError();
        } else if (array.length >= (Integer.MAX_VALUE / 3) * 2 - 1) {
            return Integer.MAX_VALUE;
        } else {
            return (array.length / 2) * 3 + 1;
        }
    }

    @Override
    public T get(int index) {
        if ((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(Object item) {
        if (item == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(array[i])) {
                    return i;
                }
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public int lastIndexOf(T item) {
        if (item == null) {
            for (int i = size - 1; i > 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (item.equals(array[i])) {
                    return i;
                }
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public T remove(int index) {
        if ((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException();
        }
        T deletedItem = array[index];
        shiftItemsToLeft(index);
        return deletedItem;
    }

    @Override
    public List<T> subList(int from, int to) {
        if (from >= size || to > size || from > to) {
            throw new IndexOutOfBoundsException();
        }

        ArrayList<T> arrayList = new ArrayList<>(to - from);
        arrayList.array = Arrays.copyOfRange(array, from, to);
        arrayList.size = to - from;
        return arrayList;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    private class IteratorImpl implements Iterator<T> {
        private final int modificationCount = ArrayList.this.modificationCount;
        private int position;

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public T next() {
            if (modificationCount != ArrayList.this.modificationCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return array[position++];
        }
    }
}

