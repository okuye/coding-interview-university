package computational_algorithms;

import java.util.Arrays;
import java.util.Random;

public class TestSort {
    public static void main(String[] args) {
        int nbInt = 500, lowestInt = 0, highestInt = 250;
        int[] array = new Random().ints(nbInt, lowestInt, highestInt).toArray();

        System.out.println("Array to sort : " + Arrays.toString(array));
        System.out.println("");

        BubbleSort.sort(array.clone());
        SelectionSort.sort(array.clone());
        InsertionSort.sort(array.clone());
        QuickSort.sort(array.clone());
    }
}
