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
    static <T extends Comparable<T>> void sort(T[] array) {
        long startTime = System.nanoTime();

        quickSort(array, 0, array.length - 1);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "QuickSort", duration + " ns", Arrays.toString(array));
    }

    private static <T extends Comparable<T>> void quickSort(T[] array, int begin, int end) {
        if (begin >= end) {
            return;
        }

        T pivot = getPivot(array, begin, end);

        int left = begin, right = end;
        while (left <= right) {

            while (array[left].compareTo(pivot) < 0) {
                left++;
            }

            while (array[right].compareTo(pivot) > 0) {
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
    private static <T extends Comparable<T>> T getPivot(T[] array, int begin, int end) {
        int middle = begin + (end - begin) / 2;

        if (array[begin].compareTo(array[end]) > 0) {
            swap(array, begin, end);
        }

        if (array[begin].compareTo(array[middle]) > 0) {
            swap(array, begin, middle);
        }

        if (array[middle].compareTo(array[end]) > 0) {
            swap(array, middle, end);
        }

        return array[middle];
    }

    private static <T extends Comparable<T>> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
