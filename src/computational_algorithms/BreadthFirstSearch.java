```java
package computational_algorithms;

import java.util.*;

/**
 * BFS: algorithm for traversing or searching tree or graph data structures.
 * It starts at the tree root (or some arbitrary node of a graph, sometimes referred to as a 'search key')
 * and explores the neighbor nodes first, before moving to the next‐level neighbours.
 *
 * Below are two common variants:
 *   1) BFS on a binary tree (level‐order traversal).
 *   2) BFS on a graph given as an adjacency list.
 *
 * Complexity (for both forms, assuming n = number of nodes and m = number of edges):
 *   Time:  O(n + m)
 *   Space: O(n)   (for the queue and visited set/array)
 */
public class BreadthFirstSearch {

    // --------------------------------------------------
    // Part 1: BFS on a Binary Tree (Level‐Order Traversal)
    // --------------------------------------------------
    /**
     * Simple binary tree node.
     */
    public static class TreeNode<T> {
        public T value;
        public TreeNode<T> left;
        public TreeNode<T> right;

        public TreeNode(T value) {
            this.value = value;
        }
    }

    /**
     * Performs a level‐order (breadth‐first) traversal of a binary tree,
     * returning a list of node values in the order they are visited.
     *
     * @param root the root node of the binary tree
     * @param <T>  the type of values stored in the tree
     * @return a List of node values in BFS order; empty list if root is null
     */
    public static <T> List<T> bfsBinaryTree(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode<T>> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode<T> node = queue.poll();
            result.add(node.value);

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }

        return result;
    }

    // --------------------------------------------------
    // Part 2: BFS on a Graph (Adjacency List)
    // --------------------------------------------------
    /**
     * Performs a breadth‐first search on a graph represented by an adjacency list.
     * Assumes nodes are labeled 0..n-1. Returns the list of nodes in the order they are first visited.
     *
     * @param adjList adjacency list where adjList[u] is a List of neighbors of u
     * @param start   the starting node index
     * @return List of node indices in BFS order (may not include disconnected nodes)
     */
    public static List<Integer> bfsGraph(List<List<Integer>> adjList, int start) {
        int n = adjList.size();
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            for (int v : adjList.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.offer(v);
                }
            }
        }
        return order;
    }

    // --------------------------------------------------
    // Example usage / quick test
    // --------------------------------------------------
    public static void main(String[] args) {
        // --- Example 1: BFS on a Binary Tree ---
        /*
         * Construct the following binary tree:
         *          1
         *        /   \
         *       2     3
         *      / \   / \
         *     4   5 6   7
         */
        TreeNode<Integer> root = new TreeNode<>(1);
        root.left = new TreeNode<>(2);
        root.right = new TreeNode<>(3);
        root.left.left = new TreeNode<>(4);
        root.left.right = new TreeNode<>(5);
        root.right.left = new TreeNode<>(6);
        root.right.right = new TreeNode<>(7);

        List<Integer> treeBfsOrder = bfsBinaryTree(root);
        System.out.println("Binary Tree BFS (level order): " + treeBfsOrder);
        // Expected output: [1, 2, 3, 4, 5, 6, 7]


        // --- Example 2: BFS on a Graph ---
        /*
         * Construct the following undirected graph (0-based indexing):
         *
         *    (0)---(1)---(3)
         *     |     |
         *    (2)   (4)
         *
         * Adjacency list:
         * 0: [1,2]
         * 1: [0,3,4]
         * 2: [0]
         * 3: [1]
         * 4: [1]
         *
         */
        int numNodes = 5;
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<>());
        }
        // Add undirected edges
        addUndirectedEdge(adjList, 0, 1);
        addUndirectedEdge(adjList, 0, 2);
        addUndirectedEdge(adjList, 1, 3);
        addUndirectedEdge(adjList, 1, 4);

        int startNode = 0;
        List<Integer> graphBfsOrder = bfsGraph(adjList, startNode);
        System.out.println("Graph BFS starting at node 0: " + graphBfsOrder);
        // One possible output: [0, 1, 2, 3, 4]
        // (The exact ordering among neighbors depends on insertion order in adjList.)

        // To demonstrate handling a disconnected graph, we can add node 5 with no edges:
        adjList.add(new ArrayList<>());  // node index 5 (disconnected)
        // If you want to traverse all components, you would loop over all nodes:
        List<Integer> fullOrder = bfsAllComponents(adjList);
        System.out.println("Graph BFS over all components: " + fullOrder);
        // Expected output: [0,1,2,3,4, 5]
    }

    // Helper to add an undirected edge in adjacency list
    private static void addUndirectedEdge(List<List<Integer>> adjList, int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    /**
     * Performs BFS for every component in the graph, returning nodes in the order they are first visited.
     * Useful when the graph may be disconnected.
     *
     * @param adjList adjacency list for all nodes (indices 0..n-1)
     * @return List of all node indices in the order of BFS (across components)
     */
    public static List<Integer> bfsAllComponents(List<List<Integer>> adjList) {
        int n = adjList.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();

        for (int node = 0; node < n; node++) {
            if (!visited[node]) {
                Queue<Integer> queue = new ArrayDeque<>();
                visited[node] = true;
                queue.offer(node);

                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    order.add(u);
                    for (int v : adjList.get(u)) {
                        if (!visited[v]) {
                            visited[v] = true;
                            queue.offer(v);
                        }
                    }
                }
            }
        }
        return order;
    }
}
```
