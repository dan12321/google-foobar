package findlargest;

/**
 * Solution for "Please Pass the Coded Messages"
 * 
 * @author Daniel Songi
 *
 */
public class Solution {
	public static int solution(int[] l) {
		int sum = 0;
		for (int d : l) {
			sum += d;
		}
		int rem = sum % 3;
		java.util.Arrays.sort(l);
		if (rem == 0) {
			return arrayToNumber(l);
		} else {
			return findLargestNumber(l, rem);
		}
	}

	/**
	 * Finds largest number divisible by 3 of a sorted array where the remainder of
	 * the sum of said array is non zero.
	 * 
	 * @param l         The sorted array of numbers.
	 * @param remainder The current remainder of the sum of the array
	 * @return The largest int that can be made from l such that it is divisible by
	 *         3.
	 */
	private static int findLargestNumber(int[] l, int remainder) {
		int[] indexesToRemove = findIndexesToDelete(l, remainder);
		if (indexesToRemove[0] != -1) {
			int[] newArray = new int[l.length - 1];
			// Copy array without the index to remove
			for (int i = 0; i < indexesToRemove[0]; i++) {
				newArray[i] = l[i];
			}
			for (int i = indexesToRemove[0] + 1; i < l.length; i++) {
				newArray[i - 1] = l[i];
			}
			return arrayToNumber(newArray);
		} else if (indexesToRemove[1] != -1 && indexesToRemove[2] != -1) {
			int first = indexesToRemove[1] < indexesToRemove[2] ? indexesToRemove[1] : indexesToRemove[2];
			int second = indexesToRemove[1] < indexesToRemove[2] ? indexesToRemove[2] : indexesToRemove[1];
			// Copy array without the indexes to remove
			int[] newArray = new int[l.length - 2];
			for (int i = 0; i < first; i++) {
				newArray[i] = l[i];
			}
			for (int i = first + 1; i < second; i++) {
				newArray[i - 1] = l[i];
			}
			for (int i = second + 1; i < l.length; i++) {
				newArray[i - 2] = l[i];
			}
			return arrayToNumber(newArray);
		} else {
			return 0;
		}
	}

	/**
	 * Translates an array of digits to a number where the place of a digit in the
	 * array is it's place in the number
	 * 
	 * @param array The array to translate
	 * @return An int
	 */
	private static int arrayToNumber(int[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			result += array[i] * Math.pow(10, i);
		}
		return result;
	}

	/**
	 * Finds what indexes need to be deleted in a sorted list for it's sum to be
	 * divisible by 3 idealRemainder: remainder of a number such that only it needs
	 * to be removed.
	 * 
	 * @param l              The sorted list
	 * @param idealRemainder difference needed for the list to be divisible by 3
	 * @return {only has to be removed by itself, remove with last, remove with
	 *         second}
	 */
	private static int[] findIndexesToDelete(int[] l, int idealRemainder) {
		int iIdeal = -1;
		int iFirstNonIdeal = -1;
		int iSecondNonIdeal = -1;
		for (int i = 0; i < l.length; i++) {
			if (l[i] % 3 == idealRemainder) {
				iIdeal = i;
				break;
			} else if (l[i] % 3 == 3 - idealRemainder && iSecondNonIdeal == -1) {
				if (iFirstNonIdeal == -1) {
					iFirstNonIdeal = i;
				} else {
					iSecondNonIdeal = i;
					break;
				}
			}
		}
		return new int[] { iIdeal, iFirstNonIdeal, iSecondNonIdeal };
	}
}
