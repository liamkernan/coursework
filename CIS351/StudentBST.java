import java.util.*;

/**
 * Binary Search Tree starter for students.
 * - Duplicates are NOT allowed.
 * - Null keys are simply ignored where applicable (e.g., insert does nothing, contains returns false).
 *
 * Students must implement:
 *   1) insert(T value)            // maintain parent pointers; ignore null; ignore duplicates
 *   2) remove(T value) -> T       // return the removed element, or null if not found
 *   3) bfs() -> List<T>           // level-order traversal
 *   4) dfsInOrder() -> List<T>    // Left, Root, Right
 *   5) dfsPreOrder() -> List<T>   // Root, Left, Right
 *   6) dfsPostOrder() -> List<T>  // Left, Right, Root
 *
 * Deletion strategy when a node has two children:
 *   - Use the in-order successor (smallest node in the right subtree).
 */
public class StudentBST<T extends Comparable<? super T>> {

    /** Tree node (with parent pointer). */
    private static final class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;
        Node<T> parent;  // parent pointer

        Node(T data, Node<T> parent) {
            this.data = data;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }

    // --- State ---
    private Node<T> root;
    private int size;
    private final Comparator<? super T> comparator;

    // --- Constructors ---

    /** Constructs an empty BST using natural ordering (T must be Comparable). */
    public StudentBST() {
        this(null);
    }

    /**
    * Constructs an empty BST using a provided comparator.
    * If comparator is null, natural ordering is used.
    */
    public StudentBST(Comparator<? super T> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    // --- Public API students must implement ---


    public void insert(T value) {
        Node<T> current = root;

        if (value == null) {
            return;
        } else if (size == 0){
            root = new Node<>(value, null);
            size++;
            return;
        }

        while (current != null) {
            int b = compare(value, current.data);
            if (b < 0) {
                if (current.left == null){
                    current.left = new Node<>(value, current);
                    size++;
                    return;
                }
                current = current.left;
            } else if (b > 0) {
                if (current.right == null){
                    current.right = new Node<>(value, current);
                    size++;
                    return;
                }
                current = current.right;
            } else {
                return;
            }


        }
        // Starter intentionally does nothing so tests can SKIP until implemented.
        // Implement per the steps above (do not throw; ignore null and duplicates).
    }


    public T remove(T value) {
        if (value == null){
            return null;
        }
        Node<T> node = root;
        while (node != null){
            int b = compare(value, node.data);

            if (b < 0){
                node = node.left;
            } else if (b > 0){
                node = node.right;
            } else {
                break;
            }
        }

        if (node == null){
            return null;
        }

        T removed = node.data;

        if (node.left != null && node.right != null){
            Node<T> nextup = minNode(node.right);
            node.data = nextup.data;
            node = nextup;
        }

        Node<T> child = (node.left != null) ? node.left : node.right;

        if (node.parent == null){
            root = child;
            if (child != null){
                child.parent = null;
            }
        } else {
                Node<T> parent = node.parent;
                if (parent.left == node){
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                if (child != null){
                    child.parent = parent;
                }
            }


        size--;
        return removed;
    }


    public List<T> bfs() {
        if (root == null){
            return new ArrayList<>();
        }

        ArrayDeque<Node<T>> queue = new ArrayDeque<>();
        List<T> result = new ArrayList<>();
        queue.add(root);

        while (!queue.isEmpty()){
            Node<T> curr = queue.poll();
            result.add(curr.data);
            if (curr.left != null){
                queue.add(curr.left);
            }
            if (curr.right != null){
                queue.add(curr.right);
            }
        }


        return result;
    }


    public List<T> dfsInOrder() {
        List<T> result = new ArrayList<>();
        inOrderHelper(root, result);
        return result;
    }

    private void inOrderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        inOrderHelper(node.left, result);   // Left
        result.add(node.data);              // Root
        inOrderHelper(node.right, result);  // Right
    }


    public List<T> dfsPreOrder() {
        List<T> result = new ArrayList<>();
        preOrderHelper(root, result);
        return result;
    }

    private void preOrderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        result.add(node.data);              // Root first
        preOrderHelper(node.left, result);  // Left
        preOrderHelper(node.right, result); // Right
    }


    public List<T> dfsPostOrder() {
        List<T> result = new ArrayList<>();
        postOrderHelper(root, result);
        return result;
    }

    private void postOrderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        postOrderHelper(node.left, result);  // Left
        postOrderHelper(node.right, result); // Right
        result.add(node.data);               // Root last
    }

    // --- Convenience methods (already implemented) ---

    /** @return number of elements in the tree */
    public int size() {
        return size;
    }

    /** @return true if tree has no elements */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Removes all elements from the tree. */
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if value exists in the tree. Null is treated as "not found". */
    public boolean contains(T value) {
        if (value == null) return false;
        Node<T> curr = root;
        while (curr != null) {
            int cmp = compare(value, curr.data);
            if (cmp == 0) return true;
            curr = (cmp < 0) ? curr.left : curr.right;
        }
        return false;
    }

    // --- Protected / Private helpers students may use ---

    /** Compare a and b using comparator if present, otherwise natural order. */
    private int compare(T a, T b) {
        if (comparator != null) return comparator.compare(a, b);
        return a.compareTo(b);
    }

    /** Return the leftmost (minimum) node in the given subtree (or null if subtree is null). */
    private Node<T> minNode(Node<T> subtree) {
        if (subtree == null) return null;
        Node<T> curr = subtree;
        while (curr.left != null) curr = curr.left;
        return curr;
        // Students may call this from remove() if they like.
    }

    /** For debugging: returns an in-order string like [a, b, c]. */
    @Override
    public String toString() {
        List<T> in = new ArrayList<>();
        inOrderCollect(root, in);
        return in.toString();
    }

    private void inOrderCollect(Node<T> n, List<T> out) {
        if (n == null) return;
        inOrderCollect(n.left, out);
        out.add(n.data);
        inOrderCollect(n.right, out);
    }

    // --- Simple test harness (no JUnit, no exceptions) ---

    public static void main(String[] args) {
        System.out.println("StudentBST Starter: Running basic tests (no JUnit)...");
        int[] seed = {50, 30, 70, 20, 40, 60, 80};

        // Test 0: Empty traversals
        run("T0: empty traversals", () -> {
            StudentBST<Integer> t = new StudentBST<>();
            List<Integer> bfs = requireNotNull("bfs", t.bfs());
            if (isSkipped()) return;
            List<Integer> in  = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            List<Integer> pre = requireNotNull("dfsPreOrder", t.dfsPreOrder());
            if (isSkipped()) return;
            List<Integer> post= requireNotNull("dfsPostOrder", t.dfsPostOrder());
            if (isSkipped()) return;

            expectList("empty BFS", bfs, list());
            expectList("empty InOrder", in, list());
            expectList("empty PreOrder", pre, list());
            expectList("empty PostOrder", post, list());
            expectEq("empty size", t.size(), 0);
        });

        // Test 1: Insert basic shape
        run("T1: insert and traversals", () -> {
            StudentBST<Integer> t = new StudentBST<>();
            for (int v : seed) t.insert(v);

            List<Integer> bfs = requireNotNull("bfs", t.bfs());
            if (isSkipped()) return;
            List<Integer> in  = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            List<Integer> pre = requireNotNull("dfsPreOrder", t.dfsPreOrder());
            if (isSkipped()) return;
            List<Integer> post= requireNotNull("dfsPostOrder", t.dfsPostOrder());
            if (isSkipped()) return;

            expectEq("size after inserts", t.size(), 7);
            expectList("BFS level-order", bfs, list(50, 30, 70, 20, 40, 60, 80));
            expectList("DFS in-order", in, list(20, 30, 40, 50, 60, 70, 80));
            expectList("DFS pre-order", pre, list(50, 30, 20, 40, 70, 60, 80));
            expectList("DFS post-order", post, list(20, 40, 30, 60, 80, 70, 50));
        });

        // Test 2: Removal cases
        run("T2: removal cases", () -> {
            StudentBST<Integer> t = new StudentBST<>();
            for (int v : seed) t.insert(v);

            Integer r1 = requireNotNull("remove", t.remove(20));
            if (isSkipped()) return;
            expectEq("remove leaf 20 returned", r1, 20);
            List<Integer> bfs1 = requireNotNull("bfs", t.bfs());
            if (isSkipped()) return;
            List<Integer> in1  = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            expectList("after remove 20 (BFS)", bfs1, list(50, 30, 70, 40, 60, 80));
            expectList("after remove 20 (InOrder)", in1, list(30, 40, 50, 60, 70, 80));
            expectEq("size after remove 20", t.size(), 6);

            Integer r2 = requireNotNull("remove", t.remove(30));
            if (isSkipped()) return;
            expectEq("remove one-child 30 returned", r2, 30);
            List<Integer> bfs2 = requireNotNull("bfs", t.bfs());
            if (isSkipped()) return;
            List<Integer> in2  = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            expectList("after remove 30 (BFS)", bfs2, list(50, 40, 70, 60, 80));
            expectList("after remove 30 (InOrder)", in2, list(40, 50, 60, 70, 80));
            expectEq("size after remove 30", t.size(), 5);

            Integer r3 = requireNotNull("remove", t.remove(70));
            if (isSkipped()) return;
            expectEq("remove two-children 70 returned", r3, 70);
            List<Integer> bfs3 = requireNotNull("bfs", t.bfs());
            if (isSkipped()) return;
            List<Integer> in3  = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            expectList("after remove 70 (BFS)", bfs3, list(50, 40, 80, 60));
            expectList("after remove 70 (InOrder)", in3, list(40, 50, 60, 80));
            expectEq("size after remove 70", t.size(), 4);

            Integer r4 = requireNotNull("remove", t.remove(999));
            if (isSkipped()) return;
            expectEq("remove missing 999 returned", r4, null);
            expectEq("size unchanged", t.size(), 4);
        });

        // Test 3: Duplicates should not change size or structure
        run("T3: duplicates ignored", () -> {
            StudentBST<Integer> t = new StudentBST<>();
            for (int v : seed) t.insert(v);
            t.insert(50);
            t.insert(30);
            t.insert(70);

            List<Integer> in = requireNotNull("dfsInOrder", t.dfsInOrder());
            if (isSkipped()) return;
            expectEq("size (duplicates ignored)", t.size(), 7);
            expectList("in-order unchanged", in, list(20, 30, 40, 50, 60, 70, 80));
        });

        System.out.println("Done.");
    }

    // --- Test utilities (no JUnit, no exceptions) ---

    private static boolean SKIPPED = false;
    private static String SKIP_METHOD = "";
    private static boolean FAILED = false;
    private static String FAIL_MSG = "";

    /** Reset flags for each test run. */
    private static void resetStatus() {
        SKIPPED = false; SKIP_METHOD = "";
        FAILED = false;  FAIL_MSG = "";
    }

    /** If value is null, mark test as SKIPPED for the given method. */
    private static <X> X requireNotNull(String method, X value) {
        if (value == null && !SKIPPED) {
            SKIPPED = true;
            SKIP_METHOD = method;
        }
        return value;
    }

    /** Check if current test is marked as skipped. */
    private static boolean isSkipped() { return SKIPPED; }

    /** Runs a single test block; prints SKIPPED/OK/FAIL without throwing. */
    private static void run(String name, Runnable r) {
        System.out.print(name + " ... ");
        resetStatus();
        try {
            r.run();
        } catch (Throwable t) {
            FAILED = true;
            FAIL_MSG = "Unexpected error: " + t.getClass().getSimpleName() + " - " + t.getMessage();
        }

        if (SKIPPED) {
            System.out.println("SKIPPED (TODO: " + SKIP_METHOD + ")");
        } else if (FAILED) {
            System.out.println("FAIL");
            System.out.println("  " + FAIL_MSG);
        } else {
            System.out.println("OK");
        }
    }

    /** Build an immutable List<T> (varargs). */
    @SafeVarargs
    private static <T> List<T> list(T... items) {
        return Collections.unmodifiableList(Arrays.asList(items));
    }

    /** Records failure if two objects are not equal. */
    private static void expectEq(String label, Object actual, Object expected) {
        if (!Objects.equals(actual, expected) && !FAILED) {
            FAILED = true;
            FAIL_MSG = label + " | expected: " + expected + ", actual: " + actual;
        }
    }

    /** Records failure if two lists differ in size/order/elements. */
    private static <T> void expectList(String label, List<T> actual, List<T> expected) {
        if (FAILED) return;
        if (actual == null) {
            FAILED = true;
            FAIL_MSG = label + " | actual list is null";
            return;
        }
        if (expected.size() != actual.size()) {
            FAILED = true;
            FAIL_MSG = label + " | size mismatch. expected size: " + expected.size()
                    + ", actual size: " + actual.size()
                    + "\n  expected: " + expected + "\n  actual  : " + actual;
            return;
        }
        for (int i = 0; i < expected.size(); i++) {
            if (!Objects.equals(expected.get(i), actual.get(i))) {
                FAILED = true;
                FAIL_MSG = label + " | first mismatch at index " + i
                        + ". expected: " + expected.get(i) + ", actual: " + actual.get(i)
                        + "\n  expected: " + expected + "\n  actual  : " + actual;
                return;
            }
        }
    }
}
