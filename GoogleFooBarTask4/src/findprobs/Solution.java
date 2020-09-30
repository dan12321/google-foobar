package findprobs;

/**
 * Solution for "Doomsday Fuel"
 * 
 * @author Daniel Songi
 *
 */
public class Solution {

	/**
	 * Finds the probability of a matrix of transitions ending in each stable state
	 * given we start at index 0.
	 * 
	 * @param m The matrix
	 * @return An array of the numerators of the probabilities ending with the
	 *         denominator
	 */
	public static int[] solution(int[][] m) {
		// Find which elements are absorbers and transients
		int[] absorbers = new int[m.length];
		int[] transients = new int[m.length];
		int[] rowDenominators = new int[m.length];
		int transientsCount = 0;
		int absorbersCount = 0;
		boolean stationary = false;
		for (int i = 0; i < m.length; i++) {
			int denom = 0;
			for (int j = 0; j < m.length; j++) {
				denom += m[i][j];
			}
			if (denom == 0) {
				absorbers[absorbersCount] = i;
				absorbersCount++;
				if (i == 0) {
					stationary = true;
				}
			} else {
				rowDenominators[transientsCount] = denom;
				transients[transientsCount] = i;
				transientsCount++;
			}
		}

		// Special case. if 0 absorber position can't change
		if (stationary) {
			int[] result = new int[absorbersCount + 1];
			result[0] = 1;
			result[absorbersCount] = 1;
			return result;
		}

		// Make a sub matrix q of transients by transients to find I-q
		int[][] q = new int[transientsCount][transientsCount];
		for (int i = 0; i < transientsCount; i++) {
			for (int j = 0; j < transientsCount; j++) {
				q[i][j] = -m[transients[i]][transients[j]];
			}
		}
		for (int i = 0; i < transientsCount; i++) {
			q[i][i] += rowDenominators[i];
		}

		// Find r, the canonical form
		int[][] r = new int[transientsCount][absorbersCount];
		for (int i = 0; i < transientsCount; i++) {
			for (int j = 0; j < absorbersCount; j++) {
				r[i][j] = m[transients[i]][absorbers[j]];
			}
		}

		// The first row of inverse of I-q gives the
		// expected number of times state j is reached
		// starting from 0 before being absorbed
		int[] n = firstRowOfInverse(q);

		// Now we can calculate the probability of being
		// absorbed by an absorber state given we started
		// at 0
		int[] result = new int[absorbersCount + 1];
		for (int i = 0; i < absorbersCount; i++) {
			int expected = 0;
			for (int j = 0; j < transientsCount; j++) {
				expected += n[j] * r[j][i];
			}
			result[i] = expected;
		}

		// If you keep track of the various denominators while
		// calculating the probability it cancels to the
		// determinant of I-q
		result[absorbersCount] = n[transientsCount];

		// Simplify the answer
		int comDen = gcdArray(result);
		if (comDen != 1) {
			for (int i = 0; i < result.length; i++) {
				result[i] = result[i] / comDen;
			}
		}
		return result;
	}

	/**
	 * Returns the determinant of a matrix
	 */
	private static int det(int[][] matrix) {
		if (matrix.length == 2) {
			return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
		} else {
			int[][][] minors = findMinors(matrix);
			int result = 0;
			for (int i = 0; i < minors.length; i++) {
				if (i % 2 == 0) {
					result += matrix[i][0] * det(minors[i]);
				} else {
					result -= matrix[i][0] * det(minors[i]);
				}
			}
			return result;
		}
	}

	/**
	 * Returns the minors of the right side of a matrix
	 */
	private static int[][][] findMinors(int[][] matrix) {
		int[][][] minors = new int[matrix.length][][];
		for (int i = 0; i < matrix.length; i++) {
			int[][] minor = new int[matrix.length - 1][];
			int skipped = 0;
			for (int j = 0; j < matrix.length - 1; j++) {
				if (j == i) {
					skipped += 1;
				}
				minor[j] = java.util.Arrays.copyOfRange(matrix[j + skipped], 1, matrix.length);
			}
			minors[i] = minor;
		}
		return minors;
	}

	/**
	 * Finds the first row of the inverse of a matrix
	 * 
	 * @return An array of integers ending with the determinant
	 */
	private static int[] firstRowOfInverse(int[][] matrix) {
		if (matrix.length == 2) {
			int det = det(matrix);
			return new int[] { matrix[1][1], -matrix[0][1], det };
		} else {
			int[][][] minors = findMinors(matrix);
			int det = 0;
			int[] result = new int[matrix.length + 1];
			for (int i = 0; i < matrix.length; i++) {
				int minDet = det(minors[i]);
				if (i % 2 == 0) {
					det += minDet * matrix[i][0];
					result[i] = minDet;
				} else {
					det -= minDet * matrix[i][0];
					result[i] = -minDet;
				}
			}
			result[matrix.length] = det;
			return result;
		}
	}

	/**
	 * Returns the greatest common denominator of an array of numbers
	 */
	private static int gcdArray(int[] numbers) {
		int result = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			result = gcd(result, numbers[i]);
			if (result == 1) {
				return result;
			}
		}
		return result;
	}

	/**
	 * Returns the greatest common denominator of 2 numbers
	 * 
	 * @param The first number
	 * @param The second number
	 */
	private static int gcd(int a, int b) {
		if (a == 0) {
			return b;
		}
		return gcd(b % a, a);
	}
}
