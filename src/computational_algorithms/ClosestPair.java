```java
package computational_algorithms;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Given n points in the plane (metric space), find a pair of points with the smallest Euclidean distance between them.
 * This implementation uses the classic divide‐and‐conquer algorithm in O(n log n) time.
 *
 * Complexity:
 *   Time:  O(n log n)
 *   Space: O(n) auxiliary (for sorting and merging)
 */
public class ClosestPair {

    /**
     * Simple 2D point representation.
     */
    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Result object holding the closest pair and their distance.
     */
    public static class Result {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public Result(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }
    }

    /**
     * Computes the closest pair of points among the given array.
     *
     * @param points an array of Point objects (length ≥ 2)
     * @return a Result containing the two points in the closest pair and their distance
     * @throws IllegalArgumentException if points is null or has fewer than 2 elements
     */
    public static Result findClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Require at least two points");
        }

        // Create copies sorted by x‐coordinate and by y‐coordinate
        Point[] Px = points.clone();
        Point[] Py = points.clone();
        Arrays.sort(Px, Comparator.comparingDouble(pt -> pt.x));
        Arrays.sort(Py, Comparator.comparingDouble(pt -> pt.y));

        return closestPairRec(Px, Py, 0, points.length);
    }

    /**
     * Recursive helper: finds closest pair in Px[left..right) given Py (sorted by y) restricted to the same set.
     *
     * @param Px    points sorted by x‐coordinate
     * @param Py    points sorted by y‐coordinate
     * @param left  inclusive start index in Px
     * @param right exclusive end index in Px
     * @return the Result (closest pair and distance) in that subset
     */
    private static Result closestPairRec(Point[] Px, Point[] Py, int left, int right) {
        int n = right - left;
        // Base cases: use brute‐force for small n
        if (n <= 3) {
            return bruteForce(Px, left, right);
        }

        int mid = left + n / 2;
        Point midPoint = Px[mid];

        // Partition Py into points in left half (Yl) and right half (Yr)
        Point[] Yl = new Point[mid - left];
        Point[] Yr = new Point[right - mid];
        int li = 0, ri = 0;
        for (Point p : Py) {
            if (p.x < midPoint.x || (p.x == midPoint.x && li < Yl.length)) {
                if (li < Yl.length) {
                    Yl[li++] = p;
                } else {
                    Yr[ri++] = p;
                }
            } else {
                Yr[ri++] = p;
            }
        }

        // Recursively find best pairs in left half and right half
        Result dl = closestPairRec(Px, Yl, left, mid);
        Result dr = closestPairRec(Px, Yr, mid, right);

        // Determine smaller distance d between the two halves
        Result best = (dl.distance < dr.distance) ? dl : dr;
        double d = best.distance;

        // Build strip: points whose x‐coordinate is within d of midLine
        Point[] strip = new Point[n];
        int stripSize = 0;
        for (Point p : Py) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip[stripSize++] = p;
            }
        }

        // Find closest pair in strip (O(n) time because strip is sorted by y)
        for (int i = 0; i < stripSize; i++) {
            // Only need to compare up to 7 subsequent points in strip
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < d; j++) {
                double dist = distance(strip[i], strip[j]);
                if (dist < d) {
                    d = dist;
                    best = new Result(strip[i], strip[j], dist);
                }
            }
        }

        return best;
    }

    /**
     * Brute‐force method to compute closest pair among Px[left..right).
     *
     * @param Px    array sorted by x (but sorting isn’t used here)
     * @param left  inclusive start index
     * @param right exclusive end index
     * @return the Result for that small subset
     */
    private static Result bruteForce(Point[] Px, int left, int right) {
        Result best = null;
        for (int i = left; i < right; i++) {
            for (int j = i + 1; j < right; j++) {
                double dist = distance(Px[i], Px[j]);
                if (best == null || dist < best.distance) {
                    best = new Result(Px[i], Px[j], dist);
                }
            }
        }
        return best;
    }

    /**
     * Computes the Euclidean distance between two points.
     *
     * @param a first point
     * @param b second point
     * @return Euclidean distance
     */
    private static double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.hypot(dx, dy);
    }

    // -------------------------------------------------------------------------
    // Example usage / quick test
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        Point[] points = {
            new Point(2.0, 3.0),
            new Point(12.0, 30.0),
            new Point(40.0, 50.0),
            new Point(5.0, 1.0),
            new Point(12.0, 10.0),
            new Point(3.0, 4.0)
        };

        Result result = findClosestPair(points);
        System.out.printf("Closest pair: (%.2f, %.2f) and (%.2f, %.2f)%n",
                result.p1.x, result.p1.y, result.p2.x, result.p2.y);
        System.out.printf("Distance: %.5f%n", result.distance);
        // Expected output:
        // Closest pair: (2.00, 3.00) and (3.00, 4.00)
        // Distance: 1.41421
    }
}
```
