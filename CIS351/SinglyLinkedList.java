/**
 * SinglyLinkedList.java
 *
 * A basic implementation of a generic Singly Linked List (HEAD ONLY).
 * Supports common operations such as add/remove/search and indexing.
 */
public class SinglyLinkedList<T> {
    /**
     * Node represents a single element in the list.
     */
    private static class Node<T> {
        T data;
        Node<T> next;
        /**
         * Default constructor
         * (no need for it to be public since this is a private class)
         * Initializes the node without data
         * If you want the default constructor along with another, you must create both. Java only provides the default constructor when there isn't any.
         */
        Node() {
            this.data = null;
            this.next = null;
        }
        /**
         * Constructor with parameter
         * (no need for it to be public since this is a private class)
         * Initializes the node with the given element
         * Element is of generic type T. Data type will be selected by the class using the list
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;

    /**
     * Adds an element to the front of the list.
     */
    public void addFirst(T value) {
        Node<T> node = new Node<T>(value);
        node.next = head;
        head = node;
        size++;
    }

    /**
     * Adds an element to the end of the list.
     * Since we don't have indexes, you have to traverse the list until the end before adding the element.
     */
    public void addLast(T value) {
        Node<T> node2 = new Node<T>(value);
        if (size == 0){
            head = node2;
        } else {
            Node<T> current = head;
            while (current.next != null){
                current = current.next;
            }
            current.next = node2;
        }
        size++;
    }

    /**
     * Removes and returns the first element of the list.
     * @return the removed element, or null if the list is empty.
     */
    public T removeFirst() {
        if (size == 0){
            return null;
        }
            T data = head.data;
            head = head.next;
            size--;
        return data; // placeholder
    }

    /**
     * Returns (without removing) the first element of the list.
     * @return the first element, or null if the list is empty.
     */
    public T peekFirst() {
        if (size == 0) {
            return null;
        }
        return head.data;
    }

    /**
     * Removes and returns the last element of the list.
     * Returns null if the list is empty.
     * Since we don't have indexes, you have to traverse the list until you find the last element.
     * @return the removed element, or null if the list is empty.
     */
    public T removeLast() {
        if (size == 0){
            return null;
        } else if (size == 1){
            T data = head.data;
            head = null;
            size = 0;
            return data;
        } else {
            Node<T> current = head;
            while (current.next.next != null){
                current = current.next;
            }
            T data = current.next.data;
            current.next = null;
            size--;
            return data;
        }
    }

    /**
     * Removes the first occurrence of the given value.
     * @return true if an element was removed; false otherwise.
     */
    public boolean remove(T value) {
        if (size == 0){
            return false;
        } else {
            if (equalsValue(head.data, value)) {
                removeFirst();
                return true;
            }
            Node<T> previous = head;
            Node<T> current = head.next;
            while (current != null){
                if(equalsValue(current.data, value)){
                    previous.next = current.next;
                    size--;
                    return true;
                }
                previous = current;
                current = current.next;
            }
        }
        return false; // placeholder
    }

    /**
     * @return true if the list contains the given value, false otherwise.
     *
     */
    public boolean contains(T value) {
        Node<T> current = head;
        while (current != null){
            if (equalsValue(current.data, value)){
                return true;
            }
            current = current.next;
        }
       return false; // placeholder
    }

    /**
     * Returns the element at the given index.
     * @return null if index is invalid.
     */
    public T get(int index) {
        if (checkIndex(index) == false){
            return null;
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++){
            current = current.next;
        }
        return current.data;
    }

    /**
     * @return the number of elements in the list.
     */
    public int size() {
        // TODO: Return the current size of the list.
        return size; // placeholder
    }

    /**
     * @return true if the list has no elements.
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true; // placeholder
        } else {
            return false;
        }
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Returns a string representation of the list.
     * Example: [A -> B -> C]
     * Uses string concatenation (not StringBuilder), per your request.
     */
    @Override
    public String toString() {
        String result = "[";
        Node<T> current = head;
        while (current != null) {
            result += current.data;
            if (current.next != null) {
                result += " -> ";
            }
            current = current.next;
        }
        result += "]";
        return result;
    }

    // -------------------- Helpers --------------------
    private boolean checkIndex(int index) {
        if (index >= 0 && index < size){
            return true;
        }
        return false; // placeholder
    }

    private boolean equalsValue(T a, T b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // -------------------- Demo --------------------
    // public static void main(String[] args) {
    //     SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
    //     list.addLast(10);
    //     list.addLast(20);
    //     list.addFirst(5);
    //     System.out.println("List: " + list); // [5 -> 10 -> 20]
    //     System.out.println("Size: " + list.size()); // 3
    //     System.out.println("Contains 10? " + list.contains(10)); // true
    //     System.out.println("Element at index 1: " + list.get(1)); // 10
    //     System.out.println("Removed first: " + list.removeFirst()); // 5
    //     System.out.println("List after removeFirst: " + list); // [10 -> 20]
    //     System.out.println("Remove 20? " + list.remove(20)); // true
    //     System.out.println("List after remove(20): " + list); // [10]
    //     list.clear();
    //     System.out.println("Cleared list: " + list); // []
    //     System.out.println("Is empty? " + list.isEmpty()); // true
    // }
}