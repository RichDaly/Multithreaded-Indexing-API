package ie.atu.sw;

/**
 * The class Runner contains the main method of the application, it creates a
 * new instance of Menu and begins it operation.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 *
 * @see Menu
 */
public class Runner {

	/**
	 * The main method of the application, no command line arguments expected.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {// O(1)
		Menu menu = new Menu();
		menu.run();

	}
}
