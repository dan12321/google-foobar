package Task7;

/**
 * Solution for "Free the Bunny Prisoners".
 * 
 * @author Daniel Songi
 *
 */
public class Solution {
	/**
	 * Finds the combination of keys to give to the bunnies such that any
	 * num_required picked will be able to open any door
	 * 
	 * @param num_buns     The total number of bunnies
	 * @param num_required The number required to open a door
	 * @return The combination that uses the least keys
	 */
	public static int[][] solution(int num_buns, int num_required) {

		if (num_required == 0) {
			return new int[num_buns][0];
		}

		int bunniesPerKey = num_buns - num_required + 1;
		int keysPerBunny = choose(num_buns - 1, bunniesPerKey - 1);
		int numKeysRequired = choose(num_buns, bunniesPerKey);

		int[][] result = new int[num_buns][keysPerBunny];
		int[] rowPositions = new int[num_buns];
		int[] colPositions = new int[bunniesPerKey];

		for (int i = 0; i < bunniesPerKey; i++) {
			colPositions[i] = i;
		}

		for (int i = 0; i < numKeysRequired; i++) {
			// give key i to the bunnies in the colPositions of the result
			for (int row : colPositions) {
				result[row][rowPositions[row]] = i;
				rowPositions[row]++;
			}
			if (i != numKeysRequired - 1) {
				increment(colPositions, colPositions.length - 1, num_buns);
			}
		}
		return result;
	}

	/**
	 * Cycles through possible combinations of how to choose bunnies to give a given
	 * key to
	 * 
	 * @param previousCombination The combination to increment
	 * @param pos                 The index in the previousCombination to increment
	 * @param rowsTotal           The value an element in the array must be less
	 *                            than
	 */
	private static void increment(int[] previousCombination, int pos, int rowsTotal) {
		int max = pos == previousCombination.length - 1 ? rowsTotal : previousCombination[pos + 1];
		if (previousCombination[pos] == max - 1) {
			increment(previousCombination, pos - 1, rowsTotal);
			previousCombination[pos] = previousCombination[pos - 1] + 1;
		} else {
			previousCombination[pos] += 1;
		}
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
}
