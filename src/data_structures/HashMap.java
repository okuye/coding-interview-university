```java
package data_structures;

import java.util.ArrayList;
import java.util.List;

/**
 * A hash table (also called a hash, hash map, map, unordered map, or dictionary) is a data structure
 * that pairs keys to values. This implementation uses separate chaining with singly linked lists for collision handling.
 *
 * Complexity (assuming a good hash function and uniform distribution):
 *
 *   Algorithm   Average       Worst Case
 *   Space       O(n + k)      O(n + k)      (where k = number of buckets)
 *   get         O(1)          O(n)          (all keys collide into one bucket)
 *   put         O(1)          O(n)
 *   remove      O(1)          O(n)
 *
 * @param <K> type of keys (must implement hashCode() and equals())
 * @param <V> type of values
 */
public class HashMap<K, V> {
    /** Default initial capacity (number of buckets). Must be a power of two. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /** Default load factor before resizing. */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /** Array of buckets; each bucket is the head of a singly linked list of Nodes. */
    private Node<K, V>[] table;
    /** Current number of key-value mappings in this map. */
    private int size;
    /** The threshold at which to resize (capacity * loadFactor). */
    private int threshold;
    /** Load factor for deciding when to resize. */
    private final float loadFactor;

    /**
     * Node class for storing key-value pairs in each bucket's linked list.
     */
    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Constructs an empty HashMap with default initial capacity (16) and load factor (0.75).
     */
    @SuppressWarnings("unchecked")
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * loadFactor);
        this.size = 0;
    }

    /**
     * Constructs an empty HashMap with the specified initial capacity and default load factor (0.75).
     *
     * @param initialCapacity initial number of buckets (will be rounded up to a power of two)
     * @throws IllegalArgumentException if initialCapacity is non-positive
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        int cap = tableSizeFor(initialCapacity);
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<K, V>[]) new Node[cap];
        this.threshold = (int) (cap * loadFactor);
        this.size = 0;
    }

    /**
     * Constructs an empty HashMap with the specified initial capacity and load factor.
     *
     * @param initialCapacity initial number of buckets (will be rounded up to a power of two)
     * @param loadFactor load factor threshold for resizing
     * @throws IllegalArgumentException if initialCapacity is non-positive or loadFactor is non-positive
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        int cap = tableSizeFor(initialCapacity);
        this.loadFactor = loadFactor;
        this.table = (Node<K, V>[]) new Node[cap];
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
     * Computes the hash for a given key, applying a supplemental mix to reduce collisions.
     */
    private int hash(Object key) {
        int h = key == null ? 0 : key.hashCode();
        // Spread bits (from Java 8 HashMap implementation)
        return h ^ (h >>> 16);
    }

    /**
     * Puts the key-value pair into the map. If the key already exists, its value is replaced.
     *
     * @param key key to insert (may be null)
     * @param value value to associate with the key (may be null)
     * @return the previous value associated with key, or null if none
     */
    public V put(K key, V value) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        // Traverse the bucket to check for existing key
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (key == node.key || (key != null && key.equals(node.key)))) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        // Insert new node at head of bucket
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
        // Check if resizing needed
        if (size > threshold) {
            resize();
        }
        return null;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key key to look up (may be null)
     * @return the value if found, or null if not found
     */
    public V get(Object key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (key == node.key || (key != null && key.equals(node.key)))) {
                return node.value;
            }
        }
        return null;
    }

    /**
     * Removes the key (and its corresponding value) from this map if present.
     *
     * @param key key whose mapping is to be removed (may be null)
     * @return the previous value associated with key, or null if none
     */
    public V remove(Object key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> prev = null;
        Node<K, V> curr = table[index];
        while (curr != null) {
            if (curr.hash == hash && (key == curr.key || (key != null && key.equals(curr.key)))) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    /**
     * Checks whether the map contains the specified key.
     *
     * @param key key whose presence is to be tested (may be null)
     * @return true if the map contains a mapping for the key
     */
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all mappings from this map. The capacity remains unchanged.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    /**
     * Returns a List of all keys in this map. Order is not guaranteed.
     *
     * @return List of keys
     */
    public List<K> keys() {
        List<K> keyList = new ArrayList<>(size);
        for (Node<K, V> bucket : table) {
            for (Node<K, V> node = bucket; node != null; node = node.next) {
                keyList.add(node.key);
            }
        }
        return keyList;
    }

    /**
     * Returns a List of all values in this map. Order corresponds to keys() order.
     *
     * @return List of values
     */
    public List<V> values() {
        List<V> valueList = new ArrayList<>(size);
        for (Node<K, V> bucket : table) {
            for (Node<K, V> node = bucket; node != null; node = node.next) {
                valueList.add(node.value);
            }
        }
        return valueList;
    }

    /** 
     * Doubles the capacity of the table and rehashes all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1; // double capacity
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        // Rehash all nodes into new table
        for (Node<K, V> headNode : oldTable) {
            while (headNode != null) {
                Node<K, V> nextNode = headNode.next;
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
        HashMap<String, Integer> map = new HashMap<>();

        System.out.println("Putting entries:");
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        System.out.println("Size after inserts: " + map.size()); // 3

        System.out.println("\nRetrieving values:");
        System.out.println("apple -> " + map.get("apple"));   // 1
        System.out.println("banana -> " + map.get("banana")); // 2
        System.out.println("grape -> " + map.get("grape"));   // null

        System.out.println("\nUpdating 'banana' to 20:");
        map.put("banana", 20);
        System.out.println("banana -> " + map.get("banana")); // 20

        System.out.println("\nContains key 'cherry'? " + map.containsKey("cherry")); // true
        System.out.println("Contains key 'date'? " + map.containsKey("date"));       // false

        System.out.println("\nRemoving 'apple':");
        System.out.println("Removed value: " + map.remove("apple")); // 1
        System.out.println("Contains key 'apple'? " + map.containsKey("apple"));    // false
        System.out.println("Size after removal: " + map.size());                     // 2

        System.out.println("\nIterating over keys and values:");
        for (String key : map.keys()) {
            System.out.println(key + " => " + map.get(key));
        }

        System.out.println("\nFilling map to trigger resize:");
        for (int i = 0; i < 20; i++) {
            map.put("key" + i, i);
        }
        System.out.println("Size after bulk insert: " + map.size()); // 22 (2 existing + 20 new)
        System.out.println("Capacity after resize (approx): " + map.table.length);

        System.out.println("\nClearing map:");
        map.clear();
        System.out.println("Size after clear: " + map.size()); // 0
        System.out.println("Is empty? " + map.isEmpty());     // true
    }
}
```

---

### Explanation of Key Points

1. **Generic Type Parameters `<K, V>`**
   We allow any object types for keys and values. Keys must correctly implement `hashCode()` and `equals()` so that different instances that should be treated as the same key do actually collide/compare equal.

2. **Buckets & Separate Chaining**
   Internally, we use an array of `Node<K, V>[] table`. Each entry in that array is the head of a singly linked list (chain) of `Node<K, V>`. If two keys hash to the same bucket index, they are stored in that bucket’s linked list.

3. **Hash Computation & Indexing**
   We compute an integer `hash` from `key.hashCode()`, then “spread” its bits via `h ^ (h >>> 16)` (borrowed from Java’s own HashMap) to reduce clustering. The actual bucket index is `hash & (table.length – 1)`. Because `table.length` is always a power of two, the bitmask operation efficiently maps the hash onto the correct range `[0, table.length - 1]`.

4. **Load Factor & Resizing**

   * We track `size` (number of key-value pairs) and `threshold` = `capacity * loadFactor`.
   * Whenever `size > threshold`, we “resize”—double the bucket array’s length and rehash all existing nodes into the new table.
   * By default, `loadFactor = 0.75`. You can override this in the constructor if you want a different threshold.

5. **Put / Get / Remove**

   * **put(K key, V value):** Compute `hash(key)`, find bucket index, traverse that list to see if the key already exists (if so, replace its value). Otherwise, insert a new `Node` at the head of the list. Increment `size` and, if necessary, call `resize()`. Returns the old value if replaced, or `null` if it was a new key.
   * **get(Object key):** Compute hash and bucket index, then traverse that bucket’s list to find a matching key. Return the associated value, or `null` if not found.
   * **remove(Object key):** Similarly locate the node; if found, unlink it from the list, decrement `size`, and return its old value. If not found, return `null`.
   * **containsKey(Object key):** Just calls `get(key)` and checks for non-`null`.

6. **Utility Methods**

   * **size()**, **isEmpty()**, **clear()** (nulls out all buckets but keeps the same capacity), **keys()**, and **values()**.
   * `keys()` and `values()` gather all entries in arbitrary order by scanning every bucket’s linked list.

7. **Amortized Complexity**

   * Thanks to resizing, the average cost of `put`, `get`, and `remove` remains $O(1)$. In the worst case (all keys collide), each operation degrades to $O(n)$, but with a good hash function and load factor, that rarely happens.

8. **Example `main(...)`**
   Demonstrates basic insertion, lookup, update, removal, iteration over keys, bulk insertion (triggering a resize), and clearing.

You can place this file as `HashMap.java` in your `data_structures/` directory. It compiles under Java 8+ (or any modern Java release). From other classes, simply do:

```java
import data_structures.HashMap;

HashMap<String, Integer> myMap = new HashMap<>();
myMap.put("hello", 123);
Integer val = myMap.get("hello");
```

to use it. Feel free to extend—for example, by adding key‐set or entry‐set iterators, customizing the load factor, or switching to a different collision resolution strategy if desired.
