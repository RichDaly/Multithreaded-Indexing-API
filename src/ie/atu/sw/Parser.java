package ie.atu.sw;

/**
 * The interface Parser is an <b>abstract</b> parser that has one single
 * abstract method (SAM) that must be implemented by any class using this
 * interface. A candidate for a lambda expression.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 * 
 */
public interface Parser {
	// No running time as its abstract, dependent on implementing class

	/**
	 * Parses the file given as a parameter.
	 * 
	 * @param file the file to be parsed
	 */
	public abstract void parse(String file);

}
