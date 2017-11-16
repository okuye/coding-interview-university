package computational_algorithms;


import java.util.Arrays;

/**
 * Sorting algorithm, specifically an in-place comparison sort
 *
 * The algorithm divides the input list into two parts: the sublist of items already sorted, which is built up from left to right at the front (left) of the list, and the sublist of items remaining to be sorted that occupy the rest of the list.
 * Initially, the sorted sublist is empty and the unsorted sublist is the entire input list
 */
public class SelectionSort {

    /**
     * Perform a selection sort
     *
     * @param array
     * @return the array sorted by ascending order
     */
    static int[] sort(int[] array) {
        long startTime = System.nanoTime();

        int i = 0;
        while (i < array.length) {
            int min = i; // min index
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }

            int temp = array[i];
            array[i] = array[min];
            array[min] = temp;

            i++;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "SelectionSort", duration + " ns", Arrays.toString(array));

        return array;
    }
}