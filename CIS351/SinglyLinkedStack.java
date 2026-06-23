/**
 * SinglyLinkedStack.java
 *
 * A minimal, generic LIFO stack built on top of the provided SinglyLinkedList<T>.
 * 
 * You must decide how to use the underlying list to represent the
 * "top" of the stack and implement each method accordingly.
 *
 * Error policy for this assignment:
 *  - pop() and peek() return null if the stack is empty.
 */
public class SinglyLinkedStack<T> {

    // Underlying storage structure for the stack
    private final SinglyLinkedList<T> list = new SinglyLinkedList<>();

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param item the element to push
     */
    public void push(T item) {
        list.addFirst(item);
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * Error policy: return null if the stack is empty.
     *
     * @return the removed element, or null if empty
     */
    public T pop() {
        return list.removeFirst();
    }

    /**
     * Returns (without removing) the top element of the stack.
     *
     * Error policy: return null if the stack is empty.
     *
     * @return the top element, or null if empty
     */
    public T peek() {
        return list.peekFirst();
    }

    /**
     * Indicates whether the stack currently contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements currently stored in the stack.
     *
     * @return the stack size
     */
    public int size() {
        return list.size(); // placeholder
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Returns a string representation of the stack for debugging purposes.
     * It is acceptable to delegate to the list's toString().
     *
     * @return a human-readable representation of the stack
     */
    @Override
    public String toString() {
        return list.toString();
    }
}