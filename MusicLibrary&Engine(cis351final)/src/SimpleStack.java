public class SimpleStack<T> {
    private static class Node<T> {
        private T value;
        private Node<T> next;

        private Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private int size;

    public SimpleStack() {
        this.head = null;
        this.size = 0;
    }

    public void push(T value) {
        Node<T> node = new Node<T>(value);
        node.next = head;
        head = node;
        size++;
    }

    public T pop() {
        if (head == null) {
            return null;
        }
        T value = head.value;
        head = head.next;
        size--;
        return value;
    }

    public T peek() {
        if (head == null) {
            return null;
        }
        return head.value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
