```java
package computational_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algorithm for taking in multiple (k) sorted lists and merging them into a single sorted list.
 *
 * This implementation uses a min‐heap (Java’s PriorityQueue) to achieve O(N log k) time complexity,
 * where N is the total number of elements across all lists and k is the number of lists.
 *
 * Complexity:
 *   - Time:  O(N log k) (N = total elements across all k lists)
 *   - Space: O(k) auxiliary (for the heap) plus O(N) for the output list
 *
 * @param <T> the type of elements in the lists; must implement Comparable<T>
 */
public class KWayMerge<T extends Comparable<T>> {

    /**
     * Internal helper to track an element in the heap along with which list it came from and its index within that list.
     */
    private static class Node<E extends Comparable<E>> implements Comparable<Node<E>> {
        final E value;
        final int listIndex;    // which of the k lists this value is from
        final int elementIndex; // index within that list

        Node(E value, int listIndex, int elementIndex) {
            this.value = value;
            this.listIndex = listIndex;
            this.elementIndex = elementIndex;
        }

        @Override
        public int compareTo(Node<E> other) {
            return this.value.compareTo(other.value);
        }
    }

    /**
     * Merges k sorted lists into one sorted list.
     *
     * @param sortedLists a List of k lists, each of which is already sorted in ascending order
     * @return a new List containing all elements from the input lists in ascending order
     * @throws IllegalArgumentException if sortedLists is null
     */
    public List<T> merge(List<List<T>> sortedLists) {
        if (sortedLists == null) {
            throw new IllegalArgumentException("Input list of lists cannot be null");
        }

        // Min‐heap of size at most k
        PriorityQueue<Node<T>> minHeap = new PriorityQueue<>();

        // Initialize the heap with the first element of each non‐empty list
        for (int i = 0; i < sortedLists.size(); i++) {
            List<T> lst = sortedLists.get(i);
            if (lst != null && !lst.isEmpty()) {
                minHeap.offer(new Node<>(lst.get(0), i, 0));
            }
        }

        List<T> result = new ArrayList<>();
        // Repeatedly extract the smallest element, then push the next element from that same list (if any)
        while (!minHeap.isEmpty()) {
            Node<T> node = minHeap.poll();
            result.add(node.value);

            int nextIdx = node.elementIndex + 1;
            List<T> originList = sortedLists.get(node.listIndex);
            if (nextIdx < originList.size()) {
                minHeap.offer(new Node<>(originList.get(nextIdx), node.listIndex, nextIdx));
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Example: merge 3 sorted integer lists
        List<Integer> list1 = List.of(1, 4, 7, 10);
        List<Integer> list2 = List.of(2, 5, 8, 11, 14);
        List<Integer> list3 = List.of(0, 3, 6, 9, 12, 15);

        List<List<Integer>> allLists = new ArrayList<>();
        allLists.add(list1);
        allLists.add(list2);
        allLists.add(list3);

        KWayMerge<Integer> merger = new KWayMerge<>();
        List<Integer> merged = merger.merge(allLists);

        System.out.println("Merged result: " + merged);
        // Expected output:
        // Merged result: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15]
    }
}
```
