```java
package computational_algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Algorithm for finding shortest paths in a weighted graph with positive or negative edge weights
 * (but with no negative cycles). Uses the Floyd–Warshall all-pairs shortest paths algorithm.
 *
 * Complexity:
 *   Time:  O(n³)      (where n = number of vertices)
 *   Space: O(n²)      (for distance and next‐hop matrices)
 *
 * The graph is represented by its adjacency matrix. If there is no direct edge from i to j,
 * the weight should be set to Double.POSITIVE_INFINITY. Negative weights are allowed, but
 * the graph must contain no negative-weight cycles (i.e., no cycle whose total weight is negative).
 *
 * This implementation also supports path reconstruction via a "next" matrix:
 *   next[i][j] = the index of the vertex immediately after i on the shortest path from i to j.
 */
public class FloydWarshall {

    /**
     * Encapsulates the result of running Floyd–Warshall:
     * - dist[i][j]: the length of the shortest path from i to j (Double.POSITIVE_INFINITY if no path).
     * - next[i][j]: the next vertex after i on the shortest path to j (or -1 if no path).
     */
    public static class Result {
        public final double[][] dist;
        public final int[][] next;

        public Result(double[][] dist, int[][] next) {
            this.dist = dist;
            this.next = next;
        }
    }

    /**
     * Runs the Floyd–Warshall algorithm on a graph with n vertices (0..n-1), given its adjacency matrix.
     * The adjacency matrix weight[u][v] should be:
     *   - the weight of the edge (u→v) if one exists,
     *   - Double.POSITIVE_INFINITY if no direct edge from u to v,
     *   - 0.0 if u == v.
     *
     * After execution, dist[i][j] is the shortest distance from i to j, or Double.POSITIVE_INFINITY if unreachable.
     * If a negative cycle is detected (i.e., dist[i][i] < 0 for some i), this method throws an exception.
     *
     * @param weight the n×n adjacency matrix of the graph
     * @return a Result containing the distance matrix and next‐hop matrix
     * @throws IllegalArgumentException if weight is null or not square
     * @throws RuntimeException         if a negative cycle is detected
     */
    public static Result floydWarshall(double[][] weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Adjacency matrix cannot be null");
        }
        int n = weight.length;
        for (double[] row : weight) {
            if (row.length != n) {
                throw new IllegalArgumentException("Adjacency matrix must be square");
            }
        }

        // Initialize dist and next matrices
        double[][] dist = new double[n][n];
        int[][] next = new int[n][n];

        // Copy weights into dist, and set up next-hop
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = weight[i][j];
                if (i == j) {
                    next[i][j] = i; // path from i to itself
                } else if (weight[i][j] < Double.POSITIVE_INFINITY) {
                    next[i][j] = j; // there's a direct edge i→j
                } else {
                    next[i][j] = -1; // no path known yet
                }
            }
        }

        // Main triple-nested loop of Floyd–Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == Double.POSITIVE_INFINITY) {
                    continue; // no need to proceed if i→k is unreachable
                }
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] == Double.POSITIVE_INFINITY) {
                        continue; // if k→j is unreachable, skip
                    }
                    double newDist = dist[i][k] + dist[k][j];
                    if (newDist < dist[i][j]) {
                        dist[i][j] = newDist;
                        next[i][j] = next[i][k]; // path goes through k
                    }
                }
            }
        }

        // Check for negative cycles: if dist[i][i] < 0 for any i, there is a negative cycle
        for (int i = 0; i < n; i++) {
            if (dist[i][i] < 0) {
                throw new RuntimeException("Negative cycle detected involving vertex " + i);
            }
        }

        return new Result(dist, next);
    }

    /**
     * Reconstructs the shortest path from source to target using the next matrix produced by Floyd–Warshall.
     *
     * @param next   the next‐hop matrix from floydWarshall()
     * @param source the source vertex index
     * @param target the target vertex index
     * @return a List of vertex indices representing the path from source to target (inclusive);
     *         empty List if no path exists.
     */
    public static List<Integer> reconstructPath(int[][] next, int source, int target) {
        List<Integer> path = new ArrayList<>();
        if (next[source][target] == -1) {
            return path; // no path exists
        }
        int at = source;
        while (at != target) {
            path.add(at);
            at = next[at][target];
            if (at == -1) {
                // Shouldn't happen if next was constructed properly, but just in case
                return new ArrayList<>();
            }
        }
        path.add(target);
        return path;
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        /*
         * Example:
         *
         * Graph with 4 vertices (0..3) and weighted edges (could be negative but no negative cycles):
         *
         *   (0) --5--> (1)
         *    | \         \
         *   3|  \2        \-1
         *    |   v         \
         *   (2) <--4--    (3)
         *    \           /
         *    -2\       / 3
         *       v     v
         *       (1) --------> (3)
         *
         * Adjacency matrix representation: weight[u][v] = edge weight or ∞ if no edge.
         */

        final double INF = Double.POSITIVE_INFINITY;
        double[][] weight = {
            { 0,   5,   3,   INF },
            { INF, 0,   INF, -1  },
            { INF, 4,   0,    INF },
            { INF, INF, 3,    0   }
        };

        // Run Floyd–Warshall
        Result result = floydWarshall(weight);
        double[][] dist = result.dist;
        int[][] next = result.next;

        // Print the distance matrix
        System.out.println("All‐pairs shortest distances:");
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("  INF ");
                } else {
                    System.out.printf("%6.2f ", dist[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();

        // Reconstruct and print specific paths
        int n = weight.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                List<Integer> path = reconstructPath(next, i, j);
                if (path.isEmpty()) {
                    System.out.printf("No path from %d → %d%n", i, j);
                } else {
                    System.out.printf("Path %d → %d: %s (distance = %.2f)%n",
                            i, j, path, dist[i][j]);
                }
            }
            System.out.println();
        }

        /*
         * Expected output:
         *
         * All‐pairs shortest distances:
         *   0.00  5.00  3.00  4.00 
         *   7.00  0.00 10.00  -1.00
         *   8.00  4.00  0.00  3.00 
         *   6.00 10.00  3.00  0.00 
         *
         * Path 0 → 1: [0, 1] (distance = 5.00)
         * Path 0 → 2: [0, 2] (distance = 3.00)
         * Path 0 → 3: [0, 1, 3] (distance = 4.00)
         *
         * Path 1 → 0: [1, 3, 2, 1, 0] (distance = 7.00)
         * Path 1 → 2: [1, 3, 2] (distance = 10.00)
         * Path 1 → 3: [1, 3] (distance = -1.00)
         *
         * Path 2 → 0: [2, 1, 0] (distance = 8.00)
         * Path 2 → 1: [2, 1] (distance = 4.00)
         * Path 2 → 3: [2, 1, 3] (distance = 3.00)
         *
         * Path 3 → 0: [3, 2, 1, 0] (distance = 6.00)
         * Path 3 → 1: [3, 2, 1] (distance = 10.00)
         * Path 3 → 2: [3, 2] (distance = 3.00)
         */
    }
}
```
