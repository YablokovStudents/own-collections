package dev.students.practice.collections.impl;

import dev.students.practice.collections.*;

import java.util.Arrays;
import java.util.Comparator;

public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private static final int MAX_CAPACITY = 1 << 30; // 2^31

    private static final int TREEIFY_THRESHOLD = 8;
    private static final int UNTREEIFY_THRESHOLD = 6;

    private static final int[] POWERS_OF_TWO = getPowersOfTwo();

    private static int[] getPowersOfTwo() {
        int[] result = new int[31];
        int powerOfTwo = 1;
        result[0] = powerOfTwo;
        for (int i = 1; i < result.length; ++i) {
            result[i] = (powerOfTwo <<= 1);
        }
        return result;
    }

    private static class Bucket<K, V> {
        private Deque<Entry<K, V>> queue;
        private Map<K, V> map;

        Bucket(Deque<Entry<K, V>> queue) {
            this.queue = queue;
        }

        Bucket(Map<K, V> map) {
            this.map = map;
        }

        boolean isQueue() {
            return queue != null;
        }

        Deque<Entry<K, V>> asQueue() {
            return queue;
        }

        void setAsQueue(Deque<Entry<K, V>> queue) {
            this.queue = queue;
            map.clear();
            map = null;
        }

        boolean isMap() {
            return map != null;
        }

        Map<K, V> asMap() {
            return map;
        }

        void setAsMap(Map<K, V> map) {
            this.map = map;
            queue.clear();
            queue = null;
        }
    }

    private static class QueueNode<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        private QueueNode(K key, V value) {
            this.key = key;
            this.value = value;
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
    }

    private static class EmbeddedTreeMap<K, V> extends TreeMap<K, V> {
        private enum CompareResult {
            LOWER(-1),
            EQUALS(0),
            GREATER(1),
            MAYBE_GREATER_OR_LOWER(2);

            private final int value;

            CompareResult(int value) {
                this.value = value;
            }

            private static CompareResult valueFrom(int value) {
                for (CompareResult compareResult : values()) {
                    if (compareResult.value == value) {
                        return compareResult;
                    }
                }
                throw new IllegalArgumentException();
            }
        }

        private EmbeddedTreeMap() {
            super(new HashCodeBasedComparator<>());
        }

        private static class HashCodeBasedComparator<K> implements Comparator<K> {
            @Override
            @SuppressWarnings("unchecked")
            public int compare(K key1, K key2) {
                if (key1 == null) {
                    return ((key2 == null) ? CompareResult.EQUALS : CompareResult.LOWER).value;
                }
                if (key2 == null) {
                    return CompareResult.GREATER.value;
                }
                if (key1.hashCode() == key2.hashCode()) {
                    if (key1.equals(key2)) {
                        return CompareResult.EQUALS.value;
                    }
                    if ((key1 instanceof Comparable) && (key1.getClass() == key2.getClass())) {
                        int compareResult = ((Comparable<K>) key1).compareTo(key2);
                        if (compareResult > 0) {
                            return CompareResult.GREATER.value;
                        }
                        if (compareResult < 0) {
                            return CompareResult.LOWER.value;
                        }
                        return CompareResult.EQUALS.value;
                    }
                    return CompareResult.MAYBE_GREATER_OR_LOWER.value;
                }
                return ((key1.hashCode() < key2.hashCode()) ? CompareResult.LOWER : CompareResult.GREATER).value;
            }
        }

        @Override
        protected Node<K, V> getNode(K key) {
            return getNode(key, root);
        }

        private Node<K, V> getNode(K key, Node<K, V> currentNode) {
            if (currentNode == null) {
                return null;
            }

            int compareResult = comparator.compare(key, currentNode.getKey());
            switch (CompareResult.valueFrom(compareResult)) {
                case EQUALS:
                    return currentNode;

                case LOWER:
                    return getNode(key, currentNode.getLeft());

                case GREATER:
                    return getNode(key, currentNode.getRight());

                case MAYBE_GREATER_OR_LOWER: {
                    Node<K, V> foundNode = getNode(key, currentNode.getRight());
                    return (foundNode != null) ? foundNode : getNode(key, currentNode.getLeft());
                }

                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private final float loadFactor;
    private int threshold;

    private Bucket<K, V>[] buckets;
    private int size;

    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, float loadFactor) {
        int capacity = calculateCapacityByBinaryShift(initialCapacity);
        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
        buckets = createEmptyBuckets(capacity);
    }

    @SuppressWarnings("unchecked")
    private Bucket<K, V>[] createEmptyBuckets(int capacity) {
        Bucket<K, V>[] result = new Bucket[capacity];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Bucket<>(new LinkedList<>());
        }
        return result;
    }

    private int calculateCapacityByBinaryShift(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity == 0) {
            return 1;
        }

        int result = initialCapacity - 1;
        result |= result >>> 1;  // получаем 2 старших единичных бита
        result |= result >>> 2;  // получаем 4 старших единичных бита
        result |= result >>> 4;  // получаем 8 старших единичных бита
        result |= result >>> 8;  // получаем 16 старших единичных бита
        result |= result >>> 16; // получаем 32 старших единичных бита
        return result + 1;
    }

    private int calculateCapacityByBinarySearch(int initialCapacity) {
        int itemIndex = binarySearch(initialCapacity, POWERS_OF_TWO, 0, POWERS_OF_TWO.length - 1);
        return POWERS_OF_TWO[itemIndex];
    }

    private int binarySearch(int value, int[] array, int leftIndex, int rightIndex) {
        if (leftIndex == rightIndex) {
            int item = array[leftIndex];
            if (item == value) {
                return leftIndex;
            } else if (item < value) {
                return leftIndex;
            } else {
                return (leftIndex == array.length - 1) ? leftIndex : leftIndex + 1;
            }
        }

        int middleIndex = leftIndex + (rightIndex - leftIndex) >>> 1;
        int arrayItem = array[middleIndex];
        if (value == arrayItem) {
            return middleIndex;
        } else if (value < arrayItem) {
            return binarySearch(value, array, leftIndex, middleIndex - 1);
        } else {
            return binarySearch(value, array, middleIndex + 1, rightIndex);
        }
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
        Bucket<K, V> bucket = buckets[getBucketIndex(key)];
        if (bucket.isQueue()) {
            return getEntryByKey(key, bucket.asQueue()) != null;
        } else {
            return bucket.asMap().containsKey(key);
        }
    }

    private int getBucketIndex(K key) {
        return getBucketIndex(key, buckets.length);
    }

    private int getBucketIndex(K key, int bucketLength) {
        if (key == null) {
            return 0;
        }
        return calculateHashCode(key, bucketLength) & (bucketLength - 1);
    }

    /**
     * @return число, младшие n бит которого попарно связаны с битами из более старших групп,
     * причем n == количеству бит, которые могут использоваться при кодировании индекса в рамках массива корзин.
     */
    private int calculateHashCode(K key, int bucketLength) {
        if (bucketLength == 1) {
            return key.hashCode();
        }

        int originalHashCode = key.hashCode();
        int result = originalHashCode;
        int powerOfTwo = Arrays.binarySearch(POWERS_OF_TWO, bucketLength);
        for (int i = 1; i < (32 / powerOfTwo); i++) {
            result ^= (originalHashCode >>>= powerOfTwo);
        }
        result ^= originalHashCode >>> powerOfTwo; // имеет смысл лишь для случая: if (32 % powerOfTwo != 0)
        return result;
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null) {
            for (Bucket<K, V> bucket : buckets) {
                if (bucket.isQueue()) {
                    for (Entry<K, V> entry : bucket.asQueue()) {
                        if (entry.getValue() == null) {
                            return true;
                        }
                    }
                } else {
                    if (bucket.asMap().containsValue(null)) {
                        return true;
                    }
                }
            }
        } else {
            for (Bucket<K, V> bucket : buckets) {
                if (bucket.isQueue()) {
                    for (Entry<K, V> entry : bucket.asQueue()) {
                        if (value.equals(entry.getValue())) {
                            return true;
                        }
                    }
                } else {
                    if (bucket.asMap().containsValue(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Bucket<K, V> bucket = buckets[getBucketIndex(key)];
        if (bucket.isQueue()) {
            Entry<K, V> foundEntry = getEntryByKey(key, bucket.asQueue());
            return (foundEntry == null) ? null : foundEntry.getValue();
        } else {
            return bucket.asMap().get(key);
        }
    }

    private Entry<K, V> getEntryByKey(K key, Deque<Entry<K, V>> bucket) {
        if (key == null) {
            for (Entry<K, V> node : bucket) {
                if (node.getKey() == null) {
                    return node;
                }
            }
        } else {
            for (Entry<K, V> node : bucket) {
                if (key.equals(node.getKey())) {
                    return node;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Bucket<K, V> bucket = buckets[bucketIndex];
        if (bucket.isQueue()) {
            return putIntoBucket(key, value, bucket.asQueue(), bucketIndex);
        } else {
            return putIntoBucket(key, value, bucket.asMap());
        }
    }

    private V putIntoBucket(K key, V value, Deque<Entry<K, V>> bucket, int bucketIndex) {
        Entry<K, V> foundEntry = getEntryByKey(key, bucket);
        if (foundEntry != null) {
            V oldValue = foundEntry.getValue();
            foundEntry.setValue(value);
            return oldValue;
        } else {
            bucket.add(new QueueNode<>(key, value));
            if (++size > threshold) {
                increaseBucketNumber();
            } else if (bucket.size() >= TREEIFY_THRESHOLD) {
                buckets[bucketIndex].setAsMap(convertQueueBucketToMap(bucket));
            }
            return null;
        }
    }

    private Map<K, V> convertQueueBucketToMap(Deque<Entry<K, V>> queueBucket) {
        Map<K, V> mapBucket = new EmbeddedTreeMap<>();
        for (Entry<K, V> entry : queueBucket) {
            mapBucket.put(entry.getKey(), entry.getValue());
        }
        return mapBucket;
    }

    private Deque<Entry<K, V>> convertMapBucketToQueue(Map<K, V> mapBucket) {
        Deque<Entry<K, V>> queueBucket = new LinkedList<>();
        for (Entry<K, V> entry : mapBucket.entrySet()) {
            queueBucket.add(new QueueNode<>(entry.getKey(), entry.getValue()));
        }
        return queueBucket;
    }

    private V putIntoBucket(K key, V value, Map<K, V> bucket) {
        int bucketSizeBeforePut = bucket.size();
        V oldValue = bucket.put(key, value);
        if (bucket.size() > bucketSizeBeforePut) {
            if (++size > threshold) {
                increaseBucketNumber();
            }
        }
        return oldValue;
    }

    private void increaseBucketNumber() {
        buckets = createIncreasedBuckets();
        threshold = calculateNewThreshold();
    }

    private Bucket<K, V>[] createIncreasedBuckets() {
        Bucket<K, V>[] buckets = createEmptyBuckets(this.buckets.length << 1);
        for (Bucket<K, V> bucket : this.buckets) {
            if (bucket.isQueue()) {
                for (Entry<K, V> entry : bucket.asQueue()) {
                    putEntryToBucket(entry, buckets);
                }
            } else {
                for (Entry<K, V> entry : bucket.asMap().entrySet()) {
                    putEntryToBucket(entry, buckets);
                }
            }
        }
        return buckets;
    }

    private void putEntryToBucket(Entry<K, V> entry, Bucket<K, V>[] buckets) {
        int bucketIndex = getBucketIndex(entry.getKey(), buckets.length);
        Bucket<K, V> bucket = buckets[bucketIndex];
        if (bucket.isQueue()) {
            Deque<Entry<K, V>> queueBucket = bucket.asQueue();
            queueBucket.add(entry);
            if (queueBucket.size() >= TREEIFY_THRESHOLD) {
                buckets[bucketIndex].setAsMap(convertQueueBucketToMap(queueBucket));
            }
        } else {
            bucket.asMap().put(entry.getKey(), entry.getValue());
        }
    }

    private int calculateNewThreshold() {
        if (buckets.length == MAX_CAPACITY) {
            return Integer.MAX_VALUE;
        } else {
            return (int) (buckets.length * loadFactor);
        }
    }

    @Override
    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Bucket<K, V> bucket = buckets[bucketIndex];
        if (bucket.isQueue()) {
            return removeFromBucket(key, bucket.asQueue());
        } else {
            return removeFromBucket(key, bucket.asMap(), bucketIndex);
        }
    }

    private V removeFromBucket(K key, Deque<Entry<K, V>> bucket) {
        Entry<K, V> foundEntry = getEntryByKey(key, bucket);
        if (foundEntry == null) {
            return null;
        }

        V removedValue = foundEntry.getValue();
        bucket.remove(foundEntry);
        --size;
        return removedValue;
    }

    private V removeFromBucket(K key, Map<K, V> bucket, int bucketIndex) {
        int bucketSizeBeforeRemove = bucket.size();
        V removedValue = bucket.remove(key);
        if (bucket.size() < bucketSizeBeforeRemove) {
            if (--size <= UNTREEIFY_THRESHOLD) {
                buckets[bucketIndex].setAsQueue(convertMapBucketToQueue(bucket));
            }
        }
        return removedValue;
    }

    @Override
    public void clear() {
        size = 0;
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) {
                bucket.asQueue().clear();
            } else {
                bucket.asMap().clear();
            }
        }
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) {
                for (Entry<K, V> entry : bucket.asQueue()) {
                    values.add(entry.getValue());
                }
            } else {
                for (V value : bucket.asMap().values()) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> keys = new ArrayList<>();
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) {
                for (Entry<K, V> entry : bucket.asQueue()) {
                    keys.add(entry.getKey());
                }
            } else {
                for (K key : bucket.asMap().keySet()) {
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> entries = new ArrayList<>();
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) {
                for (Entry<K, V> entry : bucket.asQueue()) {
                    entries.add(entry);
                }
            } else {
                for (Entry<K, V> entry : bucket.asMap().entrySet()) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }
}
