```java
package data_structures;

/**
 * An abstract data type that serves as a collection of elements, with two principal operations:
 * - push, which adds an element to the collection, and
 * - pop, which removes the most recently added element that was not yet removed.
 *
 * The order in which elements come off a stack gives rise to its alternative name, LIFO (last in, first out).
 * Additionally, a peek operation may give access to the top without modifying the stack.
 *
 * Complexity:
 *   Space:       O(n)
 *   push:        O(1)
 *   pop:         O(1)
 *   peek:        O(1)
 *   isEmpty:     O(1)
 *   size:        O(1)
 *
 * @param <T> the type of elements stored in the stack
 */
public class Stack<T> {

    /**
     * Internal node class representing an element in the stack.
     */
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    /** Reference to the top node of the stack. */
    private Node<T> top;
    /** Number of elements currently in the stack. */
    private int size;

    /** Constructs an empty stack. */
    public Stack() {
        top = null;
        size = 0;
    }

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param value the element to push
     */
    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Removes and returns the element at the top of the stack.
     *
     * @return the element that was on top, or null if the stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T value = top.data;
        top = top.next;
        size--;
        return value;
    }

    /**
     * Returns (but does not remove) the element at the top of the stack.
     *
     * @return the element on top, or null if the stack is empty
     */
    public T peek() {
        return isEmpty() ? null : top.data;
    }

    /**
     * Returns true if this stack contains no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this stack.
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Removes all elements from this stack. After calling this, size() returns 0.
     */
    public void clear() {
        while (top != null) {
            Node<T> nextNode = top.next;
            top.data = null;
            top.next = null;
            top = nextNode;
        }
        size = 0;
    }

    /**
     * Returns a string representation of the stack in the form [top, ..., bottom].
     * The first element shown is the top of the stack.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = top;
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
        Stack<Integer> stack = new Stack<>();

        System.out.println("Push 10, 20, 30 onto the stack:");
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack: " + stack);       // [30, 20, 10]
        System.out.println("Size: " + stack.size()); // 3
        System.out.println("Peek: " + stack.peek()); // 30

        System.out.println("\nPop twice:");
        System.out.println("Popped: " + stack.pop()); // 30
        System.out.println("Popped: " + stack.pop()); // 20
        System.out.println("Stack now: " + stack);    // [10]
        System.out.println("Size: " + stack.size());  // 1
        System.out.println("Peek now: " + stack.peek()); // 10

        System.out.println("\nIsEmpty? " + stack.isEmpty()); // false
        System.out.println("\nPop last element:");
        System.out.println("Popped: " + stack.pop());    // 10
        System.out.println("IsEmpty? " + stack.isEmpty()); // true
        System.out.println("Pop on empty: " + stack.pop()); // null

        System.out.println("\nPush 40, 50:");
        stack.push(40);
        stack.push(50);
        System.out.println("Stack: " + stack); // [50, 40]
        System.out.println("\nClear stack");
        stack.clear();
        System.out.println("After clear, isEmpty? " + stack.isEmpty()); // true
        System.out.println("Stack: " + stack);                         // []
    }
}
```
