package java.data_structures;

/**
 * Data structures that store "items" (such as numbers, names etc.) in memory.
 * They allow fast lookup, addition and removal of items, and can be used to implement either dynamic sets of items, or lookup tables that allow finding an item by its key (e.g., finding the phone number of a person by name).
 *
 * Binary search trees keep their keys in sorted order, so that lookup and other operations can use the principle of binary search:
 * when looking for a key in a tree (or a place to insert a new key), they traverse the tree from root to leaf, making comparisons to keys stored in the nodes of the tree and deciding, based on the comparison, to continue searching in the left or right subtrees
 *
 * Complexity :
 *
 *  Algorithm 	Average 	Worst Case
 *  Space 		O(n) 	    O(n)
 *  Search 		O(log n) 	O(n)
 *  Insert 		O(log n) 	O(n)
 *  Delete 		O(log n) 	O(n)
 *
 */
public class BinarySearchTree {

}
