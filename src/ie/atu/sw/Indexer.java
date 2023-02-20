package ie.atu.sw;

/**
 * The Interface Indexer is an <b>abstract</b> blueprint of an index. It
 * specifies what the index should do, but not how it does it. Any class that
 * declares this interface must implement all its methods.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 * 
 */
public interface Indexer {
	// No running time as its abstract, dependent on implementing class

	/**
	 * Builds the index from processed information sources.
	 */
	public abstract void buildIndex();

	/**
	 * Processes each line of the text that the index is being created for.
	 * 
	 * @param line the line it will process
	 */
	public abstract void processText(String line);

	/**
	 * Processes each line of the dictionary containing definitions for the index.
	 * 
	 * @param line the line it will process
	 */
	public abstract void processDictionary(String line);

	/**
	 * Processes each line of the common words text that <b>will not</b> be included
	 * in the index.
	 * 
	 * @param line the line it will process
	 */
	public abstract void processStopWords(String line);

	/**
	 * Outputs the index.
	 */
	public abstract void outputIndex();
}
