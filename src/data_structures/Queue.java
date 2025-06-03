```java
package data_structures;

/**
 * An abstract data type or collection in which the entities in the collection are kept in order and the principle
 * (or only) operations on the collection are the addition of entities to the rear terminal position, known as
 * enqueue, and removal of entities from the front terminal position, known as dequeue.
 * This makes the queue a First-In-First-Out (FIFO) data structure.
 *
 * Complexity:
 *   enqueue:    O(1)
 *   dequeue:    O(1)
 *   peek:       O(1)
 *   isEmpty:    O(1)
 *   size:       O(1)
 *
 * @param <T> the type of elements stored in the queue
 */
public class Queue<T> {

    /**
     * Inner node class for singly linked list.
     */
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    /** Reference to the head (front) node. */
    private Node<T> head;
    /** Reference to the tail (rear) node. */
    private Node<T> tail;
    /** Number of elements in the queue. */
    private int size;

    /** Constructs an empty queue. */
    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Inserts the specified element into the rear of this queue.
     *
     * @param value the element to enqueue
     */
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * @return the element at the front, or null if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T result = head.data;
        head = head.next;
        size--;
        if (head == null) {
            // Queue is now empty, so tail must also be null
            tail = null;
        }
        return result;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * @return the element at the front, or null if the queue is empty
     */
    public T peek() {
        return isEmpty() ? null : head.data;
    }

    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Removes all elements from this queue. After calling this, size() returns 0.
     */
    public void clear() {
        // Help garbage collection by unlinking all nodes
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
     * Returns a string representation of the queue in the form [a, b, c, ...],
     * where 'a' is the front element and 'c' is the rear.
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
        Queue<Integer> queue = new Queue<>();

        System.out.println("Enqueue 10, 20, 30:");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Queue: " + queue);       // [10, 20, 30]
        System.out.println("Size: " + queue.size()); // 3
        System.out.println("Peek: " + queue.peek()); // 10

        System.out.println("\nDequeue twice:");
        System.out.println("Dequeued: " + queue.dequeue()); // 10
        System.out.println("Dequeued: " + queue.dequeue()); // 20
        System.out.println("Queue now: " + queue);          // [30]
        System.out.println("Size: " + queue.size());        // 1
        System.out.println("Peek now: " + queue.peek());    // 30

        System.out.println("\nIsEmpty? " + queue.isEmpty()); // false
        System.out.println("\nDequeue last element:");
        System.out.println("Dequeued: " + queue.dequeue());  // 30
        System.out.println("IsEmpty? " + queue.isEmpty());   // true
        System.out.println("Dequeue on empty: " + queue.dequeue()); // null

        System.out.println("\nEnqueue 40, 50:");
        queue.enqueue(40);
        queue.enqueue(50);
        System.out.println("Queue: " + queue); // [40, 50]
        System.out.println("\nClear queue");
        queue.clear();
        System.out.println("After clear, isEmpty? " + queue.isEmpty()); // true
        System.out.println("Queue: " + queue);                           // []
    }
}
```
