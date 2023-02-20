package ie.atu.sw;

/**
 * The class IndexSetup is an <b>abstract</b> class that contains instance
 * variable setter methods for any class extending it and acts as a
 * <b>superclass</b> for that class. A subclass of this class must implement its
 * abstract methods.
 * 
 * The instance variables are defined as protected and therefore are visible and
 * usable by a subclass of this class. Implements Indexer, Parser and
 * ProgressBar.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 * 
 * @see Indexer
 * @see Parser
 * @see ProgressBar
 * 
 */
public abstract class IndexSetup implements Indexer, Parser, ProgressBar {
	/** Instance variable for display unique word count option */
	protected boolean displayUniqueWordCount;
	/** Instance variable for remove null definitions option */
	protected boolean removeNullDefinition;
	/** file location for stop words to be parsed */
	protected String stopWordsFile;
	/** file location for dictionary to be parsed */
	protected String dictionaryFile;
	/** file location for text to be parsed */
	protected String textFile;
	/** file location for output of index */
	protected String outputFile;

	/**
	 * Constructor of the IndexSetup class, as class is abstract a instance of this
	 * class is not intended to be created.
	 */
	public IndexSetup() {
		super();
	}

	/**
	 * Returns the amount of unique words in index, unique is classified as only
	 * occurring once.
	 * 
	 * @return int value of unique words
	 */
	public abstract int uniqueWordCount();// Running time decided by implementing class

	/**
	 * Remove any words in the index with a null definition. Words that had no
	 * definition in the dictionary file processed.
	 */
	public abstract void removeNullDefinition();// Running time decided by implementing class

	/**
	 * Sets if the index displays the unique word count at start of index.
	 * 
	 * @param displayUniqueWordCount boolean true or false
	 */
	public void setDisplayUniqueWordCount(boolean displayUniqueWordCount) {// O(1) one action - set
		this.displayUniqueWordCount = displayUniqueWordCount;
	}

	/**
	 * Sets if index removes null definitions.
	 * 
	 * @param removeNullDefinition boolean true or false
	 */
	public void setRemoveNullDefinition(boolean removeNullDefinition) {// O(1) one action - set
		this.removeNullDefinition = removeNullDefinition;
	}

	/**
	 * Sets stop words file to passed parameter.
	 * 
	 * @param stopWordsFile String for file location
	 */
	public void setStopWordsFile(String stopWordsFile) {// O(1) one action - set
		this.stopWordsFile = stopWordsFile;
	}

	/**
	 * Sets dictionary file to passed parameter.
	 * 
	 * @param dictionaryFile String for file location
	 */
	public void setDictionaryFile(String dictionaryFile) {// O(1) one action - set
		this.dictionaryFile = dictionaryFile;
	}

	/**
	 * Sets text file to passed parameter.
	 * 
	 * @param textFile String for file location
	 */
	public void setTextFile(String textFile) {// O(1) one action - set
		this.textFile = textFile;
	}

	/**
	 * Sets output file to passed parameter.
	 * 
	 * @param outputFile String for file location
	 */
	public void setOutputFile(String outputFile) {// O(1) one action - set
		this.outputFile = outputFile;
	}
}
