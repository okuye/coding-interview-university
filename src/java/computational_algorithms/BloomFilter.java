package java.computational_algorithms;

/**
 *  Probabilistic data structure designed to tell you, rapidly and memory-efficiently, whether an element is present in a set.
 *
 *  False positive matches are possible, but false negatives are not.
 *  In other words, a query returns either "possibly in set" or "definitely not in set" (remember it's a << probabilistic >> data structure).
 *  Elements can be added to the set, but not removed (though this can be addressed with a "counting" filter); the more elements that are added to the set, the larger the probability of false positives.
 *
 * 1) Initialize a bit array of n bits with zeros. Generally n is chosen to be much greater than the number of elements in the set.
 * 2) Whenever the filter sees a new element apply each of the hash functions h(x) on the element. With the value generated, which is an index in the bit array, set the bit to 1 in the array. For example, if there are k hash functions there will be k indices generated. For each of these k positions in the bit array set array[i] = 1
 * 3) To check if an element exists in the set, simply carry out the exact same procedure with a slight twist. Generate k values by applying the k hash-functions on the input. If at least one of these k indices in the bit array is set to zero then the element is a new element else this is an existing element in the set.
 *
 */
public class BloomFilter {

}
