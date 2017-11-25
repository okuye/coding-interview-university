package computational_algorithms;

import java.util.Arrays;

/**
 * Efficient sorting algorithm, serving as a systematic method for placing the elements of an array in order.
 */
public class QuickSort {
    /**
     * Perform a quick sort
     *
     * @param array
     * @return the array sorted by ascending order
     */
    static int[] sort(int[] array) {
        long startTime = System.nanoTime();

        quickSort(array, 0, array.length - 1);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "QuickSort", duration + " ns", Arrays.toString(array));

        return array;
    }

    private static void quickSort(int[] array, int begin, int end) {
        if (begin >= end) {
            return;
        }

        int pivot = getPivot(array, begin, end);

        int left = begin, right = end;
        while (left <= right) {

            while (array[left] < pivot) {
                left++;
            }

            while (array[right] > pivot) {
                right--;
            }

            // we found 2 elements that are not at the right position, with regard to the pivot

            if (left <= right) {
                swap(array, left, right);
                left++;
                right--;
            }
        }

        quickSort(array, begin, right);
        quickSort(array, left, end);
    }

    // Median-of-three
    private static int getPivot(int[] array, int begin, int end) {
        int middle = begin + (end - begin) / 2;

        if (array[begin] >  array[end]) {
            swap(array, begin, end);
        }

        if (array[begin] > array[middle]) {
            swap(array, begin, middle);
        }

        if (array[middle] > array[end]) {
            swap(array, middle, end);
        }

        return array[middle];
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
