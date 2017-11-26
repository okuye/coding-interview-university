package computational_algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sorting algorithm that works by distributing the elements of an array into a number of buckets.
 * Each bucket is then sorted individually, either using a different sorting algorithm, or by recursively applying the bucket sorting algorithm.
 */
public class BucketSort {
    /**
     * Perform a bucket sort
     *
     * @param array
     * @return the array sorted by ascending order
     */
    static void sort(int[] array) {
        long startTime = System.nanoTime();

        bucketSort(array);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf( "%-25s %-15s %-15s %n", "BucketSort", duration + " ns", Arrays.toString(array));
    }

    private static void bucketSort(int[] array) {
        if (array.length == 0 || array.length == 1) {
            return;
        }

        int min = getMin(array);
        int max = getMax(array);

        // Create an array of buckets
        List<Integer>[] buckets = createBuckets((max - min) / array.length + 1);

        // Add each elem of the original array to one of the bucket
        for (int elem : array) {
            int index = (elem - min) / array.length;
            buckets[index].add(elem);
        }

        // Sort each bucket and inject into the original array the sorted values
        int index = 0;
        for (List<Integer> bucket : buckets) {

            Integer[] bucketArr = new Integer[bucket.size()];
            bucketArr = bucket.toArray(bucketArr);
            InsertionSort.sortForBucket(bucketArr);

            for (int elem : bucketArr) {
                array[index] = elem;
                index++;
            }
        }
    }

    private static int getMax(int[] array) {
        int max = array[0];
        for (int elem : array) {
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }

    private static int getMin(int[] array) {
        int min = array[0];
        for (int elem : array) {
            if (elem < min) {
                min = elem;
            }
        }
        return min;
    }

    private static List<Integer>[] createBuckets(int n) {
        List<Integer>[] buckets = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            buckets[i] = new ArrayList<>();
        }
        return buckets;
    }
}
