/**
 * Main.java
 *
 * Test harness for the stack assignment.
 *
 * This file:
 *   1. Tests SinglyLinkedList
 *   2. Tests SinglyLinkedStack
 *   3. Tests StackApplication.checkParentheses
 *   4. Tests StackApplication.infixToPostfix
 *
 * You CANNOT modify this file.
 * It reports exactly how many tests passed and failed.
 */
public class Main {

    private static int passed = 0;
    private static int failed = 0;

    /** Utility: check a boolean condition */
    private static void check(String description, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + description);
            passed++;
        } else {
            System.out.println("[FAIL] " + description);
            failed++;
        }
    }

    /** Utility: check equality of strings (handles null) */
    private static void checkEquals(String description, String expected, String actual) {
        boolean ok = (expected == null ? actual == null : expected.equals(actual));
        if (ok) {
            System.out.println("[PASS] " + description);
            passed++;
        } else {
            System.out.println("[FAIL] " + description);
            System.out.println("       Expected: " + expected);
            System.out.println("       Actual:   " + actual);
            failed++;
        }
    }

    public static void main(String[] args) {

        System.out.println("\n==============================");
        System.out.println(" SECTION 1 — SinglyLinkedList ");
        System.out.println("==============================");

        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();

        check("New list should be empty", list.isEmpty());
        checkEquals("peekFirst on empty list returns null", null, String.valueOf(list.peekFirst()));
        checkEquals("removeFirst on empty list returns null", null, String.valueOf(list.removeFirst()));

        list.addFirst(10);
        list.addFirst(20);
        list.addFirst(30);
        check("List size should be 3 after 3 addFirst", list.size() == 3);
        check("peekFirst should be 30", list.peekFirst() != null && list.peekFirst() == 30);

        check("removeFirst returns 30", list.removeFirst() == 30);
        check("removeFirst returns 20", list.removeFirst() == 20);
        check("removeFirst returns 10", list.removeFirst() == 10);
        check("List should be empty after removing all elements", list.isEmpty());

        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        check("addLast 1,2,3 -> size should be 3", list.size() == 3);
        check("get(0) == 1", list.get(0) != null && list.get(0) == 1);
        check("get(1) == 2", list.get(1) != null && list.get(1) == 2);
        check("get(2) == 3", list.get(2) != null && list.get(2) == 3);

        list.clear();
        check("clear() -> list should be empty", list.isEmpty());


        System.out.println("\n==============================");
        System.out.println(" SECTION 2 — SinglyLinkedStack ");
        System.out.println("==============================");

        SinglyLinkedStack<String> stack = new SinglyLinkedStack<>();

        check("New stack should be empty", stack.isEmpty());
        checkEquals("peek() on empty stack returns null", null, stack.peek());
        checkEquals("pop() on empty stack returns null", null, stack.pop());

        stack.push("a");
        stack.push("b");
        stack.push("c");
        check("After pushing a,b,c -> size = 3", stack.size() == 3);
        check("peek() should be c", "c".equals(stack.peek()));
        check("pop() returns c", "c".equals(stack.pop()));
        check("pop() returns b", "b".equals(stack.pop()));
        check("pop() returns a", "a".equals(stack.pop()));
        check("Stack should be empty after pops", stack.isEmpty());


        System.out.println("\n=============================================");
        System.out.println(" SECTION 3 — StackApplication.checkParentheses");
        System.out.println("=============================================");

        check("Balanced: \"()\"", StackApplication.checkParentheses("()"));
        check("Balanced: \"([]{})\"", StackApplication.checkParentheses("([]{})"));
        check("Balanced with noise: \"a+(b*c)-[12/(x-7)]\"", StackApplication.checkParentheses("a+(b*c)-[12/(x-7)]"));

        check("Not balanced: \"(]\"", !StackApplication.checkParentheses("(]"));
        check("Not balanced: \"([)]\"", !StackApplication.checkParentheses("([)]"));
        check("Not balanced: \"(()\"", !StackApplication.checkParentheses("(()"));
        check("Not balanced: \")(\"", !StackApplication.checkParentheses(")("));


        System.out.println("\n=============================================");
        System.out.println(" SECTION 4 — StackApplication.infixToPostfix ");
        System.out.println("=============================================");

        checkEquals("3 + 4 * 2  ->  3 4 2 * +",
                "3 4 2 * +", StackApplication.infixToPostfix("3 + 4 * 2"));

        checkEquals("(3 + 4) * 2  ->  3 4 + 2 *",
                "3 4 + 2 *", StackApplication.infixToPostfix("(3 + 4) * 2"));

        checkEquals("2 ^ 3 ^ 2  ->  2 3 2 ^ ^",
                "2 3 2 ^ ^", StackApplication.infixToPostfix("2 ^ 3 ^ 2"));

        checkEquals("A + B * C ^ D - E  ->  A B C D ^ * + E -",
                "A B C D ^ * + E -", StackApplication.infixToPostfix("A + B * C ^ D - E"));

        checkEquals("(x1 + y_2) / (7 - 5) ^ 2",
                "x1 y_2 + 7 5 - 2 ^ /", StackApplication.infixToPostfix("(x1 + y_2) / (7 - 5) ^ 2"));

        checkEquals("decimal: 3.5 * (2 + 0.25)",
                "3.5 2 0.25 + *", StackApplication.infixToPostfix("3.5 * (2 + 0.25)"));

        checkEquals("Malformed: (1 + 2 -> null",
                null, StackApplication.infixToPostfix("(1 + 2"));

        checkEquals("Malformed: 1 + 2) -> null",
                null, StackApplication.infixToPostfix("1 + 2)"));

        checkEquals("Invalid char: 2 @ 3 -> null",
                null, StackApplication.infixToPostfix("2 @ 3"));

        checkEquals("1+2-3 -> 1 2 + 3 -",
                "1 2 + 3 -", StackApplication.infixToPostfix("1+2-3"));


        System.out.println("\n================================");
        System.out.println(" FINAL RESULTS ");
        System.out.println("================================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("================================");

        if (failed == 0) {
            System.out.println("All tests passed! Excellent work!");
        } else {
            System.out.println("Some tests failed — review your implementations.");
        }
    }
}