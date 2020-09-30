package findradius;

/**
 * Solution for "Gearing Up For Destruction"
 * 
 * @author Daniel Songi
 *
 */
public class FindRadius {
	/**
	 * Given the position of pegs on a line find the radius of the first peg such
	 * that all pegs touch
	 * 
	 * @param pegs The positions of the pegs
	 * @return returns an array representing the radius in it's simplest form where
	 *         the first number is the numerator and the second is the denominator
	 */
	public static int[] solution(int[] pegs) {
		int z = 0;
		for (int i = 1; i < pegs.length - 1; i += 2) {
			z += 2 * pegs[i];
		}
		for (int i = 2; i < pegs.length - 1; i += 2) {
			z -= 2 * pegs[i];
		}
		if (pegs.length % 2 == 1) {
			z -= (pegs[0] + pegs[pegs.length - 1]);
			if (2 * z < 1) {
				return new int[] { -1, -1 };
			} else {
				return new int[] { 2 * z, 1 };
			}
		} else {
			z += pegs[pegs.length - 1] - pegs[0];
			if (2 * z < 3) {
				return new int[] { -1, -1 };
			} else {
				return new int[] { 2 * z, 3 };
			}
		}
	}
}
