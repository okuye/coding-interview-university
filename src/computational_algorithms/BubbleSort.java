package computational_algorithms;


import java.util.Arrays;

/**
 * Simple sorting algorithm that repeatedly steps through the list to be sorted, compares each pair of adjacent items and swaps them if they are in the wrong order.
 * The pass through the list is repeated until no swaps are needed, which indicates that the list is sorted
 */
public class BubbleSort {

    /**
     * Perform a bubble sort
     *
     * @param array
     * @return the array sorted by ascending order
     */
    static <T extends Comparable<T>> void sort(T[] array) {
        long startTime = System.nanoTime();

        if (array == null || array.length <= 1) {
            return;
        }

        for (int i = 0; i < array.length; i++) {
            boolean swapped = false;
            for (int j = 1; j < array.length - i; j++) {
                if (array[j - 1].compareTo(array[j]) > 0) {
                    T temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "BubbleSort", duration + " ns", Arrays.toString(array));
    }
}