public class MyStack {

    private int[] stack;
    private int size;

    public MyStack(int capacity) {
        stack = new int[capacity];
        size = 0;
    }

    public void push(int value) {
        stack[size] = value;
        size++;
    }

    public int pop() {
        size--;
        return stack[size];
    }

    public int peek() {
        if (isEmpty() == true) {
            System.out.println("stack is empty, cannot peek");
            return -1;
        }
        return stack[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == stack.length - 1;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        MyStack myStack = new MyStack(10);

        // push 7 numbers
        myStack.push(10);
        myStack.push(20);
        myStack.push(30);
        myStack.push(40);
        myStack.push(50);
        myStack.push(60);
        myStack.push(70);

        // print current size
        System.out.println(myStack.size());

        // pop 2 numbers
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());

        // print empty and full results
        System.out.println(myStack.isEmpty());
        System.out.println(myStack.isFull());

        // print peek result
        System.out.println(myStack.peek());

        // pop 5 numbers
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());

        // pop one more
        System.out.println("one more pop:");
        myStack.pop();
    }
}

/*
    question 1: I was curious how stacks and FILO structures in general would compare
    to my experience with recursion, since I know it has a similar structure
    for execution.

    question 2: Yes, I think it was a good “visualization” of how stacks execute and will
    be a helpful foundation of knowledge for future structures.

    question 3: Yes, I had problems with the length and OutOfBound Expectations when
    I was first getting started, but they were pretty easy to work around by
    subtracting from the end of the list and just being more conscious of my
    position in the lists.
 */
