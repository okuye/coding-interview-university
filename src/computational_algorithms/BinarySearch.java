```java
package computational_algorithms;

/**
 * Search algorithm that finds the position of a target value within a sorted array.
 *
 * Binary search compares the target value to the middle element of the array; if they are unequal,
 * the half in which the target cannot lie is eliminated and the search continues on the remaining
 * half until it is successful. If the search ends with the remaining half being empty, the target
 * is not in the array.
 *
 * Complexity:
 *   Time:  O(log n)
 *   Space: O(1) for iterative, O(log n) for recursive (due to call stack)
 */
public class BinarySearch {

    /**
     * Performs binary search on a sorted array of Comparable elements.
     *
     * @param <T>    the element type, which must implement Comparable<T>
     * @param array  a sorted array of T in ascending order
     * @param target the element to search for
     * @return the index of target in array if present; otherwise, -1
     */
    public static <T extends Comparable<T>> int binarySearch(T[] array, T target) {
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >>> 1);
            T midVal = array[mid];
            int cmp = midVal.compareTo(target);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // target found
            }
        }
        return -1; // target not found
    }

    /**
     * Recursive version of binary search on a sorted array of Comparable elements.
     *
     * @param <T>    the element type, which must implement Comparable<T>
     * @param array  a sorted array of T in ascending order
     * @param target the element to search for
     * @return the index of target in array if present; otherwise, -1
     */
    public static <T extends Comparable<T>> int binarySearchRecursive(T[] array, T target) {
        return binarySearchRecursive(array, target, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> int binarySearchRecursive(T[] array, T target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + ((high - low) >>> 1);
        T midVal = array[mid];
        int cmp = midVal.compareTo(target);

        if (cmp < 0) {
            return binarySearchRecursive(array, target, mid + 1, high);
        } else if (cmp > 0) {
            return binarySearchRecursive(array, target, low, mid - 1);
        } else {
            return mid;
        }
    }

    /**
     * Overload for primitive int arrays.
     *
     * @param array  a sorted array of ints in ascending order
     * @param target the int to search for
     * @return the index of target in array if present; otherwise, -1
     */
    public static int binarySearch(int[] array, int target) {
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >>> 1);
            int midVal = array[mid];

            if (midVal < target) {
                low = mid + 1;
            } else if (midVal > target) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * Recursive overload for primitive int arrays.
     *
     * @param array  a sorted array of ints in ascending order
     * @param target the int to search for
     * @return the index of target in array if present; otherwise, -1
     */
    public static int binarySearchRecursive(int[] array, int target) {
        return binarySearchRecursive(array, target, 0, array.length - 1);
    }

    private static int binarySearchRecursive(int[] array, int target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + ((high - low) >>> 1);
        int midVal = array[mid];

        if (midVal < target) {
            return binarySearchRecursive(array, target, mid + 1, high);
        } else if (midVal > target) {
            return binarySearchRecursive(array, target, low, mid - 1);
        } else {
            return mid;
        }
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Example with Integer array
        Integer[] sortedInts = {1, 3, 5, 7, 9, 11, 13};
        System.out.println("Iterative search for 7: " + binarySearch(sortedInts, 7));      // 3
        System.out.println("Iterative search for 4: " + binarySearch(sortedInts, 4));      // -1
        System.out.println("Recursive search for 11: " + binarySearchRecursive(sortedInts, 11)); // 5
        System.out.println("Recursive search for 2: " + binarySearchRecursive(sortedInts, 2));   // -1

        // Example with String array
        String[] sortedStrings = {"apple", "banana", "cherry", "date", "fig", "grape"};
        System.out.println("Search for \"date\": " + binarySearch(sortedStrings, "date")); // 3
        System.out.println("Search for \"kiwi\": " + binarySearch(sortedStrings, "kiwi")); // -1

        // Example with primitive int array
        int[] sortedPrimitives = {2, 4, 6, 8, 10, 12, 14};
        System.out.println("Iterative search for 10: " + binarySearch(sortedPrimitives, 10));      // 4
        System.out.println("Iterative search for 5: " + binarySearch(sortedPrimitives, 5));        // -1
        System.out.println("Recursive search for 14: " + binarySearchRecursive(sortedPrimitives, 14)); // 6
        System.out.println("Recursive search for 1: " + binarySearchRecursive(sortedPrimitives, 1));   // -1
    }
}
```
