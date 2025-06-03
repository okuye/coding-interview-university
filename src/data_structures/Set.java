```java
package data_structures;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract data type that can store certain values, without any particular order, and no repeated values.
 * Unlike most other collection types, rather than retrieving a specific element from a set, one typically tests
 * a value for membership in a set.
 *
 * This implementation uses a hash table with separate chaining (linked lists) for collision resolution.
 *
 * Complexity (assuming a good hash function and uniform distribution):
 *
 *   Algorithm    Average      Worst Case
 *   Space        O(n + k)     O(n + k)      (where k = number of buckets)
 *   add          O(1)         O(n)          (all items hash to the same bucket)
 *   remove       O(1)         O(n)
 *   contains     O(1)         O(n)
 *
 * @param <T> the type of elements stored in the set (must implement hashCode() and equals())
 */
public class Set<T> {
    /** Default initial capacity (number of buckets). Must be a power of two. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /** Default load factor before resizing. */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /** Array of buckets; each bucket is the head of a singly linked list of Nodes. */
    private Node<T>[] table;
    /** Current number of elements in the set. */
    private int size;
    /** The threshold at which to resize (capacity * loadFactor). */
    private int threshold;
    /** Load factor for deciding when to resize. */
    private final float loadFactor;

    /**
     * Node class for storing a value in each bucket's linked list.
     */
    private static class Node<E> {
        final int hash;
        final E value;
        Node<E> next;

        Node(int hash, E value, Node<E> next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Constructs an empty Set with default initial capacity (16) and load factor (0.75).
     */
    @SuppressWarnings("unchecked")
    public Set() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<T>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * loadFactor);
        this.size = 0;
    }

    /**
     * Constructs an empty Set with the specified initial capacity and default load factor (0.75).
     *
     * @param initialCapacity initial number of buckets (will be rounded up to a power of two)
     * @throws IllegalArgumentException if initialCapacity is non-positive
     */
    @SuppressWarnings("unchecked")
    public Set(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        int cap = tableSizeFor(initialCapacity);
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<T>[]) new Node[cap];
        this.threshold = (int) (cap * loadFactor);
        this.size = 0;
    }

    /**
     * Constructs an empty Set with the specified initial capacity and load factor.
     *
     * @param initialCapacity initial number of buckets (will be rounded up to a power of two)
     * @param loadFactor load factor threshold for resizing
     * @throws IllegalArgumentException if initialCapacity is non-positive or loadFactor is non-positive
     */
    @SuppressWarnings("unchecked")
    public Set(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        int cap = tableSizeFor(initialCapacity);
        this.loadFactor = loadFactor;
        this.table = (Node<T>[]) new Node[cap];
        this.threshold = (int) (cap * loadFactor);
        this.size = 0;
    }

    /**
     * Calculates the next power-of-two capacity for the given requested capacity.
     */
    private int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= (1 << 30)) ? (1 << 30) : n + 1;
    }

    /**
     * Computes the hash for a given value, applying a supplemental mix to reduce collisions.
     */
    private int hash(Object value) {
        int h = (value == null) ? 0 : value.hashCode();
        // Spread bits (from Java 8 HashMap implementation)
        return h ^ (h >>> 16);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value the element to add (may be null)
     * @return true if the set did not already contain the specified element
     */
    public boolean add(T value) {
        int h = hash(value);
        int index = (table.length - 1) & h;

        // Check if value already exists in bucket
        for (Node<T> node = table[index]; node != null; node = node.next) {
            if (node.hash == h && (value == node.value || (value != null && value.equals(node.value)))) {
                return false; // already present
            }
        }

        // Insert new node at head of bucket
        Node<T> newNode = new Node<>(h, value, table[index]);
        table[index] = newNode;
        size++;

        // Check if resizing is needed
        if (size > threshold) {
            resize();
        }
        return true;
    }

    /**
     * Returns true if this set contains the specified element.
     *
     * @param value element whose presence is to be tested (may be null)
     * @return true if this set contains the element
     */
    public boolean contains(Object value) {
        int h = hash(value);
        int index = (table.length - 1) & h;
        for (Node<T> node = table[index]; node != null; node = node.next) {
            if (node.hash == h && (value == node.value || (value != null && value.equals(node.value)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from the set, if present (may be null)
     * @return true if the set contained the specified element
     */
    public boolean remove(Object value) {
        int h = hash(value);
        int index = (table.length - 1) & h;
        Node<T> prev = null;
        Node<T> curr = table[index];

        while (curr != null) {
            if (curr.hash == h && (value == curr.value || (value != null && value.equals(curr.value)))) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the size of the set
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from this set. The capacity remains unchanged.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    /**
     * Returns a List of all elements in this set. Order is not guaranteed.
     *
     * @return List of elements
     */
    public List<T> elements() {
        List<T> list = new ArrayList<>(size);
        for (Node<T> bucket : table) {
            for (Node<T> node = bucket; node != null; node = node.next) {
                list.add(node.value);
            }
        }
        return list;
    }

    /**
     * Doubles the capacity of the table and rehashes all existing elements.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1; // double capacity
        Node<T>[] oldTable = table;
        table = (Node<T>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        // Rehash all nodes into new table
        for (Node<T> headNode : oldTable) {
            while (headNode != null) {
                Node<T> nextNode = headNode.next;
                int index = (newCapacity - 1) & headNode.hash;
                headNode.next = table[index];
                table[index] = headNode;
                headNode = nextNode;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        Set<String> set = new Set<>();

        System.out.println("Adding elements:");
        System.out.println("Add \"apple\": " + set.add("apple"));   // true
        System.out.println("Add \"banana\": " + set.add("banana")); // true
        System.out.println("Add \"apple\" again: " + set.add("apple")); // false (duplicate)
        System.out.println("Size after adds: " + set.size());       // 2

        System.out.println("\nContains check:");
        System.out.println("Contains \"apple\"? " + set.contains("apple"));   // true
        System.out.println("Contains \"cherry\"? " + set.contains("cherry")); // false

        System.out.println("\nRemoving elements:");
        System.out.println("Remove \"banana\": " + set.remove("banana")); // true
        System.out.println("Remove \"cherry\": " + set.remove("cherry")); // false (not present)
        System.out.println("Size after removals: " + set.size());         // 1

        System.out.println("\nCurrent elements in set:");
        for (String s : set.elements()) {
            System.out.println(s);
        }

        System.out.println("\nFilling set to trigger resize:");
        for (int i = 0; i < 20; i++) {
            set.add("key" + i);
        }
        System.out.println("Size after bulk add: " + set.size());
        System.out.println("Capacity after resize (approx): " + set.table.length);

        System.out.println("\nClearing set:");
        set.clear();
        System.out.println("Size after clear: " + set.size()); // 0
        System.out.println("Is empty? " + set.isEmpty());     // true
    }
}
```
