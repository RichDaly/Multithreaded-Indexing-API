package ie.atu.sw;

/**
 * The interface ProgressBar contains one method that prints a progress bar to
 * the console.
 * 
 * Is a default method and ready to be used without the implementing class
 * having to specify the implementation or override it.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 * 
 */
public interface ProgressBar {

	/**
	 * Prints a progress bar that indicates the progress towards completion of the
	 * task utilizing this method, represented as a percentage.
	 * 
	 * @param progress the current integer value indicating progress towards
	 *                 completion
	 * @param total    the size of the progress bar/value indicating completion
	 */
	public default void printProgress(int progress, int total) {// O(n) because of loop
		if (progress > total)
			return;
		int size = 50;
		char done = '=';
		char todo = ' ';

		int complete = (100 * progress) / total;
		int completeLen = size * complete / 100;

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {// a loop
			sb.append((i < completeLen) ? done : todo);
		}

		System.out.print("\r" + sb + "] " + complete + "%");

		if (progress == total)
			System.out.println("\n");

	}
}
