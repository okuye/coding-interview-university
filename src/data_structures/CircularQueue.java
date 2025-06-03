Below is a simple fixed‐size circular queue implementation in Java, with all the usual operations (enqueue, dequeue, peek, isEmpty, isFull, size). It uses a single array internally and treats it as circular by wrapping head/tail indices around when they reach the end.

```java
package data_structures;

/**
 * A linear data structure in which the operations are performed based on FIFO (First In First Out) principle
 * and the last position is connected back to the first position to make a circle.
 * It uses a single, fixed-size buffer as if it were connected end-to-end.
 *
 * Complexity (for all operations below):
 *   Space:  O(capacity)
 *   enqueue: O(1)
 *   dequeue: O(1)
 *   peek:    O(1)
 *   isEmpty: O(1)
 *   isFull:  O(1)
 *   size:    O(1)
 *
 * @param <T> the type of elements stored in the queue
 */
public class CircularQueue<T> {
    private T[] buffer;      // underlying array storage
    private int head;        // index of the element at the front (to dequeue/peek)
    private int tail;        // index where the next element will be enqueued
    private int count;       // number of elements currently in the queue
    private int capacity;    // maximum number of elements the queue can hold

    /**
     * Constructs a circular queue with the specified capacity.
     *
     * @param capacity the maximum number of elements this queue can hold
     * @throws IllegalArgumentException if capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    /**
     * Inserts the specified element into the queue if there is room.
     *
     * @param item the element to add
     * @return true if the element was added, false if the queue is full
     */
    public boolean enqueue(T item) {
        if (isFull()) {
            return false;  // cannot enqueue because the queue is full
        }
        buffer[tail] = item;
        // advance tail circularly
        tail = (tail + 1) % capacity;
        count++;
        return true;
    }

    /**
     * Retrieves and removes the head of the queue.
     *
     * @return the element at the front, or null if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;  // nothing to dequeue
        }
        T item = buffer[head];
        buffer[head] = null;  // help garbage collection
        // advance head circularly
        head = (head + 1) % capacity;
        count--;
        return item;
    }

    /**
     * Retrieves, but does not remove, the head of the queue.
     *
     * @return the element at the front, or null if the queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return buffer[head];
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue has no elements, false otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Checks whether the queue is full.
     *
     * @return true if the queue has reached its capacity, false otherwise
     */
    public boolean isFull() {
        return count == capacity;
    }

    /**
     * Returns the number of elements currently in the queue.
     *
     * @return the current size of the queue
     */
    public int size() {
        return count;
    }

    /**
     * Returns the maximum capacity of this queue.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Clears the queue of all elements.
     * Resets head, tail, and count.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        // Null out references to help garbage collection
        for (int i = 0; i < capacity; i++) {
            buffer[i] = null;
        }
        head = 0;
        tail = 0;
        count = 0;
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        CircularQueue<Integer> queue = new CircularQueue<>(5);

        System.out.println("Enqueue 1,2,3:");
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println("Size: " + queue.size());     // 3
        System.out.println("Peek: " + queue.peek());     // 1

        System.out.println("\nDequeue twice:");
        System.out.println("Dequeued: " + queue.dequeue()); // 1
        System.out.println("Dequeued: " + queue.dequeue()); // 2
        System.out.println("Size after dequeues: " + queue.size()); // 1
        System.out.println("Peek now: " + queue.peek());           // 3

        System.out.println("\nEnqueue 4,5,6,7:");
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        boolean enqResult = queue.enqueue(7);  // should wrap around; capacity is 5, count was 1, so 4 more fits up to 5
        System.out.println("Tried to enqueue 7 (overflow): " + enqResult); // false, because queue is now full
        System.out.println("Queue is full? " + queue.isFull());            // true
        System.out.println("Size: " + queue.size());                        // 5 (max capacity)

        System.out.println("\nDequeuing all elements:");
        while (!queue.isEmpty()) {
            System.out.print(queue.dequeue() + " ");
        }
        System.out.println("\nAfter clearing, isEmpty? " + queue.isEmpty()); // true

        // Demonstrate clear()
        queue.enqueue(10);
        queue.enqueue(20);
        System.out.println("\nBefore clear, size: " + queue.size()); // 2
        queue.clear();
        System.out.println("After clear, size: " + queue.size());    // 0
        System.out.println("After clear, enqueue 30:");
        queue.enqueue(30);
        System.out.println("Peek: " + queue.peek());                  // 30
    }
}
```

---

### Explanation of Key Points

1. **Underlying Array & Circular Indices**

   * We allocate a fixed‐size array `buffer` of length `capacity`.
   * Two integer pointers, `head` and `tail`, track where to dequeue and where to enqueue, respectively.
   * Each time we enqueue, we store at `buffer[tail]` and then advance `tail = (tail + 1) % capacity`.
   * Each time we dequeue, we read from `buffer[head]`, set that slot to `null` (to aid garbage collection), and then advance `head = (head + 1) % capacity`.
   * Because of the modulo wrap, when `head` or `tail` reach `capacity - 1`, the next increment takes them back to index 0.

2. **Count vs. Capacity**

   * We keep an integer `count` to know how many elements are currently in the queue.
   * If `count == 0`, the queue is empty.
   * If `count == capacity`, the queue is full (cannot `enqueue` more).
   * The `isEmpty()` and `isFull()` checks are simply `count == 0` and `count == capacity`, respectively.

3. **Operations’ Complexity**

   * All operations—`enqueue`, `dequeue`, `peek`, `isEmpty`, `isFull`, and `size`—run in constant $O(1)$ time and use $O(1)$ extra space (besides the fixed-size array).
   * The overall space is $O(\text{capacity})$.

4. **Safety Checks**

   * The constructor throws `IllegalArgumentException` if someone tries to build a queue with non-positive capacity.
   * `enqueue` returns `false` instead of throwing if the queue is already full. You could alter this to throw an exception (e.g., `IllegalStateException`) if you prefer “fail fast.”
   * `dequeue` and `peek` return `null` when the queue is empty. If you prefer throwing an exception on underflow, you could change that behavior.

5. **Clear Method**

   * `clear()` nulls out all buffer slots, resets `head` & `tail` to 0, and sets `count = 0`. This helps ensure no stale references remain for garbage collection.

6. **Example `main(...)`**

   * Demonstrates enqueuing elements until capacity is reached, checking that overflow returns `false`, wrapping of indices around the array, and then dequeuing until empty.
   * Also shows usage of `clear()`.

Feel free to adjust/enhance—for instance, by throwing exceptions on overflow/underflow or by exposing iterators, but this should give a solid, working circular queue base.
