```java
package computational_algorithms;

import java.util.BitSet;

/**
 * Probabilistic data structure designed to tell you, rapidly and memory-efficiently,
 * whether an element is present in a set. False positive matches are possible, but false negatives are not.
 * In other words, a query returns either "possibly in set" or "definitely not in set."
 * Elements can be added to the set, but not removed (though this can be addressed with a "counting" filter);
 * the more elements that are added, the larger the probability of false positives.
 *
 * Internally uses a BitSet of a given size (m) and k independent hash functions (implemented via double-hashing).
 *
 * Steps:
 * 1) Initialize a bit array of m bits (all 0).
 * 2) When adding a new element x, compute k hash values h₁(x), h₂(x), …, hₖ(x),
 *    each in the range [0, m). For each i, set bitArray[hᵢ(x)] = 1.
 * 3) To check membership of y, compute the same k hash values; if any bitArray[hᵢ(y)] is 0,
 *    y is definitely not in the set; otherwise, y is possibly in the set.
 *
 * Complexity:
 *   - add():  O(k)
 *   - contains():  O(k)
 *   - Space:  O(m)
 *
 * @param <T> the type of elements inserted into the filter (must have a reliable hashCode())
 */
public class BloomFilter<T> {

    /** Bit array for storing presence bits. */
    private final BitSet bitset;
    /** Number of bits in the filter (m). */
    private final int bitSize;
    /** Number of hash functions to use (k). */
    private final int numHashFunctions;

    /**
     * Constructs a BloomFilter with the given bit array size and number of hash functions.
     *
     * @param bitSize           number of bits (m) in the filter
     * @param numHashFunctions  number of hash functions (k) to apply per element
     * @throws IllegalArgumentException if bitSize <= 0 or numHashFunctions <= 0
     */
    public BloomFilter(int bitSize, int numHashFunctions) {
        if (bitSize <= 0) {
            throw new IllegalArgumentException("bitSize must be positive");
        }
        if (numHashFunctions <= 0) {
            throw new IllegalArgumentException("numHashFunctions must be positive");
        }
        this.bitSize = bitSize;
        this.numHashFunctions = numHashFunctions;
        this.bitset = new BitSet(bitSize);
    }

    /**
     * Adds an element to this Bloom filter. Sets k bits in the bit array based on the element's hash.
     *
     * @param element the element to add (null is allowed; will be treated like any other value)
     */
    public void add(T element) {
        int[] hashes = computeHashes(element);
        for (int hash : hashes) {
            bitset.set(hash);
        }
    }

    /**
     * Checks whether the element is possibly in the set. Returns false if it is definitely not in the set,
     * or true if it might be (with some false-positive probability).
     *
     * @param element the element to test membership for
     * @return {@code true} if the element is possibly in the set; {@code false} if definitely not
     */
    public boolean contains(T element) {
        int[] hashes = computeHashes(element);
        for (int hash : hashes) {
            if (!bitset.get(hash)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the number of bits (m) in this Bloom filter.
     *
     * @return bitSize
     */
    public int bitSize() {
        return bitSize;
    }

    /**
     * Returns the number of hash functions (k) used by this Bloom filter.
     *
     * @return numHashFunctions
     */
    public int numHashFunctions() {
        return numHashFunctions;
    }

    /**
     * Clears the Bloom filter (resets all bits to 0).
     */
    public void clear() {
        bitset.clear();
    }

    /**
     * Computes k hash values for the given element using double hashing:
     *   hash₁ = element.hashCode()
     *   hash₂ = secondary hash (derived from hash₁)
     *   hᵢ = (hash₁ + i * hash₂) mod bitSize, for i = 0..k-1
     *
     * Ensures each returned index is in [0, bitSize).
     *
     * @param element the element to hash (may be null)
     * @return an array of k distinct bit positions
     */
    private int[] computeHashes(T element) {
        int[] result = new int[numHashFunctions];
        int hash1 = (element == null) ? 0 : element.hashCode();
        int hash2 = secondaryHash(hash1);

        long combined;
        for (int i = 0; i < numHashFunctions; i++) {
            combined = ( (long)hash1 + (long)i * hash2 ) & 0x7fffffff;  // keep non-negative
            result[i] = (int)(combined % bitSize);
        }
        return result;
    }

    /**
     * A simple secondary hash derived from the primary hashCode to enable "double hashing."
     * This implementation uses a right-rotation and XOR to mix bits.
     *
     * @param h the original hash code
     * @return a second hash value
     */
    private int secondaryHash(int h) {
        // Example mixing: rotate right by 16 bits, then XOR with original
        return ( (h >>> 16) | (h << 16) ) ^ h;
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Example parameters: 
        //   - choose bitSize large enough that false positives are low
        //   - choose numHashFunctions appropriate for desired false-positive rate
        int bitSize = 1 << 20;         // about one million bits (~128 KB)
        int numHashFunctions = 7;      // commonly used k for many settings

        BloomFilter<String> filter = new BloomFilter<>(bitSize, numHashFunctions);

        // Add some strings
        filter.add("alice");
        filter.add("bob");
        filter.add("carol");

        // Test membership
        System.out.println("Contains 'alice'? " + filter.contains("alice"));   // true (definitely or possibly)
        System.out.println("Contains 'dave'?  " + filter.contains("dave"));    // false (definitely not)

        // Add more elements
        filter.add("dave");
        filter.add("eve");

        System.out.println("After adding 'dave' and 'eve':");
        System.out.println("Contains 'dave'?  " + filter.contains("dave"));    // true
        System.out.println("Contains 'eve'?   " + filter.contains("eve"));     // true

        // Even after clearing, false positives may occur until all bits reset
        filter.clear();
        System.out.println("\nAfter clearing:");
        System.out.println("Contains 'alice'? " + filter.contains("alice"));   // likely false
        System.out.println("Contains 'dave'?  " + filter.contains("dave"));    // likely false
    }
}
```
