package computational_algorithms;


import java.util.Arrays;

/**
 * Simple sorting algorithm that builds the final sorted array (or list) one item at a time.
 * It is much less efficient on large lists than more advanced algorithms such as quicksort, heapsort, or merge sort
 */
public class InsertionSort {

    /**
     * Perform an insertion sort
     *
     * @param array
     * @return the array sorted by ascending order
     */
    static int[] sort(int[] array) {
        long startTime = System.nanoTime();

        for (int i = 1; i < array.length; i++) { // loop through all items to sort
           int valToInsert = array[i]; // value to insert
           int j = i; // the array is already sorted between 0 and i - 1
           while (j > 0 && array[j-1] > valToInsert) { // shift the values within the sorted part to the right as long as the value to insert is lower than the current value
               array[j] = array[j-1];
               j--;
           }
           // j represents the index of the array where the value should be inserted
           array[j] = valToInsert;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "InsertionSort", duration + " ns", Arrays.toString(array));

        return array;
    }
}
