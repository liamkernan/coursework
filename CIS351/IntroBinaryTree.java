
/**
 * IntroBinaryTree starter code (generic, not a BST).
 * <p>
 * This class provides fully implemented constructors and a complete inner Node class.
 * All tree methods are provided with signatures, rich Javadoc explaining the goal,
 * and //TODO comments telling students what to implement. Methods currently return
 * placeholder values (no exceptions are thrown) so the file compiles and a simple
 * main() can run. Prefer recursive solutions where noted.
 *
 * <p><strong>Height Convention:</strong>
 * We will use <em>edge-height</em>: height(empty) = -1; height(leaf) = 0.
 */
import java.util.Objects;

public class IntroBinaryTree<T> {

    /** Root node of the tree (may be null for an empty tree). */
    private Node<T> root;

    // ----------------------------
    // Constructors (fully implemented)
    // ----------------------------

    /** Creates an empty tree. */
    public IntroBinaryTree() {
        this.root = null;
    }

    /** Creates a tree with a single root node holding {@code rootValue}. */
    public IntroBinaryTree(T rootValue) {
        this.root = (rootValue == null) ? null : new Node<>(rootValue);
    }

    /**
     * Creates a tree with the given root node.
     * Parent pointer of the given root (if non-null) is cleared to null.
     */
    public IntroBinaryTree(Node<T> root) {
        this.root = root;
        if (this.root != null) {
            this.root.parent = null; // tree root has no parent
        }
    }

    /** Returns the current root (may be null). */
    public Node<T> getRoot() { return root; }

    /** Replaces the root (may be null). */
    public void setRoot(Node<T> newRoot) {
        this.root = newRoot;
        if (this.root != null) this.root.parent = null;
    }

    // ----------------------------
    // Node class (fully implemented)
    // ----------------------------

    /**
     * Tree node class.
     * @param <E> value type
     */
    public static class Node<E> {
        public E value;
        public Node<E> left;
        public Node<E> right;
        public Node<E> parent; // convenience; not required for recursion

        /** Creates a leaf node with the given value. */
        public Node(E value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        /**
         * Creates a node with given value and (possibly null) children.
         * Parent pointers are set appropriately.
         */
        public Node(E value, Node<E> left, Node<E> right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = null; // parent is set by the caller after attaching to the tree
            if (this.left != null) this.left.parent = this;
            if (this.right != null) this.right.parent = this;
        }

        /**
         * Full constructor specifying value, children, and parent.
         * Caller is responsible for passing consistent relationships.
         */
        public Node(E value, Node<E> left, Node<E> right, Node<E> parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
            if (this.left != null) this.left.parent = this;
            if (this.right != null) this.right.parent = this;
        }
    }

    // ----------------------------
    // Methods for students to implement (currently return placeholders)
    // ----------------------------

    /**
     * Returns the total number of nodes in the tree.
     * <p>Goal: visit each node exactly once and count it.
     * Prefer a recursive solution that starts at the root.
     * @return number of nodes in this tree
     */
    public int size() {
        // TODO: Implement recursively from the root using a private helper: size(Node<T> node).
        // Hints:
        //  - Base case: null subtree has size 0.
        //  - Otherwise: 1 + size(node.left) + size(node.right).
        return size(root); // placeholder so code compiles
    }

    /** Private recursive helper for size. */
    private int size(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right); // placeholder
    }

    /**
     * Returns the height (max depth) of the tree using edge-height.
     * <p>Goal: compute the longest path from the given node to a leaf.
     * @return height of the tree (empty tree returns -1)
     */
    public int height() {
        return height(root);
    }

    /** Private recursive helper for height. */
    private int height(Node<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Returns true if the tree is empty.
     * <p>Goal: constant-time check based on the root.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns true if the given node is an internal (non-leaf) node.
     * <p>Goal: local property check; no recursion needed.
     */
    public boolean isInternal(Node<T> node) {
        return node != null && (node.left != null || node.right != null);
    }

    /**
     * Returns true if the given node is a leaf (has no children).
     * <p>Goal: local property check; no recursion needed.
     */
    public boolean isLeaf(Node<T> node) {
        return node != null && node.left == null && node.right == null;
    }

    /**
     * Finds and returns a node whose value equals {@code value}; returns null if absent.
     * <p>Goal: recursively search the tree (generic binary tree; not ordered like a BST).
     * @param value value to search for (may be null)
     * @return the first node found whose value equals {@code value}, or null if not found
     */
    public Node<T> find(T value) {
        return find(root, value);
    }

    /** Private recursive helper for find. */
    private Node<T> find(Node<T> node, T value) {
        if (node == null) return null;
        if (Objects.equals(node.value, value)) return node;
        Node<T> found = find(node.left, value);
        if (found != null) return found;
        return find(node.right, value);
    }

    /**
     * Attaches a new node with {@code value} as the LEFT child of {@code node} and returns it.
     * <p>Goal: modify structure; set parent pointers appropriately.
     * <p>Policy: If a left child already exists, either throw IllegalStateException OR replace it —
     * choose one policy and document it. (Typical: throw.)
     */
    public Node<T> addLeft(Node<T> node, T value) {
        if (node == null) throw new IllegalArgumentException("node cannot be null");
        if (node.left != null) throw new IllegalStateException("left child already exists");
        Node<T> child = new Node<>(value);
        node.left = child;
        child.parent = node;
        return child;
    }

    /**
     * Attaches a new node with {@code value} as the RIGHT child of {@code node} and returns it.
     * <p>Goal: modify structure; set parent pointers appropriately.
     * <p>Policy: If a right child already exists, throws IllegalStateException.
     */
    public Node<T> addRight(Node<T> node, T value) {
        if (node == null) throw new IllegalArgumentException("node cannot be null");
        if (node.right != null) throw new IllegalStateException("right child already exists");
        Node<T> child = new Node<>(value);
        node.right = child;
        child.parent = node;
        return child;
    }

    /**
     * Returns the parent of {@code node}, or null if {@code node} is root or not in this tree.
     * <p>Goal: local property if using parent pointers; otherwise, implement a recursive search from root
     * to locate the node and return the parent encountered along the way.
     */
    public Node<T> parent(Node<T> node) {
        if (node == null) return null;
        return node.parent;
    }

    /** Optional recursive helper if you choose not to rely on parent pointers. */
    private Node<T> parentOf(Node<T> current, Node<T> target) {
        if (current == null || current == target) return null;
        if (current.left == target || current.right == target) return current;
        Node<T> found = parentOf(current.left, target);
        if (found != null) return found;
        return parentOf(current.right, target);
    }

    /** Returns the LEFT child of {@code node} (may be null). */
    public Node<T> leftChild(Node<T> node) {
        if (node == null) return null;
        return node.left;
    }

    /** Returns the RIGHT child of {@code node} (may be null). */
    public Node<T> rightChild(Node<T> node) {
        if (node == null) return null;
        return node.right;
    }

    // ----------------------------
    // Simple manual tests (no JUnit)
    // ----------------------------
    public static void main(String[] args) {
        System.out.println("=== IntroBinaryTree starter: simple checks ===");

        // Empty tree checks
        IntroBinaryTree<Integer> empty = new IntroBinaryTree<>();
        System.out.println("Empty.size() -> expected 0, actual " + empty.size());
        System.out.println("Empty.height() -> expected -1, actual " + empty.height());
        System.out.println("Empty.isEmpty() -> expected true, actual " + empty.isEmpty());

        // Build a tiny tree manually via Node pointers (do NOT rely on addLeft/addRight yet)
        IntroBinaryTree<Integer> t = new IntroBinaryTree<>(10);
        Node<Integer> r = t.getRoot();
        r.left = new Node<>(5);  r.left.parent = r;
        r.right = new Node<>(20); r.right.parent = r;
        r.right.left = new Node<>(15); r.right.left.parent = r.right;

        System.out.println("\nTree built: root=10, left=5, right=20, right.left=15");
        System.out.println("size() -> expected 4, actual " + t.size());
        System.out.println("height() -> expected 2 (edge-height), actual " + t.height());

        // Local property checks
        System.out.println("isInternal(root) -> expected true, actual " + t.isInternal(r));
        System.out.println("isLeaf(root.left) -> expected true, actual " + t.isLeaf(r.left));

        // Search & relations
        Node<Integer> n20 = t.find(20);
        System.out.println("find(20) -> expected not null, actual " + (n20 != null));
        System.out.println("parent(root.left) -> expected 10, actual " + (t.parent(r.left) == null ? null : t.parent(r.left).value));
        System.out.println("leftChild(root) -> expected 5, actual " + (t.leftChild(r) == null ? null : t.leftChild(r).value));
        System.out.println("rightChild(root) -> expected 20, actual " + (t.rightChild(r) == null ? null : t.rightChild(r).value));

        // addLeft/addRight test: clear existing left child first so addLeft doesn't throw
        r.left = null;
        System.out.println("\nAttempt addLeft(root, 4) -> expected new left child value 4 after implementation.");
        t.addLeft(r, 4);
        System.out.println("root.left -> actual " + (r.left == null ? null : r.left.value));

        System.out.println("\nNOTE: Most outputs are placeholders until you complete each //TODO.");
    }
}
