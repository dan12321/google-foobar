package Task8;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Solution for "Running with Bunnies"
 * 
 * @author Daniel Songi
 *
 */
public class Solution {

	/**
	 * Finds the most bunnies that can be picked up in a given time
	 * 
	 * @param times       The time to get from one bunny to another (can be
	 *                    negative)
	 * @param times_limit The time limit
	 * @return The bunnies that can be picked up
	 */
	public static int[] solution(int[][] times, int times_limit) {
		int[][] next = shortestDists(times);
		for (int i = 0; i < times.length; i++) {
			// if there's any negative loops all bunnies can be picked up
			if (times[i][i] < 0) {
				int[] result = new int[times.length - 2];
				for (int j = 0; j < result.length; j++) {
					result[j] = j;
				}
				return result;
			}
		}
		int[] best = bestSol(times, next, times_limit);
		if (best.length == 0) {
			return new int[0];
		}
		Arrays.sort(best);
		int[] result = Arrays.copyOfRange(best, 1, best.length - 1);
		if (result.length > 0) {
			for (int i = 0; i < result.length; i++) {
				result[i] -= 1;
			}
			return result;
		}
		return new int[0];
	}

	/**
	 * Overrides a matrix with the shortest distances between bunnies and returns a
	 * matrix to reproduce the routes between them
	 * 
	 * @param edges The original times between points
	 * @return The next point to get to the goal (e.g. next[i][j] is the next point
	 *         on the shortest path from i to j)
	 */
	public static int[][] shortestDists(int[][] edges) {
		int[][] next = new int[edges.length][edges.length];
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges.length; j++) {
				next[i][j] = j;
			}
		}
		for (int k = 0; k < edges.length; k++) {
			for (int i = 0; i < edges.length; i++) {
				for (int j = 0; j < edges.length; j++) {
					if (edges[i][j] > edges[i][k] + edges[k][j]) {
						edges[i][j] = edges[i][k] + edges[k][j];
						next[i][j] = next[i][k];
					}
				}
			}
		}
		return next;
	}

	/**
	 * Finds the most bunnies that can be picked up in a given time
	 * 
	 * @param times The times to get from one bunny to another (cannot be negative)
	 * @param next  The next indexes on a shortest path (e.g. for i -> j the next
	 *              stop is next[i][j])
	 * @param time  The time limit
	 * @return The bunnies that can be picked up
	 */
	public static int[] bestSol(int[][] times, int[][] next, int time) {
		int[] best = new int[0];

		for (int steps = times.length - 2; steps >= 0; steps--) {
			int c = choose(times.length - 2, steps);
			int[] stops = new int[steps];
			for (int i = 0; i < steps; i++) {
				stops[i] = i + 1;
			}
			for (int i = 0; i < c - 1; i++) {
				best = permute(stops, best, next, times, time, 0);
				if (best.length == times.length) {
					return best;
				}

				Arrays.sort(stops);
				if (stops.length > 0) {
					incrementChoose(stops, stops.length - 1, times.length - 1);
				}
			}
			best = permute(stops, best, next, times, time, 0);
		}

		return best;
	}

	/**
	 * returns route with most bunnies given key points to stop at
	 * 
	 * @param stops         The places to stop at
	 * @param bunniesInBest The best result so far
	 * @param next          The next indexes on a shortest path (e.g. for i -> j the
	 *                      next stop is next[i][j])
	 * @param times         The times to get from one bunny to another (cannot be
	 *                      negative)
	 * @param time          The time limit
	 * @param pos           The stop to search from
	 * @return
	 */
	private static int[] permute(int[] stops, int[] bunniesInBest, int[][] next, int[][] times, int time, int pos) {
		if (pos == stops.length - 1) {
			int[] fullPath = addEnds(stops, times.length - 1);
			if (pathLength(times, fullPath) <= time) {
				int[] bunniesInStops = bunniesInPath(fullPath, next);
				bunniesInBest = bunniesInStops.length > bunniesInBest.length ? bunniesInStops : bunniesInBest;
			}
		} else {
			for (int i = pos; i < stops.length; i++) {
				int[] newStops = stops;
				int temp = newStops[i];
				newStops[i] = newStops[pos];
				newStops[pos] = temp;
				bunniesInBest = permute(newStops, bunniesInBest, next, times, time, pos + 1);
			}
		}
		return bunniesInBest;
	}

	/**
	 * Finds the stops in a path including intermediate ones
	 * 
	 * @param path The path to find all stops for
	 * @param next The next indexes on a shortest path (e.g. for i -> j the next
	 *             stop is next[i][j])
	 * @return All places that are stopped at on the path
	 */
	private static int[] bunniesInPath(int[] path, int[][] next) {
		ArrayList<Integer> bunnies = new ArrayList<Integer>();
		for (int i = 0; i < path.length - 1; i++) {
			int u = path[i];
			int v = path[i + 1];
			if (!bunnies.contains(u)) {
				bunnies.add(u);
			}
			while (u != v) {
				u = next[u][v];
				if (!bunnies.contains(u)) {
					bunnies.add(u);
				}
			}
		}
		if (!bunnies.contains(path[path.length - 1])) {
			bunnies.add(path[path.length - 1]);
		}

		int[] result = new int[bunnies.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = bunnies.get(i);
		}
		return result;
	}

	/**
	 * returns the length of a path
	 * 
	 * @param dist The weights of the edges of the graph
	 * @param path The path through the graph
	 * @return The length of the path
	 */
	private static int pathLength(int[][] dist, int[] path) {
		int result = 0;
		for (int i = 0; i < path.length - 1; i++) {
			result += dist[path[i]][path[i + 1]];
		}
		return result;
	}

	/**
	 * Returns a selection of bunny positions with the start and doors added
	 * 
	 * @param path  The path to add the start and doors to
	 * @param doors The index of the doors
	 */
	private static int[] addEnds(int[] path, int doors) {
		int[] result = new int[path.length + 2];
		System.arraycopy(path, 0, result, 1, path.length);
		result[result.length - 1] = doors;
		return result;
	}

	/**
	 * Finds the number of ways of choosing k things
	 * 
	 * @param n The number of things to choose from
	 * @param k The number of things to choose
	 * @return nCk
	 */
	private static int choose(int n, int k) {
		int result = 1;
		for (int i = 0; i < k; i++) {
			result = (result * (n - i)) / (i + 1);
		}
		return result;
	}

	/**
	 * Cycles through possible combinations of how to choose bunnies to give a given
	 * key to
	 * 
	 * @param combination The combination to increment
	 * @param pos         The index in the previousCombination to increment
	 * @param total       The value an element in the array must be less than
	 */
	private static void incrementChoose(int[] combination, int pos, int total) {
		int max = pos == combination.length - 1 ? total : combination[pos + 1];
		if (combination[pos] == max - 1) {
			incrementChoose(combination, pos - 1, total);
			combination[pos] = combination[pos - 1] + 1;
		} else {
			combination[pos] += 1;
		}
	}
}
