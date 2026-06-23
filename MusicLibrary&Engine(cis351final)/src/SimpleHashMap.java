public class SimpleHashMap<K, V> {
    public interface KeyValueVisitor<K, V> {
        void visit(K key, V value);
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public SimpleHashMap() {
        this.buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public boolean put(K key, V value) {
        if (key == null || value == null) {
            return false;
        }
        if ((double) (size + 1) / buckets.length > LOAD_FACTOR) {
            resize();
        }
        int bucketIndex = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                return false;
            }
            current = current.next;
        }
        Entry<K, V> entry = new Entry<K, V>(key, value);
        entry.next = buckets[bucketIndex];
        buckets[bucketIndex] = entry;
        size++;
        return true;
    }

    public boolean putOrReplace(K key, V value) {
        if (key == null || value == null) {
            return false;
        }
        if ((double) (size + 1) / buckets.length > LOAD_FACTOR) {
            resize();
        }
        int bucketIndex = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return true;
            }
            current = current.next;
        }
        Entry<K, V> entry = new Entry<K, V>(key, value);
        entry.next = buckets[bucketIndex];
        buckets[bucketIndex] = entry;
        size++;
        return true;
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        int bucketIndex = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int bucketIndex = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[bucketIndex];
        Entry<K, V> prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[bucketIndex] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public DynamicArray<K> keys() {
        DynamicArray<K> out = new DynamicArray<K>();
        for (int i = 0; i < buckets.length; i++) {
            Entry<K, V> current = buckets[i];
            while (current != null) {
                out.add(current.key);
                current = current.next;
            }
        }
        return out;
    }

    public DynamicArray<V> values() {
        DynamicArray<V> out = new DynamicArray<V>();
        for (int i = 0; i < buckets.length; i++) {
            Entry<K, V> current = buckets[i];
            while (current != null) {
                out.add(current.value);
                current = current.next;
            }
        }
        return out;
    }

    public void forEach(KeyValueVisitor<K, V> visitor) {
        if (visitor == null) {
            return;
        }
        for (int i = 0; i < buckets.length; i++) {
            Entry<K, V> current = buckets[i];
            while (current != null) {
                visitor.visit(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int indexFor(K key, int length) {
        int hash = key.hashCode();
        hash ^= (hash >>> 16);
        if (hash == Integer.MIN_VALUE) {
            hash = 0;
        }
        hash = Math.abs(hash);
        return hash % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        Entry<K, V>[] newBuckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];
        buckets = newBuckets;
        size = 0;

        for (int i = 0; i < oldBuckets.length; i++) {
            Entry<K, V> current = oldBuckets[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}
