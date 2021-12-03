package dev.students.practice.collections.impl;

import dev.students.practice.collections.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Objects;

public class TreeMap<K, V> implements Map<K, V> {
    private static final int TO_STRING_MIN_ITEMS_DISTANCE = 2;
    private static final char TO_STRING_ITEMS_DELIMITER = ' ';

    private int size;
    protected Node<K, V> root;
    protected final Comparator<K> comparator;

    protected static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;
        private int height;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            height = 1;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public int getHeight() {
            return height;
        }
    }

    public Node<K, V> getRoot() {
        return root;
    }

    private static class EmbeddedComparator<K extends Comparable<K>> implements Comparator<K> {
        @Override
        public int compare(K key1, K key2) {
            return key1.compareTo(key2);
        }
    }

    @SuppressWarnings("unchecked")
    public TreeMap() {
        this((Comparator<K>) new EmbeddedComparator<>());
    }

    public TreeMap(Comparator<K> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    protected Node<K, V> getNode(K key) {
        if (comparator instanceof EmbeddedComparator) {
            if (key == null) {
                throw new NullPointerException();
            } else if (!(key instanceof Comparable)) {
                throw new IllegalArgumentException();
            }
        }
        return getNodeByComparator(key);
    }

    private Node<K, V> getNodeByComparator(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int compareResult = comparator.compare(key, current.key);
            if (compareResult > 0) {
                current = current.right;
            } else if (compareResult < 0) {
                current = current.left;
            } else {
                return current;
            }
        }
        return null;
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null) {
            for (V valueFromMap : values()) {
                if (valueFromMap == null) {
                    return true;
                }
            }
        } else {
            for (V valueFromMap : values()) {
                if (value.equals(valueFromMap)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = getNode(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        if (comparator instanceof EmbeddedComparator) {
            if (key == null) {
                throw new NullPointerException();
            } else if (!(key instanceof Comparable)) {
                throw new IllegalArgumentException();
            }
        }
        return putValueByComparator(key, value);
    }

    private V putValueByComparator(K key, V value) {
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
            return null;
        }

        for (Node<K, V> current = root; ; ) {
            int compareResult = comparator.compare(key, current.key);
            if (compareResult > 0) {
                if (current.right == null) {
                    current.right = new Node<>(key, value, current);
                    size++;
                    balanceByRotateAsNeeded(current.right);
                    return null;
                } else {
                    current = current.right;
                }
            } else if (compareResult < 0) {
                if (current.left == null) {
                    current.left = new Node<>(key, value, current);
                    size++;
                    balanceByRotateAsNeeded(current.left);
                    return null;
                } else {
                    current = current.left;
                }
            } else {
                V oldValue = current.value;
                current.setValue(value);
                return oldValue;
            }
        }
    }

    private void balanceByRotateAsNeeded(Node<K, V> node) {
        node.height = calculateHeightNode(node);
        int balanceFactor = calculateBalanceFactor(node);
        if (balanceFactor < -1) {
            if (calculateBalanceFactor(node.right) > 0) {
                Node<K, V> originalRightNode = node.right;
                rotateRight(node.right);
                originalRightNode.height = calculateHeightNode(originalRightNode);
            }
            rotateLeft(node);
            node.height = calculateHeightNode(node);
            balanceByRotateAsNeeded(node.parent);
        } else if (balanceFactor > 1) {
            if (calculateBalanceFactor(node.left) < 0) {
                Node<K, V> originalLeftNode = node.left;
                rotateLeft(node.left);
                originalLeftNode.height = calculateHeightNode(originalLeftNode);
            }
            rotateRight(node);
            node.height = calculateHeightNode(node);
            balanceByRotateAsNeeded(node.parent);
        } else {
            updateHeightGoingUpNodes(node.parent);
        }
    }

    private int calculateBalanceFactor(Node<K, V> current) {
        if (current == null) {
            return 0;
        }

        if (current.left != null) {
            if (current.right != null) {
                return current.left.height - current.right.height;
            }
            return current.left.height;
        }
        if (current.right != null) {
            return -current.right.height;
        }
        return 0;
    }

    public int calculateHeightNode(Node<K, V> node) {
        if (node == null) return 0;

        if (node.left != null) {
            if (node.right != null) {
                return Math.max(node.left.height, node.right.height) + 1;
            }
            return node.left.height + 1;
        }
        if (node.right != null) {
            return node.right.height + 1;
        }
        return 1;
    }

    private void updateHeightGoingUpNodes(Node<K, V> node) {
        if (node == null) return;

        int newNodeHeight = calculateHeightNode(node);
        if (node.height != newNodeHeight) {
            node.height = newNodeHeight;
            updateHeightGoingUpNodes(node.parent);
        }
    }

    @Override
    public V remove(K key) {
        Node<K, V> deletingNode = getNode(key);
        if (deletingNode == null) {
            return null;
        }

        if (deletingNode.right != null) {
            replaceNodeBySmallestFromRightSide(deletingNode);
        } else if (deletingNode.left != null) {
            replaceNodeByGreatestFromLeftSide(deletingNode);
        } else {
            if (deletingNode.parent.right == deletingNode) {
                deletingNode.parent.right = null;
            } else {
                deletingNode.parent.left = null;
            }
            --size;
        }
        clearNodeLinks(deletingNode);
        return deletingNode.value;
    }

    private void replaceNodeBySmallestFromRightSide(Node<K, V> deletingNode) {
        Node<K, V> smallestNodeFromRightSide = deletingNode.right;
        while (smallestNodeFromRightSide.left != null) {
            smallestNodeFromRightSide = smallestNodeFromRightSide.left;
        }

        Node<K, V> smallestNodeFromRightSideParent = null;
        if (smallestNodeFromRightSide.parent != deletingNode) {
            smallestNodeFromRightSideParent = smallestNodeFromRightSide.parent;
            smallestNodeFromRightSide.parent.left = smallestNodeFromRightSide.right;

            if (smallestNodeFromRightSide.right != null) {
                smallestNodeFromRightSide.right.parent = smallestNodeFromRightSide.parent;
            }

            smallestNodeFromRightSide.right = deletingNode.right;
            deletingNode.right.parent = smallestNodeFromRightSide;
        }

        smallestNodeFromRightSide.parent = deletingNode.parent;
        if (deletingNode.parent != null) {
            if (deletingNode.parent.left == deletingNode) {
                deletingNode.parent.left = smallestNodeFromRightSide;
            } else {
                deletingNode.parent.right = smallestNodeFromRightSide;
            }
        } else {
            root = smallestNodeFromRightSide;
        }

        smallestNodeFromRightSide.left = deletingNode.left;
        if (deletingNode.left != null) {
            deletingNode.left.parent = smallestNodeFromRightSide;
        }
        --size;

        if (smallestNodeFromRightSideParent != null) {
            balanceByRotateAsNeeded(smallestNodeFromRightSideParent);
        } else {
            balanceByRotateAsNeeded(smallestNodeFromRightSide);
        }
    }

    private void replaceNodeByGreatestFromLeftSide(Node<K, V> deletingNode) {
        Node<K, V> greatestNodeFromLeftSide = deletingNode.left;
        while (greatestNodeFromLeftSide.right != null) {
            greatestNodeFromLeftSide = greatestNodeFromLeftSide.right;
        }

        Node<K, V> greatestNodeFromLeftSideParent = null;
        if (greatestNodeFromLeftSide.parent != deletingNode) {
            greatestNodeFromLeftSideParent = greatestNodeFromLeftSide.parent;
            greatestNodeFromLeftSide.parent.right = greatestNodeFromLeftSide.left;

            if (greatestNodeFromLeftSide.left != null) {
                greatestNodeFromLeftSide.left.parent = greatestNodeFromLeftSide.parent;
            }

            greatestNodeFromLeftSide.left = deletingNode.left;
            deletingNode.left.parent = greatestNodeFromLeftSide;
        }

        greatestNodeFromLeftSide.parent = deletingNode.parent;
        if (deletingNode.parent != null) {
            if (deletingNode.parent.right == deletingNode) {
                deletingNode.parent.right = greatestNodeFromLeftSide;
            } else {
                deletingNode.parent.left = greatestNodeFromLeftSide;
            }
        } else {
            root = greatestNodeFromLeftSide;
        }

        greatestNodeFromLeftSide.right = deletingNode.right;
        if (deletingNode.right != null) {
            deletingNode.right.parent = greatestNodeFromLeftSide;
        }
        --size;

        if (greatestNodeFromLeftSideParent != null) {
            balanceByRotateAsNeeded(greatestNodeFromLeftSideParent);
        } else {
            balanceByRotateAsNeeded(greatestNodeFromLeftSide);
        }
    }

    public void rotateRight(Node<K, V> node) {
        if (node.right == null) return;

        if (node.parent != null) {
            Node<K, V> parent = node.parent;
            if (parent.left == node) {
                parent.left = node.left;
            } else {
                parent.right = node.left;
            }
        }

        Node<K, V> rightChildOfLeftNode = node.left.right;
        node.left.right = node;
        node.left.parent = node.parent;
        node.parent = node.left;
        node.left = rightChildOfLeftNode;
        if (rightChildOfLeftNode != null) {
            rightChildOfLeftNode.parent = node;
        }

        if (node == root) {
            root = node.left;
        }
    }

    public void rotateLeft(Node<K, V> node) {
        if (node.right == null) return;

        if (node.parent != null) {
            Node<K, V> parent = node.parent;
            if (parent.left == node) {
                parent.left = node.right;
            } else {
                parent.right = node.right;
            }
        }

        Node<K, V> leftChildOfRightNode = node.right.left;
        node.right.left = node;
        node.right.parent = node.parent;
        node.parent = node.right;
        node.right = leftChildOfRightNode;
        if (leftChildOfRightNode != null) {
            leftChildOfRightNode.parent = node;
        }

        if (node == root) {
            root = node.right;
        }
    }

    @Override
    public void clear() {
        clearSubNodeLinks(root);
        size = 0;
    }

    private void clearSubNodeLinks(Node<K, V> node) {
        if (node == null) return;

        clearSubNodeLinks(node.left);
        clearSubNodeLinks(node.right);
        clearNodeLinks(node);
    }

    private void clearNodeLinks(Node<K, V> deletingNode) {
        deletingNode.parent = null;
        deletingNode.left = null;
        deletingNode.right = null;
        deletingNode.value = null;
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>(size);
        addSubNodeValues(collection, root);
        return collection;
    }

    private void addSubNodeValues(Collection<V> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodeValues(collection, node.left);
        collection.add(node.value);
        addSubNodeValues(collection, node.right);
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>(size);
        addSubNodeKeys(collection, root);
        return collection;
    }

    private void addSubNodeKeys(Collection<K> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodeKeys(collection, node.left);
        collection.add(node.key);
        addSubNodeKeys(collection, node.right);
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>(size);
        addSubNodes(collection, root);
        return collection;
    }

    private void addSubNodes(Collection<Entry<K, V>> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodes(collection, node.left);
        collection.add(node);
        addSubNodes(collection, node.right);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();

        int rowNodesIndent = TO_STRING_MIN_ITEMS_DISTANCE;
        int maxKeyLength = Objects.toString(getBiggestKey(root)).length();
        Map<Integer, Integer> indexesOfLastPrintedItemInRows = new HashMap<>();

        Deque<PrintingNode<K, V>> deque = new LinkedList<>();
        deque.addLast(new PrintingNode<>(root, 0));
        while (!deque.isEmpty()) {
            PrintingNode<K, V> printingNode = deque.pollFirst();
            rowNodesIndent = addLeftIndentForNode(stringBuilder, rowNodesIndent, maxKeyLength, indexesOfLastPrintedItemInRows, printingNode);

            // дополняем до длины наибольшего ключа с учетом централизации
            stringBuilder.append(StringUtils.center(Objects.toString(printingNode.node.key), maxKeyLength));

            if (printingNode.node.left != null) {
                deque.addLast(new PrintingNode<>(printingNode.node.left, printingNode.indexInRow << 1));
            }
            if (printingNode.node.right != null) {
                deque.addLast(new PrintingNode<>(printingNode.node.right, (printingNode.indexInRow << 1) + 1));
            }
        }
        return stringBuilder.toString();
    }

    /** Добавление отступа перед выводимым элементом. */
    private int addLeftIndentForNode(StringBuilder stringBuilder, int rowNodesIndent, int maxKeyLength,
                                     Map<Integer, Integer> indexesOfLastPrintedItemInRows, PrintingNode<K, V> printingNode) {
        Integer prevLastPrintedItemInRowIndex = indexesOfLastPrintedItemInRows.put(root.height - printingNode.node.height, printingNode.indexInRow);
        if (prevLastPrintedItemInRowIndex == null) {
            prevLastPrintedItemInRowIndex = -1;
            rowNodesIndent = getRowItemsIndent(root.height, root.height - printingNode.node.height, maxKeyLength);

            stringBuilder.append("\n");
            // заполняем левый отступ первого элемента строки символом delimiter'ом
            for (int i = 0; i < (rowNodesIndent >>> 1); ++i) {
                stringBuilder.append(TO_STRING_ITEMS_DELIMITER);
            }
        } else {
            for (int j = 0; j < rowNodesIndent; ++j) {
                stringBuilder.append(TO_STRING_ITEMS_DELIMITER);
            }
        }

        int distanceToPrevNodeInRow = printingNode.indexInRow - prevLastPrintedItemInRowIndex;
        // заполняем отступы за отсутствующие элементы строки
        for (int i = 1; i < distanceToPrevNodeInRow; ++i) {
            // заполняем отступы между соседними элементами строки, которые отсутствуют
            for (int j = 0; j < rowNodesIndent; ++j) {
                stringBuilder.append(TO_STRING_ITEMS_DELIMITER);
            }
            // заполняем отступы за отсутствующие элементы строки
            for (int j = 0; j < maxKeyLength; ++j) {
                stringBuilder.append(TO_STRING_ITEMS_DELIMITER);
            }
        }
        return rowNodesIndent;
    }

    private K getBiggestKey(Node<K, V> node) {
        if (node == null) return null;

        while (node.right != null) {
            node = node.right;
        }
        return node.getKey();
    }

    private int getRowItemsIndent(int maxRowIndex, int rowIndex, int maxKeyLength) {
        if (rowIndex >= maxRowIndex - 1) {
            return TO_STRING_MIN_ITEMS_DISTANCE;
        }
        return (getRowItemsIndent(maxRowIndex, rowIndex + 1, maxKeyLength) << 1) + maxKeyLength;
    }

    // private int getRowItemsIndent(int rowsCount, int rowNumber) {
    //     int powerOfTwo = 1 << (rowsCount - rowNumber - 1);
    //     return ITEM_SHIFT * powerOfTwo + ITEM_WIDTH * (powerOfTwo >>> 1) + ITEM_WIDTH;
    // }

    private static class PrintingNode<K, V> {
        private final Node<K, V> node;
        private final int indexInRow;

        private PrintingNode(Node<K, V> node, int indexInRow) {
            this.node = node;
            this.indexInRow = indexInRow;
        }
    }
}
