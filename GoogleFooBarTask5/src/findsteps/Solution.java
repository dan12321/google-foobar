package findsteps;

import java.util.Arrays;

/**
 * Solution for "Bomb, Baby!"
 * @author Daniel Songi
 *
 */
public class Solution {
	
	/**
	 * Finds the number of steps it will take to reach a
	 * number where a step is adding the larger number to
	 * the smaller number
	 * @param x The first number
	 * @param y The second number
	 * @return The number of steps as a string or the word
	 * impossible if that
	 */
	public static String solution(String x, String y) {
		int[] a = stringToDigits(x);
		int[] b = stringToDigits(y);
		int[] one = { 1 };
		int[] zero = { 0 };
		int[] steps = { 0 };
		while ((lessThan(zero, a) && lessThan(zero, b)) && 
				(lessThan(one, a) || lessThan(one, b))){
			if (lessThan(a, b)) {
				int[][] leap = leap(a, b);
				b = leap[1];
				steps = add(steps, leap[0]);
			}
			else if (lessThan(b, a)) {
				int[][] leap = leap(b, a);
				a = leap[1];
				steps = add(steps, leap[0]);
			}
			else return "impossible";
		}
		return (Arrays.equals(a, one) && Arrays.equals(b, one)) ? digitsToString(steps) : "impossible";
	}
	
	/**
	 * Big integer take away
	 * @param a First big integer as an array of digits
	 * @param b Second big integer as an array of digits
	 * @return returns b take away a
	 */
	private static int[] takeAway(int[] a, int[] b) {
		for (int i = 1; i <= a.length; i++) {
			b = minusDidget(b, b.length - i, a[a.length - i]);
		}
		for (int i = 0; i < b.length; i ++) {
			if (b[i] != 0 || i == b.length - 1) {
				b = Arrays.copyOfRange(b, i, b.length);
				break;
			}
		}
		return b;
	}
	
	/**
	 * Takes away from one digit and handles the remainder
	 * @param a The big integer to take away from
	 * @param pos The position on "a" to take away from
	 * @param amount The amount to take away
	 * @return A copy of "a" with the "amount" taken away at the "pos"
	 */
	private static int[] minusDidget(int[] a, int pos, int amount) {
		int[] result = Arrays.copyOf(a, a.length);
		int num = result[pos];
		num -= amount;
		if (num < 0) {
			num += 10;
			result[pos] = num;
			return minusDidget(result, pos - 1, 1);
		}
		else {
			result[pos] = num;
			return result;
		}
	}
	
	/**
	 * Big integer comparison of a < b
	 * @param a Fist number as an array of digits
	 * @param b Second number as an array of digits
	 * @return true if a < b, false otherwise
	 */
	private static boolean lessThan(int[] a, int[] b) {
		if (a.length != b.length) {
			return a.length < b.length;
		} 
		else {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i]) {
					return a[i] < b[i];
				}
			}
			return false;
		}
	}
	
	/**
	 * Big integer add
	 * @param a Fist number as an array of digits
	 * @param b Second number as an array of digits
	 * @return Returns the sum as an array of digits
	 */
	private static int[] add(int[] a, int[] b) {
		int[] aCopy = Arrays.copyOf(a, a.length);
		int[] bCopy = Arrays.copyOf(b, b.length);
		if (aCopy.length < bCopy.length) {
			aCopy = copyToLength(aCopy, bCopy.length);
		}
		else if (bCopy.length < aCopy.length) {
			bCopy = copyToLength(bCopy, aCopy.length);
		}
		for (int i = aCopy.length - 1; i >= 0; i--) {
			bCopy = addDigit(bCopy, i, aCopy[i]);
		}
		return bCopy;
	}
	
	/**
	 * Adds an amount to a at a given position and handles remainder.
	 * @param a Number as array of digits
	 * @param pos The position to add to
	 * @param amount The amount to add
	 * @return A copy of "a" the the "amount" added at "pos"
	 */
	private static int[] addDigit(int[] a, int pos, int amount) {
		int[] result = Arrays.copyOf(a, a.length);
		int num = result[pos];
		num += amount;
		if (num < 10) {
			result[pos] = num;
			return result;		
		}	
		else {
			int remainder = num / 10;
			int digit = num % 10;
			result[pos] = digit;
			if (pos > 0) {
				return addDigit(result, pos - 1, remainder);
			}
			else {
				int[] newResult = new int[result.length + 1];
				newResult[0] = remainder;
				for (int i = 0; i < result.length; i++) {
					newResult[i + 1] = result[i];
				}
				return newResult;
			}
		}
	}
	
	/**
	 * Big integer multiplication
	 * @param a Fist number as an array of digits
	 * @param b Second number as an array of digits
	 * @return a * b as an array of digits
	 */
	private static int[] timesBy(int[] a, int[] b) {
		int[][] rows = new int[a.length][];
		for (int i = a.length - 1; i >= 0; i--) {
			rows[i] = new int[b.length];
			for (int j = b.length - 1; j >= 0; j--) {
				int digit = a[i] * b[j];
				rows[i] = addDigit(rows[i], j, digit);
			}
		}
		int[] total = new int[1];
		for (int i = 0; i < a.length; i++) {
			int[] shifted = new int[rows[i].length + a.length - i - 1];
			for (int j = 0; j < rows[i].length; j++) {
				shifted[j] = rows[i][j];
			}
			total = add(total, shifted);
		}
		return total;
	}
	
	/**
	 * Converts a string number into an array of digits
	 * @param a The number to convert
	 * @return "a" as an array of digits
	 */
	private static int[] stringToDigits(String a) {
		char[] charA = a.toCharArray();
		int[] result = new int[charA.length];
		for (int i = 0; i < charA.length; i++) {
			result[i] = Character.getNumericValue(charA[i]);
		}
		return result;
	}
	
	/**
	 * Converts an array of digits to a string number
	 * @param a The array to convert
	 * @return "a" as a string number
	 */
	private static String digitsToString(int[] a) {
		String result = "";
		for (int digit : a) {
			result += digit;
		}
		return result;
	}
	
	/**
	 * Makes a copy of "a" padded at the front to length "b"
	 * @param a The array to make to copy it
	 * @param b The length to extend "a"
	 * @return "a" with length "b"
	 */
	private static int[] copyToLength(int[] a, int b) {
		int[] c = new int[b];
		for (int i = a.length - 1; i >= 0; i--) {
			c[b - a.length + i] = a[i];
		}
		return c;
	}
	
	/**
	 * Similar to dividing "b" by "a" but with the remainder in
	 * (0, a] instead of [0, a)
	 * @param a The number to divide by as an array of digits
	 * @param b The number to be divided as an array of digits
	 * @return An array of integer arrays where the first is
	 * the number of steps in the leap and the second is the remainder
	 */
	public static int[][] leap(int[] a, int[] b) {
		int[] firstPart = Arrays.copyOf(b, a.length);
		if (!lessThan(a, firstPart)) {
			firstPart = Arrays.copyOf(b, a.length + 1);
		}
		int[] steps = new int[] { 0 };
		while(lessThan(timesBy(a, addDigit(steps, steps.length - 1, 1)), firstPart)) {
			steps = addDigit(steps, steps.length - 1, 1);
		}
		int[] tens = new int[b.length - firstPart.length + 1];
		tens[0] = 1;
		steps = timesBy(steps, tens);
		int[] bResult = takeAway(timesBy(a, steps), b);
		if (!lessThan(a, bResult)) {
			return new int[][] {steps, bResult};
		}
		else {
			int[][] innerResult = leap(a, bResult);
			steps = add(innerResult[0], steps);
			return new int[][] {steps, innerResult[1]};
		}
	}
}
