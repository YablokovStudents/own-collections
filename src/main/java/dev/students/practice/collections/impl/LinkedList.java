package dev.students.practice.collections.impl;

import dev.students.practice.collections.Deque;
import dev.students.practice.collections.List;

import java.util.*;

public class LinkedList<T> implements List<T>, Deque<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;
    private int modificationCount;

    private class IteratorImpl implements Iterator<T> {
        private final int modificationCount = LinkedList.this.modificationCount;
        private Node<T> node = first;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            if (modificationCount != LinkedList.this.modificationCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T result = node.item;
            node = node.next;
            return result;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    private static class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;

        public Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public void addFirst(T item) {
        add(0, item);
    }

    @Override
    public void addLast(T item) {
        add(size, item);
    }

    @Override
    public T getFirst() {
        checkRangeFotGet(0);
        return first.item;
    }

    @Override
    public T getLast() {
        checkRangeFotGet(size - 1);
        return last.item;
    }

    private void checkRangeFotGet(int index) {
        if ((index < 0) || (index >= size)) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T pollFirst() {
        if (first == null) {
            return null;
        }
        return deleteNode(first);
    }

    @Override
    public T pollLast() {
        if (last == null) {
            return null;
        }
        return deleteNode(last);
    }

    @Override
    public T removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return deleteNode(first);
    }

    @Override
    public T removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return deleteNode(last);
    }

    private T deleteNode(Node<T> deletingNode) {
        T deletedItem = deletingNode.item;

        if (deletingNode.prev == null) {
            first = deletingNode.next;
        } else {
            deletingNode.prev.next = deletingNode.next;
        }

        if (deletingNode.next == null) {
            last = deletingNode.prev;
        } else {
            deletingNode.next.prev = deletingNode.prev;
        }

        deletingNode.prev = null;
        deletingNode.next = null;

        size--;
        modificationCount++;

        return deletedItem;
    }

    @Override
    public void add(int index, T item) {
        checkRangeForAdd(index);
        if (index == size) {
            addLastItem(item);
        } else {
            addNotLastItem(index, item);
        }
    }

    private void checkRangeForAdd(int index) {
        if ((index < 0) || (index > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void addLastItem(T item) {
        Node<T> addingNode = new Node<>(item, last, null);
        if (addingNode.prev == null) {
            first = addingNode;
        } else {
            addingNode.prev.next = addingNode;
        }
        last = addingNode;

        size++;
        modificationCount++;
    }

    private void addNotLastItem(int index, T item) {
        Node<T> nextNode = getNode(index);
        Node<T> prevNode = nextNode.prev;

        Node<T> addingNode = new Node<>(item, prevNode, nextNode);

        if (addingNode.prev == null) {
            first = addingNode;
        } else {
            addingNode.prev.next = addingNode;
        }

        if (addingNode.next == null) {
            last = addingNode;
        } else {
            addingNode.next.prev = addingNode;
        }

        size++;
        modificationCount++;
    }

    @Override
    public void set(int index, T item) {
        if (index == size) {
            addLast(item);
        } else {
            checkRangeFotGet(index);
            Node<T> currentNode = getNode(index);
            currentNode.item = item;
            modificationCount++;
        }
    }

    private Node<T> getNode(int index) {
        Node<T> currentNode;
        if (index < (size >>> 1)) { // size >>> 1 == size / 2
            currentNode = getNodeFromLeft(index);
        } else {
            currentNode = getNodeFromRight(index);
        }
        return currentNode;
    }

    private Node<T> getNodeFromLeft(int index) {
        Node<T> result = first;
        for (int i = 0; i < index; i++) {
            result = result.next;
        }
        return result;
    }

    private Node<T> getNodeFromRight(int index) {
        Node<T> result = last;
        for (int i = size - 1; i > index; i--) {
            result = result.prev;
        }
        return result;
    }

    @Override
    public T get(int index) {
        checkRangeFotGet(index);
        return getNode(index).item;
    }

    @Override
    public int indexOf(T item) {
        Node<T> currentNode = first;
        if (item == null) {
            for (int index = 0; index < size; index++) {
                if (currentNode.item == null) {
                    return index;
                }
                currentNode = currentNode.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(currentNode.item)) {
                    return i;
                }
                currentNode = currentNode.next;
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public int lastIndexOf(T item) {
        Node<T> currentNode = last;
        if (item == null) {
            for (int index = size - 1; index >= 0; index--) {
                if (currentNode.item == null) {
                    return index;
                }
                currentNode = currentNode.prev;
            }
        } else {
            for (int index = size - 1; index >= 0; index--) {
                if (item.equals(currentNode.item)) {
                    return index;
                }
                currentNode = currentNode.prev;
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public T remove(int index) {
        checkRangeFotGet(index);
        return deleteNode(getNode(index));
    }

    @Override
    public List<T> subList(int from, int to) {
        checkRangeForSubList(from, to);

        List<T> result = new LinkedList<>();
        Node<T> currentNode = getNode(from);
        for (int index = 0; index < (to - from); index++) {
            result.add(currentNode.item);
            currentNode = currentNode.next;
        }
        return result;
    }

    private void checkRangeForSubList(int from, int to) {
        if ((from < 0) || (from >= size) || (to < 0) || (to > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            for (Node<T> currentNode = first; currentNode != null; currentNode = currentNode.next) {
                if (currentNode.item == null) {
                    return true;
                }
            }
        } else {
            for (Node<T> currentNode = first; currentNode != null; currentNode = currentNode.next) {
                if (item.equals(currentNode.item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(T item) {
        addLast(item);
        return true;
    }

    @Override
    public boolean remove(T item) {
        boolean deletedAtLeastOne = false;
        Node<T> currentNode = first;
        if (item == null) {
            while (currentNode != null) {
                Node<T> nextNode = currentNode.next;
                if (currentNode.item == null) {
                    deleteNode(currentNode);
                    deletedAtLeastOne = true;
                }
                currentNode = nextNode;
            }
        } else {
            while (currentNode != null) {
                Node<T> nextNode = currentNode.next;
                if (item.equals(currentNode.item)) {
                    deleteNode(currentNode);
                    deletedAtLeastOne = true;
                }
                currentNode = nextNode;
            }
        }
        return deletedAtLeastOne;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
        modificationCount++;
    }
}
