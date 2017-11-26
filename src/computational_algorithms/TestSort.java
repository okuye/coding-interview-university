package computational_algorithms;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class TestSort {
    private static void testInteger(int nbInt, int low, int high) {
        int[] intArray = new Random().ints(nbInt, low, high).toArray();
        IntStream integerStream = IntStream.of(intArray);
        Integer[] integerArray = integerStream.boxed().toArray(Integer[]::new);

        System.out.println();
        System.out.println("*********************");
        System.out.println("Integer array to sort : " + Arrays.toString(intArray));
        System.out.println("*********************");
        System.out.println();

        BubbleSort.sort(integerArray.clone());
        SelectionSort.sort(integerArray.clone());
        InsertionSort.sort(integerArray.clone());
        QuickSort.sort(integerArray.clone());
        BucketSort.sort(intArray.clone());
    }

    private static void testDouble(int nbDouble, double low, double high) {
        Double[] doubleArray = new Random().doubles(nbDouble, low, high).boxed().toArray(Double[]::new);

        System.out.println();
        System.out.println("*********************");
        System.out.println("Double array to sort : " + Arrays.toString(doubleArray));
        System.out.println("*********************");
        System.out.println("");

        BubbleSort.sort(doubleArray.clone());
        SelectionSort.sort(doubleArray.clone());
        InsertionSort.sort(doubleArray.clone());
        QuickSort.sort(doubleArray.clone());
    }

    public static void main(String[] args) {
        int nbElem = 5000;

        int lowestInt = 0, highestInt = 501;
        testInteger(nbElem, lowestInt, highestInt);

        double lowestDouble = 1, highestDouble = 20;
        testDouble(nbElem, lowestDouble, highestDouble);
    }
}
