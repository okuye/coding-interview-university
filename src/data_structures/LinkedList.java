```java
package data_structures;

/**
 * A linear collection of data elements, in which linear order is not given by their physical placement in memory.
 * Instead, each element points to the next. It is a data structure consisting of a group of nodes which together
 * represent a sequence. Under the simplest form, each node is composed of data and a reference (in other words,
 * a link) to the next node in the sequence.
 *
 * Complexity (for a singly linked list with head and tail pointers):
 *
 *   Operation        Worst Case
 *   addFirst         O(1)
 *   addLast          O(1)
 *   removeFirst      O(1)
 *   removeLast       O(n)    (must traverse to find the node before tail)
 *   contains/search  O(n)
 *   remove(value)    O(n)
 *   size             O(1)    (if we keep a counter)
 *   isEmpty          O(1)
 *   get(index)       O(n)
 *
 * @param <T> the type of elements stored in the list
 */
public class LinkedList<T> {

    /**
     * Inner class representing a node in the linked list.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /** Reference to the first node (head) of the list. */
    private Node<T> head;
    /** Reference to the last node (tail) of the list. */
    private Node<T> tail;
    /** Number of elements currently in the list. */
    private int size;

    /** Constructs an empty list. */
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Inserts an element at the front of the list.
     *
     * @param value the element to add
     */
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;
        if (tail == null) {
            // List was empty, so tail should also point to the new node
            tail = newNode;
        }
        size++;
    }

    /**
     * Inserts an element at the end of the list.
     *
     * @param value the element to add
     */
    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);
        if (isEmpty()) {
            // If the list is empty, head and tail both point to new node
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the first element of the list.
     *
     * @return the removed element, or null if the list is empty
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;  // nothing to remove
        }
        T removedData = head.data;
        head = head.next;
        size--;
        if (head == null) {
            // List became empty, so tail must also become null
            tail = null;
        }
        return removedData;
    }

    /**
     * Removes and returns the last element of the list.
     *
     * @return the removed element, or null if the list is empty
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (head == tail) {
            // Only one element in the list
            T removedData = head.data;
            head = null;
            tail = null;
            size = 0;
            return removedData;
        }
        // Traverse to the node just before tail
        Node<T> current = head;
        while (current.next != tail) {
            current = current.next;
        }
        T removedData = tail.data;
        current.next = null;
        tail = current;
        size--;
        return removedData;
    }

    /**
     * Returns (but does not remove) the first element.
     *
     * @return the first element, or null if empty
     */
    public T peekFirst() {
        return isEmpty() ? null : head.data;
    }

    /**
     * Returns (but does not remove) the last element.
     *
     * @return the last element, or null if empty
     */
    public T peekLast() {
        return isEmpty() ? null : tail.data;
    }

    /**
     * Returns true if the list contains the specified value.
     *
     * @param value the value to search for
     * @return true if found, false otherwise
     */
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    /**
     * Returns the index of the first occurrence of the specified value,
     * or -1 if the list does not contain the value.
     *
     * @param value the value to search for
     * @return index of the value, or -1 if not found
     */
    public int indexOf(T value) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if ( (current.data == null && value == null) ||
                 (current.data != null && current.data.equals(value)) ) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Removes the first occurrence of the specified value from the list.
     *
     * @param value the value to remove
     * @return true if an element was removed, false if not found
     */
    public boolean remove(T value) {
        if (isEmpty()) {
            return false;
        }
        // Special case: removing head
        if ( (head.data == null && value == null) ||
             (head.data != null && head.data.equals(value)) ) {
            removeFirst();
            return true;
        }
        // Traverse, keeping track of previous node
        Node<T> prev = head;
        Node<T> current = head.next;
        while (current != null) {
            if ( (current.data == null && value == null) ||
                 (current.data != null && current.data.equals(value)) ) {
                prev.next = current.next;
                if (current == tail) {
                    // If removing the tail, update tail pointer
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the element at the specified index (0-based).
     *
     * @param index 0-based position
     * @return the element, or throws IndexOutOfBoundsException if index < 0 or >= size
     */
    public T get(int index) {
        checkElementIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Inserts the specified value at the given index (0-based).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right.
     *
     * @param index 0-based position where to insert (0 ≤ index ≤ size)
     * @param value the element to insert
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size) {
            addLast(value);
            return;
        }
        Node<T> newNode = new Node<>(value);
        Node<T> current = head;
        // Traverse to node just before the insertion point
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    /**
     * Removes the element at the specified index and returns it.
     *
     * @param index 0-based position to remove (0 ≤ index < size)
     * @return the removed element
     * @throws IndexOutOfBoundsException if index < 0 or index ≥ size
     */
    public T removeAt(int index) {
        checkElementIndex(index);
        if (index == 0) {
            return removeFirst();
        }
        Node<T> prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
        }
        Node<T> toRemove = prev.next;
        prev.next = toRemove.next;
        if (toRemove == tail) {
            tail = prev;
        }
        size--;
        return toRemove.data;
    }

    /** Helper to check if index is in [0, size-1]. */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if the list contains no elements.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the list. After calling this, size() returns 0.
     */
    public void clear() {
        // Help GC by unlinking all nodes
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.data = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns a string representation of the list in the form [a, b, c, ...].
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            current = current.next;
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();

        System.out.println("Add at end: 10, 20, 30");
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        System.out.println("List: " + list);        // [10, 20, 30]
        System.out.println("Size: " + list.size()); // 3

        System.out.println("\nAdd at front: 5");
        list.addFirst(5);
        System.out.println("List: " + list);        // [5, 10, 20, 30]

        System.out.println("\nAdd at index 2: 15");
        list.add(2, 15);
        System.out.println("List: " + list);        // [5, 10, 15, 20, 30]

        System.out.println("\nGet element at index 3: " + list.get(3)); // 20

        System.out.println("\nContains 20? " + list.contains(20));       // true
        System.out.println("Index of 20: " + list.indexOf(20));           // 3
        System.out.println("Contains 100? " + list.contains(100));        // false

        System.out.println("\nRemove first: " + list.removeFirst());      // 5
        System.out.println("Remove last: " + list.removeLast());          // 30
        System.out.println("List now: " + list);                           // [10, 15, 20]
        System.out.println("Size: " + list.size());                        // 3

        System.out.println("\nRemove value 15: " + list.remove((Integer)15)); // true
        System.out.println("List now: " + list);                               // [10, 20]
        System.out.println("Remove value 100 (not in list): " + list.remove((Integer)100)); // false

        System.out.println("\nRemove at index 1: " + list.removeAt(1));    // 20
        System.out.println("List now: " + list);                           // [10]
        System.out.println("Size: " + list.size());                        // 1

        System.out.println("\nClear list");
        list.clear();
        System.out.println("List after clear: " + list); // []
        System.out.println("Is empty? " + list.isEmpty()); // true
    }
}
```
