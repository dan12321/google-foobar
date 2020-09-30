package LuckyFind;

import java.util.Arrays;

/**
 * Solution for "Find the Access Codes"
 * 
 * @author Daniel Songi
 *
 */
public class LuckyFind {

	/**
	 * Finds the number of lucky triples in a list
	 * 
	 * @param l The list to find the lucky triples of
	 * @return The number of lucky triples
	 */
	public static int solution(int[] l) {
		int result = 0;
		for (int i = 0; i < l.length - 2; i++) {
			int[] Ys = findMultiplicators(l, i);
			for (int j = 0; j < Ys.length; j++) {
				int[] Zs = findMultiplicators(Ys, j);
				result += Zs.length;
			}
		}
		return result;
	}

	/**
	 * Finds the numbers in the list for which the number at idx is a factor
	 * 
	 * @param l   The list to search
	 * @param idx The index of the factor
	 * @return A list of numbers in the list where l[idx] is a factor
	 */
	private static int[] findMultiplicators(int[] l, int idx) {
		int[] results = new int[l.length - idx - 1];
		int resultLength = 0;
		for (int i = idx + 1; i < l.length; i++) {
			if (l[i] % l[idx] == 0) {
				results[resultLength] = l[i];
				resultLength++;
			}
		}
		return Arrays.copyOf(results, resultLength);
	}
}
