```java
package computational_algorithms;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * The travelling salesman problem (TSP) asks the following question:
 * "Given a list of cities and the distances between each pair of cities,
 * what is the shortest possible route that visits each city exactly once
 * and returns to the origin city?"
 *
 * This implementation uses the Held–Karp dynamic‐programming algorithm
 * (bitmask DP) to solve TSP in O(n² · 2ⁿ) time and O(n · 2ⁿ) space,
 * where n is the number of cities.
 *
 * Complexity:
 *   Time:  O(n² · 2ⁿ)
 *   Space: O(n · 2ⁿ)
 */
public class TravelingSalesman {

    /**
     * Solves the TSP for a complete graph of n cities (0..n-1) with the given distance matrix.
     * Assumes distances[i][j] ≥ 0, distances[i][i] = 0, and distances is symmetric (distances[i][j] == distances[j][i]).
     *
     * @param distances an n×n matrix of pairwise distances between cities
     * @return a Result object containing the minimal tour cost and the sequence of cities in the optimal tour
     * @throws IllegalArgumentException if distances is null, not square, or has fewer than 2 cities
     */
    public static Result solveTSP(double[][] distances) {
        if (distances == null) {
            throw new IllegalArgumentException("Distance matrix cannot be null");
        }
        int n = distances.length;
        if (n < 2) {
            throw new IllegalArgumentException("At least two cities are required");
        }
        for (double[] row : distances) {
            if (row.length != n) {
                throw new IllegalArgumentException("Distance matrix must be square");
            }
        }

        // Number of subsets of {1, 2, ..., n-1} is 2^(n-1). We treat city 0 as the fixed start.
        int subsetCount = 1 << (n - 1);

        // dp[mask][i] = minimum cost to start at city 0, visit all cities in 'mask', and end at city i.
        // Here, mask is an (n-1)-bit bitmask representing which of {1..n-1} are already visited.
        // We will index i from 1..n-1 (the endpoint city).
        double[][] dp = new double[subsetCount][n];
        // parent[mask][i] stores the previous city before i on the optimal path for dp[mask][i].
        int[][] parent = new int[subsetCount][n];

        // Initialize dp to +∞
        for (int mask = 0; mask < subsetCount; mask++) {
            Arrays.fill(dp[mask], Double.POSITIVE_INFINITY);
            Arrays.fill(parent[mask], -1);
        }

        // Base case: mask = 0 means no cities (1..n-1) visited yet, and we jump from city 0 to city i directly.
        // Represent mask = 0 by integer 0. For each i=1..n-1,
        // dp[0][i] = cost(0 → i), and parent[0][i] = 0.
        for (int i = 1; i < n; i++) {
            dp[0][i] = distances[0][i];
            parent[0][i] = 0;
        }

        // Iterate over all nonempty subsets of {1..n-1}. Let mask run from 1 to 2^(n-1)-1.
        // For each mask and for each endpoint j in mask (i.e., bit j-1 is set), compute:
        // dp[mask][j] = min over k in mask \ {j} of (dp[mask without j][k] + cost(k → j)).
        for (int mask = 1; mask < subsetCount; mask++) {
            for (int j = 1; j < n; j++) {
                int jBit = 1 << (j - 1);
                if ((mask & jBit) == 0) {
                    // City j not in this subset
                    continue;
                }
                // Remove j from mask
                int prevMask = mask ^ jBit;
                // Try all possible predecessors k (in prevMask) to reach j
                for (int k = 1; k < n; k++) {
                    int kBit = 1 << (k - 1);
                    if ((prevMask & kBit) == 0) {
                        continue; // City k not in prevMask
                    }
                    double newCost = dp[prevMask][k] + distances[k][j];
                    if (newCost < dp[mask][j]) {
                        dp[mask][j] = newCost;
                        parent[mask][j] = k;
                    }
                }
            }
        }

        // Finally, consider returning to city 0 from each possible last city j (in fullMask),
        // where fullMask = (1 << (n-1)) - 1 (all cities 1..n-1 visited).
        int fullMask = subsetCount - 1;
        double minTourCost = Double.POSITIVE_INFINITY;
        int lastCity = -1;
        for (int j = 1; j < n; j++) {
            double tourCost = dp[fullMask][j] + distances[j][0];
            if (tourCost < minTourCost) {
                minTourCost = tourCost;
                lastCity = j;
            }
        }

        // Reconstruct the optimal tour path by walking backwards via parent[][]
        List<Integer> tour = new ArrayList<>();
        // Start from city 0 as both start and end; we reconstruct the part 0→...→lastCity→0
        // We'll build the intermediate sequence in reverse order, then add 0 at front and end.
        int mask = fullMask;
        int currCity = lastCity;
        // Add lastCity first (we'll reverse later)
        tour.add(currCity);
        while (mask != 0) {
            int prevCity = parent[mask][currCity];
            tour.add(prevCity);
            // Remove currCity from mask
            mask ^= (1 << (currCity - 1));
            currCity = prevCity;
        }
        // At this point currCity should be 0 (the base parent of the first hop)
        // Reverse to get: [0, ..., lastCity]
        List<Integer> reversed = new ArrayList<>();
        for (int i = tour.size() - 1; i >= 0; i--) {
            reversed.add(tour.get(i));
        }
        // Finally, append 0 to complete the cycle
        reversed.add(0);

        return new Result(minTourCost, reversed);
    }

    /**
     * Result holder for TSP:
     * - cost: the minimal tour length
     * - tour: the list of city indices in visiting order, starting and ending at 0
     */
    public static class Result {
        public final double cost;
        public final List<Integer> tour;

        public Result(double cost, List<Integer> tour) {
            this.cost = cost;
            this.tour = tour;
        }
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        /*
         * Example with 4 cities (0,1,2,3) and the following symmetric distance matrix:
         *
         *    0   1   2   3
         * 0 [0  10  15  20]
         * 1 [10  0  35  25]
         * 2 [15 35   0  30]
         * 3 [20 25  30   0]
         *
         * The optimal tour starting and ending at 0 is: 0 → 1 → 3 → 2 → 0 with cost 80.
         */
        double[][] distances = {
            {  0, 10, 15, 20 },
            { 10,  0, 35, 25 },
            { 15, 35,  0, 30 },
            { 20, 25, 30,  0 }
        };

        Result result = solveTSP(distances);
        System.out.printf("Minimum tour cost: %.2f%n", result.cost);
        System.out.println("Tour order: " + result.tour);
        // Expected output:
        // Minimum tour cost: 80.00
        // Tour order: [0, 1, 3, 2, 0]
    }
}
```
