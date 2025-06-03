Below is a simple, generic implementation of a binary search tree in Java that supports insertion, lookup (contains), and deletion. It also includes a couple of traversal methods (in-order and pre-order) for testing or debugging purposes. Feel free to adapt or extend it (for example, by adding breadth-first traversals, size tracking, height computations, etc.).

```java
package data_structures;

/**
 * Data structures that store "items" (such as numbers, names, etc.) in memory.
 * They allow fast lookup, addition and removal of items, and can be used to implement either dynamic sets of items,
 * or lookup tables that allow finding an item by its key (e.g., finding the phone number of a person by name).
 *
 * Binary search trees keep their keys in sorted order, so that lookup and other operations can use the principle of binary search:
 * when looking for a key in a tree (or a place to insert a new key), they traverse the tree from root to leaf, making comparisons
 * to keys stored in the nodes of the tree and deciding, based on the comparison, to continue searching in the left or right subtrees.
 *
 * Complexity :
 *
 *   Algorithm   Average     Worst Case
 *   Space       O(n)        O(n)
 *   Search      O(log n)    O(n)
 *   Insert      O(log n)    O(n)
 *   Delete      O(log n)    O(n)
 *
 * @param <T> any type that implements Comparable<T>
 */
public class BinarySearchTree<T extends Comparable<T>> {

    /**
     * Inner class representing a node in the BST.
     */
    private static class Node<T> {
        T key;
        Node<T> left, right;

        Node(T key) {
            this.key = key;
        }
    }

    /** The root of the BST. */
    private Node<T> root;

    /** Constructs an empty BST. */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Inserts a new key into the BST. If the key already exists, this implementation does nothing.
     *
     * @param key the key to insert
     */
    public void insert(T key) {
        root = insertRec(root, key);
    }

    /** Helper for insert: recursively find the right spot. */
    private Node<T> insertRec(Node<T> node, T key) {
        if (node == null) {
            return new Node<>(key);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRec(node.left, key);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key);
        }
        // if key == node.key, do nothing (no duplicates)
        return node;
    }

    /**
     * Checks whether the BST contains the given key.
     *
     * @param key the key to search for
     * @return true if key is found, false otherwise
     */
    public boolean contains(T key) {
        return containsRec(root, key);
    }

    /** Helper for contains: recursively search left or right. */
    private boolean containsRec(Node<T> node, T key) {
        if (node == null) {
            return false;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsRec(node.left, key);
        } else if (cmp > 0) {
            return containsRec(node.right, key);
        } else {
            return true;  // found
        }
    }

    /**
     * Deletes the given key from the BST (if it exists).
     *
     * @param key the key to delete
     */
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    /** Helper for delete: recursive deletion routine. */
    private Node<T> deleteRec(Node<T> node, T key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRec(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            // node.key == key: we need to remove this node
            if (node.left == null && node.right == null) {
                // case 1: no children
                return null;
            } else if (node.left == null) {
                // case 2: only right child
                return node.right;
            } else if (node.right == null) {
                // case 2: only left child
                return node.left;
            } else {
                // case 3: two children: find inorder successor (smallest in right subtree)
                Node<T> succ = findMin(node.right);
                node.key = succ.key;
                // delete that successor node from right subtree
                node.right = deleteRec(node.right, succ.key);
            }
        }
        return node;
    }

    /** Finds the node with minimum key in the subtree rooted at node. */
    private Node<T> findMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Performs an in-order traversal of the BST and prints each key.
     * In-order traversal prints keys in ascending sorted order.
     */
    public void inorderTraversal() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(Node<T> node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.print(node.key + " ");
            inorderRec(node.right);
        }
    }

    /**
     * Performs a pre-order traversal of the BST and prints each key.
     * Pre-order traversal can be useful for serialization, debugging, etc.
     */
    public void preorderTraversal() {
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(Node<T> node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preorderRec(node.left);
            preorderRec(node.right);
        }
    }

    /**
     * Returns the height of the BST (length of longest path from root to a leaf).
     * An empty tree has height 0; a single-node tree has height 1.
     *
     * @return the height of the tree
     */
    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node<T> node) {
        if (node == null) {
            return 0;
        }
        int lh = heightRec(node.left);
        int rh = heightRec(node.right);
        return Math.max(lh, rh) + 1;
    }

    /**
     * Returns the total number of nodes in the BST.
     *
     * @return size of the tree
     */
    public int size() {
        return sizeRec(root);
    }

    private int sizeRec(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRec(node.left) + sizeRec(node.right);
    }

    // You can add more methods here, such as:
    //   - findMax()
    //   - successor/predecessor
    //   - level-order (BFS) traversals
    //   - methods to check if the tree is balanced, etc.

    // -------------------------------------------------------------------------
    // Example usage / quick test (you can remove this main method in production)
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        // Insert some values
        int[] values = { 50, 30, 20, 40, 70, 60, 80 };
        for (int v : values) {
            bst.insert(v);
        }

        // In-order should print: 20 30 40 50 60 70 80
        System.out.print("In-order: ");
        bst.inorderTraversal();

        // Pre-order should print: 50 30 20 40 70 60 80
        System.out.print("Pre-order: ");
        bst.preorderTraversal();

        // Check contains()
        System.out.println("Contains 40? " + bst.contains(40)); // true
        System.out.println("Contains 25? " + bst.contains(25)); // false

        // Delete some nodes
        bst.delete(20); // leaf
        bst.delete(30); // node with one child (40)
        bst.delete(50); // root with two children

        // After deletions, in-order should be: 40 60 70 80
        System.out.print("In-order after deletions: ");
        bst.inorderTraversal();

        // Size and height
        System.out.println("Size: " + bst.size());       // 4
        System.out.println("Height: " + bst.height());   // depends on how it's structured
    }
}
```

---

### How this implementation works

1. **Generic Type Parameter `<T extends Comparable<T>>`:**
   By requiring `T` to implement `Comparable<T>`, we can compare arbitrary “keys” (numbers, strings, or any custom object that implements `compareTo`) and keep them in sorted order.

2. **Node Inner Class:**
   Each `Node<T>` holds a `key` plus references to its left/right children.  A `null` reference means “empty subtree.”

3. **Insertion (`insert` / `insertRec`):**

   * If the current subtree is empty (`node == null`), create a new node.
   * Otherwise compare the new key with `node.key`:

     * If it’s smaller, recurse into `node.left`.
     * If it’s larger, recurse into `node.right`.
     * If equal, do nothing (no duplicates in this simple version).

4. **Search / Contains (`contains` / `containsRec`):**

   * Traverse down the tree: at each node compare the target key with `node.key`.
   * If equal, return true; if smaller, go left; if larger, go right; if you hit `null`, return false.

5. **Deletion (`delete` / `deleteRec`):**

   * First locate the node to remove.
   * If it has no children, simply remove it (return `null` to the parent).
   * If it has exactly one child, “splice” over it by returning its non-null child.
   * If it has two children, find the in-order successor (minimum in the right subtree), copy its key into the current node, then delete that successor from the right subtree.

6. **Traversals:**

   * **In-order (`inorderTraversal`)** prints keys in ascending order.
   * **Pre-order (`preorderTraversal`)** prints root before its subtrees, which you can use for tree serialization or debugging.

7. **Utility Methods:**

   * `height()` returns the length of the longest root→leaf path.
   * `size()` returns the total number of nodes.

---

#### Notes & Possible Extensions

* Right now this BST does **not** self-balance. In the worst case (insert keys in sorted order), it degrades to a linked list (height ≈ n), and all operations become $O(n)$. If you need guaranteed $O(\log n)$ in all cases, you’ll want a balanced variation (e.g., AVL, Red-Black Tree, etc.).

* If you want to store associated values along with each key (i.e., map keys → values), you could change

  ```java
  private static class Node<T> {
      T key;
      Node<T> left, right;
  }
  ```

  to something like

  ```java
  private static class Node<K,V> {
      K key;
      V value;
      Node<K,V> left, right;
  }
  ```

  and then make the outer class `public class BinarySearchTree<K extends Comparable<K>,V>`, adding a `insert(K key, V value)` and a `V get(K key)` method, etc.

* You can also add methods to compute the minimum or maximum key in the whole tree (just walk left from the root for `findMin(root)`, or right for `findMax(root)`).

* For debugging, the `main(...)` method shows an example of inserting a few integers, traversing, checking membership, and deleting a few nodes. Feel free to remove or adapt it in production code.

This code should compile directly under Java 8+ (or any modern Java) as long as it’s placed in the `data_structures` package. Simply drop the file `BinarySearchTree.java` into `data_structures/` and call it from your own tests.
