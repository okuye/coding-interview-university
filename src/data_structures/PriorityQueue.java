```java
package data_structures;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract data type which is like a regular queue or stack data structure, but where additionally each element has a
 * "priority" associated with it. In a priority queue, an element with high priority is served before an element with low
 * priority. If two elements have the same priority, they are served according to their order in the queue (i.e., FIFO
 * among equals).
 *
 * This implementation uses a binary heap (min-heap) under the hood, augmented with an insertion counter to ensure
 * stability among elements with equal priority.
 *
 * Complexity:
 *   Space:             O(n)
 *   offer/enqueue:     O(log n)
 *   poll/dequeue:      O(log n)
 *   peek:              O(1)
 *   isEmpty/size:      O(1)
 *
 * @param <T> the type of elements stored in the priority queue
 */
public class PriorityQueue<T> {

    /**
     * Internal node wrapping an element with its priority and insertion order.
     */
    private static class Node<E> {
        final E element;
        final int priority;
        final long insertionOrder;

        Node(E element, int priority, long insertionOrder) {
            this.element = element;
            this.priority = priority;
            this.insertionOrder = insertionOrder;
        }
    }

    /** List-based binary heap. heap.get(0) is the root. */
    private final List<Node<T>> heap;
    /** Monotonically increasing counter to break ties among equal-priority elements. */
    private long insertionCounter;

    /** Constructs an empty priority queue. */
    public PriorityQueue() {
        this.heap = new ArrayList<>();
        this.insertionCounter = 0;
    }

    /**
     * Inserts the specified element with the given priority into this priority queue.
     *
     * @param element  the element to add (may be null)
     * @param priority the priority of the element; lower values indicate higher priority
     */
    public void offer(T element, int priority) {
        Node<T> node = new Node<>(element, priority, insertionCounter++);
        heap.add(node);
        heapifyUp(heap.size() - 1);
    }

    /**
     * Retrieves and removes the head of this queue (the element with highest priority, where smaller priority value is
     * considered higher priority). If two elements share the same priority, the one that was inserted earlier is returned first.
     *
     * @return the element with highest priority, or null if the queue is empty
     */
    public T poll() {
        if (heap.isEmpty()) {
            return null;
        }
        Node<T> rootNode = heap.get(0);
        Node<T> lastNode = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            heapifyDown(0);
        }
        return rootNode.element;
    }

    /**
     * Retrieves, but does not remove, the head of this queue (the element with highest priority). If two elements share
     * the same priority, the one that was inserted earlier is returned first.
     *
     * @return the element with highest priority, or null if the queue is empty
     */
    public T peek() {
        return heap.isEmpty() ? null : heap.get(0).element;
    }

    /**
     * Returns the number of elements in this priority queue.
     *
     * @return the size
     */
    public int size() {
        return heap.size();
    }

    /**
     * Returns true if this priority queue contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /** Removes all elements from this priority queue. */
    public void clear() {
        heap.clear();
    }

    /** Restores heap property by moving the node at index up. */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIdx = (index - 1) / 2;
            if (compare(heap.get(index), heap.get(parentIdx)) < 0) {
                swap(index, parentIdx);
                index = parentIdx;
            } else {
                break;
            }
        }
    }

    /** Restores heap property by moving the node at index down. */
    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int leftChildIdx = 2 * index + 1;
            int rightChildIdx = 2 * index + 2;
            int smallest = index;

            if (leftChildIdx < size && compare(heap.get(leftChildIdx), heap.get(smallest)) < 0) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < size && compare(heap.get(rightChildIdx), heap.get(smallest)) < 0) {
                smallest = rightChildIdx;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    /**
     * Compares two nodes first by priority, then by insertionOrder to ensure stability.
     *
     * @return negative if a < b, zero if equal, positive if a > b
     */
    private int compare(Node<T> a, Node<T> b) {
        int prioCmp = Integer.compare(a.priority, b.priority);
        if (prioCmp != 0) {
            return prioCmp;
        }
        return Long.compare(a.insertionOrder, b.insertionOrder);
    }

    /** Swaps two nodes in the heap list. */
    private void swap(int i, int j) {
        Node<T> tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        PriorityQueue<String> pq = new PriorityQueue<>();

        System.out.println("Offer elements with various priorities:");
        pq.offer("task-low-1", 5);
        pq.offer("task-high-1", 1);
        pq.offer("task-medium", 3);
        pq.offer("task-high-2", 1);
        pq.offer("task-low-2", 5);

        System.out.println("Size after offers: " + pq.size()); // 5
        System.out.println("Peek (should be first '1'-priority): " + pq.peek()); // "task-high-1"

        System.out.println("\nPolling all elements in order:");
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        // Expected order:
        //   task-high-1   (priority 1, inserted before task-high-2)
        //   task-high-2   (priority 1)
        //   task-medium   (priority 3)
        //   task-low-1    (priority 5, inserted before task-low-2)
        //   task-low-2    (priority 5)

        System.out.println("\nConfirm queue is empty? " + pq.isEmpty());

        System.out.println("\nAdd elements again and demonstrate clear():");
        pq.offer("A", 2);
        pq.offer("B", 2);
        System.out.println("Size before clear: " + pq.size()); // 2
        pq.clear();
        System.out.println("Size after clear: " + pq.size()); // 0
        System.out.println("Peek after clear (should be null): " + pq.peek());
    }
}
```
