```java
package computational_algorithms;

import java.util.*;

/**
 * Algorithm for finding the shortest paths between nodes in a graph.
 * A more common variant fixes a single node as the "source" node and finds shortest paths from the source
 * to all other nodes in the graph, producing a shortest-path tree.
 *
 * This implementation assumes a directed (or undirected, by adding edges both ways) graph with non-negative edge weights.
 * It uses a binary min-heap (Java's PriorityQueue) to achieve O((n + m) log n) time, where n = number of vertices and m = number of edges.
 *
 * Complexity:
 *   Time:  O((n + m) log n)
 *   Space: O(n + m)
 */
public class Dijkstra {

    /**
     * Internal class representing a weighted edge in the graph.
     */
    public static class Edge {
        public final int to;
        public final double weight;

        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    /**
     * Result object holding the shortest‐path tree information:
     * - dist[v]: the minimum distance from the source to vertex v (Double.POSITIVE_INFINITY if unreachable)
     * - prev[v]: the predecessor of v on the shortest path (or -1 if none)
     */
    public static class Result {
        public final double[] dist;
        public final int[] prev;

        public Result(double[] dist, int[] prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }

    /**
     * Runs Dijkstra's algorithm on a graph with the given adjacency list and source vertex.
     *
     * @param n        the number of vertices (vertices are assumed to be labeled 0..n-1)
     * @param adjList  adjacency list where adjList[u] is a List of Edges (u → v with weight w)
     * @param source   the source vertex index (0 ≤ source < n)
     * @return a Result containing dist[] and prev[] arrays
     */
    public static Result dijkstra(int n, List<List<Edge>> adjList, int source) {
        // dist[v] = current known shortest distance from source to v
        double[] dist = new double[n];
        // prev[v] = predecessor of v on the shortest path from source (or -1 if none)
        int[] prev = new int[n];
        // Initialize distances to ∞, predecessors to -1
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        // Min-heap ordered by (distance, vertex)
        PriorityQueue<VertexEntry> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));

        // Distance to source is 0
        dist[source] = 0.0;
        pq.offer(new VertexEntry(source, 0.0));

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            VertexEntry entry = pq.poll();
            int u = entry.vertex;
            // If we've already found a better path to u, skip
            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            // Relax edges out of u
            for (Edge e : adjList.get(u)) {
                int v = e.to;
                double weight = e.weight;
                double alt = dist[u] + weight;
                if (alt < dist[v]) {
                    dist[v] = alt;
                    prev[v] = u;
                    pq.offer(new VertexEntry(v, alt));
                }
            }
        }

        return new Result(dist, prev);
    }

    /**
     * Helper class used in the priority queue to track (vertex, distance).
     */
    private static class VertexEntry {
        public final int vertex;
        public final double distance;

        public VertexEntry(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    /**
     * Reconstructs the shortest path from source to target using the prev[] array produced by Dijkstra.
     *
     * @param prev    the predecessor array from Dijkstra
     * @param target  the target vertex index
     * @return a List of vertex indices representing the path from source to target (inclusive);
     *         empty list if target is unreachable
     */
    public static List<Integer> reconstructPath(int[] prev, int target) {
        List<Integer> path = new ArrayList<>();
        if (prev[target] == -1 && target < 0) {
            return path; // no path
        }
        // Walk backwards from target to source
        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        /*
         * Construct the following directed graph with 6 vertices (0 through 5):
         *
         *     (0)
         *    /   \
         * 1 /     \ 4
         *  /       \
         * (1)───2──>(2)
         *  | \       |
         *  |  \3     |1
         *  |   \     v
         *  5    >   (3)
         *  |     \
         *  v      2
         * (4) <───(5)
         *
         * Edges:
         * 0 → 1 (weight 1)
         * 0 → 2 (weight 4)
         * 1 → 2 (weight 2)
         * 1 → 3 (weight 3)
         * 1 → 4 (weight 5)
         * 2 → 3 (weight 1)
         * 3 → 5 (weight 2)
         * 5 → 4 (weight 1)
         *
         */
        int n = 6;
        List<List<Edge>> adjList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        // Add edges
        adjList.get(0).add(new Edge(1, 1.0));
        adjList.get(0).add(new Edge(2, 4.0));

        adjList.get(1).add(new Edge(2, 2.0));
        adjList.get(1).add(new Edge(3, 3.0));
        adjList.get(1).add(new Edge(4, 5.0));

        adjList.get(2).add(new Edge(3, 1.0));

        adjList.get(3).add(new Edge(5, 2.0));

        adjList.get(5).add(new Edge(4, 1.0));

        // Run Dijkstra from source = 0
        int source = 0;
        Result result = dijkstra(n, adjList, source);

        // Print shortest distances from source
        System.out.println("Vertex\tDistance from Source");
        for (int v = 0; v < n; v++) {
            double d = result.dist[v];
            System.out.printf("%d\t\t%s%n", v, (d == Double.POSITIVE_INFINITY ? "∞" : String.format("%.2f", d)));
        }

        // Reconstruct and print shortest paths from source to each vertex
        System.out.println("\nShortest paths from source " + source + ":");
        for (int v = 0; v < n; v++) {
            if (result.dist[v] == Double.POSITIVE_INFINITY) {
                System.out.println("No path to vertex " + v);
            } else {
                List<Integer> path = reconstructPath(result.prev, v);
                System.out.println("Path to " + v + ": " + path);
            }
        }

        /*
         * Expected output:
         *
         * Vertex  Distance from Source
         * 0       0.00
         * 1       1.00
         * 2       3.00
         * 3       4.00
         * 4       5.00
         * 5       6.00
         *
         * Shortest paths from source 0:
         * Path to 0: [0]
         * Path to 1: [0, 1]
         * Path to 2: [0, 1, 2]
         * Path to 3: [0, 1, 2, 3]
         * Path to 4: [0, 1, 2, 3, 5, 4]
         * Path to 5: [0, 1, 2, 3, 5]
         */
    }
}
```
