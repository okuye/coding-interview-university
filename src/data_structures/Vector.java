```java
package data_structures;

import java.util.Arrays;

/**
 * A random access, variable-size list data structure that allows elements to be added or removed.
 * Internally uses a dynamically resizing array (similar to java.util.Vector/ArrayList).
 *
 * Complexity (amortized for resizable array):
 * 
 *   Operation           Average        Worst Case
 *   add(element)        O(1)           O(n)       (when resizing occurs)
 *   add(index, element) O(n)           O(n)
 *   remove(index)       O(n)           O(n)
 *   get(index)          O(1)           O(1)
 *   set(index, element) O(1)           O(1)
 *   size()              O(1)           O(1)
 *   isEmpty()           O(1)           O(1)
 *   clear()             O(n)           O(n)       (nulling references for GC)
 *
 * @param <T> the type of elements stored in the Vector
 */
public class Vector<T> {
    /** Default initial capacity of the underlying array. */
    private static final int DEFAULT_CAPACITY = 10;
    /** Underlying array storing the elements. */
    private T[] elements;
    /** Number of elements currently in the Vector. */
    private int size;

    /**
     * Constructs an empty Vector with default initial capacity.
     */
    @SuppressWarnings("unchecked")
    public Vector() {
        // We can't create a generic array directly; use Object[] and cast.
        this.elements = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Constructs an empty Vector with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the Vector
     * @throws IllegalArgumentException if initialCapacity is negative
     */
    @SuppressWarnings("unchecked")
    public Vector(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be non-negative: " + initialCapacity);
        }
        this.elements = (T[]) new Object[Math.max(initialCapacity, DEFAULT_CAPACITY)];
        this.size = 0;
    }

    /**
     * Returns the number of elements in this Vector.
     *
     * @return the size of the Vector
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this Vector contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Ensures that the Vector can hold at least the specified number of elements
     * without further resizing.
     *
     * @param minCapacity the desired minimum capacity
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1); // 1.5× growth
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /**
     * Adds the specified element to the end of this Vector.
     *
     * @param value the element to add
     * @return true (as specified by Collection.add)
     */
    public boolean add(T value) {
        ensureCapacity(size + 1);
        elements[size++] = value;
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this Vector.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right.
     *
     * @param index index at which to insert the element (0 ≤ index ≤ size)
     * @param value the element to insert
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void add(int index, T value) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        // Shift elements to the right from index onward
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        size++;
    }

    /**
     * Returns the element at the specified position in this Vector.
     *
     * @param index index of the element to return (0 ≤ index < size)
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public T get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    /**
     * Replaces the element at the specified position in this Vector with the specified element.
     *
     * @param index index of the element to replace (0 ≤ index < size)
     * @param value element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public T set(int index, T value) {
        rangeCheck(index);
        T oldValue = elements[index];
        elements[index] = value;
        return oldValue;
    }

    /**
     * Removes the element at the specified position in this Vector. Shifts any subsequent elements to the left.
     *
     * @param index the index of the element to be removed (0 ≤ index < size)
     * @return the element that was removed
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public T remove(int index) {
        rangeCheck(index);
        T oldValue = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // help garbage collection
        return oldValue;
    }

    /**
     * Removes the first occurrence of the specified element from this Vector, if it is present.
     *
     * @param value element to be removed from this Vector, if present
     * @return true if the Vector contained the specified element
     */
    public boolean remove(Object value) {
        for (int i = 0; i < size; i++) {
            if ((value == null && elements[i] == null) || (value != null && value.equals(elements[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this Vector contains the specified element.
     *
     * @param value element whose presence in this Vector is to be tested
     * @return true if this Vector contains the element
     */
    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this Vector,
     * or -1 if this Vector does not contain the element.
     *
     * @param value element to search for
     * @return the index of the first occurrence, or -1 if not found
     */
    public int indexOf(Object value) {
        for (int i = 0; i < size; i++) {
            if ((value == null && elements[i] == null) || (value != null && value.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes all of the elements from this Vector. The Vector will be empty after this call returns.
     */
    public void clear() {
        // Null out references for garbage collection
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Returns a shallow copy of this Vector instance. (Elements themselves are not cloned.)
     *
     * @return a new Vector containing the same elements in the same order
     */
    @SuppressWarnings("unchecked")
    @Override
    public Vector<T> clone() {
        try {
            Vector<T> cloned = (Vector<T>) super.clone();
            cloned.elements = Arrays.copyOf(this.elements, this.elements.length);
            // size is a primitive; it's copied by default
            return cloned;
        } catch (CloneNotSupportedException e) {
            // Should not happen, since we implement Cloneable by default via Object
            throw new InternalError(e);
        }
    }

    /**
     * Returns an array containing all of the elements in this Vector in proper sequence (from first to last element).
     *
     * @return an array containing all elements of this Vector
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Returns a string representation of the Vector in the form [a, b, c, ...].
     *
     * @return string representation
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /** Checks if the given index is in range for get/set/remove (0 ≤ index < size). */
    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /** Checks if the given index is in range for add (0 ≤ index ≤ size). */
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();

        System.out.println("Add elements: A, B, C");
        vector.add("A");
        vector.add("B");
        vector.add("C");
        System.out.println("Vector: " + vector);       // [A, B, C]
        System.out.println("Size: " + vector.size());  // 3
        System.out.println("Get index 1: " + vector.get(1)); // B

        System.out.println("\nAdd at index 1: D");
        vector.add(1, "D");
        System.out.println("Vector: " + vector);       // [A, D, B, C]

        System.out.println("\nSet index 2 to E");
        String old = vector.set(2, "E");
        System.out.println("Replaced '" + old + "' with 'E'");
        System.out.println("Vector: " + vector);       // [A, D, E, C]

        System.out.println("\nContains 'B'? " + vector.contains("B"));   // false
        System.out.println("Index of 'C': " + vector.indexOf("C"));     // 3

        System.out.println("\nRemove index 1: " + vector.remove(1));     // D
        System.out.println("Vector: " + vector);       // [A, E, C]

        System.out.println("\nRemove element 'E': " + vector.remove("E")); // true
        System.out.println("Vector: " + vector);         // [A, C]

        System.out.println("\nConvert to array:");
        Object[] arr = vector.toArray();
        System.out.println(Arrays.toString(arr));        // [A, C]

        System.out.println("\nClear vector");
        vector.clear();
        System.out.println("Is empty? " + vector.isEmpty()); // true
        System.out.println("Vector: " + vector);             // []
    }
}
```
