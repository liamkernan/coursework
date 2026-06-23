public class DynamicArray<T> {
    private static final int DEFAULT_CAPACITY = 8;
    private Object[] data;
    private int size;

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        data[size] = value;
        size++;
    }

    public boolean addAt(int index, T value) {
        if (index < 0 || index > size) {
            return false;
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) data[index];
    }

    public boolean set(int index, T value) {
        if (index < 0 || index >= size) {
            return false;
        }
        data[index] = value;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (equalsValue(data[i], value)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public DynamicArray<T> copy() {
        DynamicArray<T> out = new DynamicArray<T>();
        out.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T value = (T) data[i];
            out.add(value);
        }
        return out;
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length) {
            return;
        }
        int newCapacity = data.length * 2;
        while (newCapacity < needed) {
            newCapacity *= 2;
        }
        Object[] grown = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            grown[i] = data[i];
        }
        data = grown;
    }

    private boolean equalsValue(Object left, Object right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.equals(right);
    }
}
