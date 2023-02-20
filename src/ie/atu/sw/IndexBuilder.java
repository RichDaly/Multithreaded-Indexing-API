package ie.atu.sw;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * The class IndexBuilder builds and outputs an index from a text, each word has
 * an associated definition and page index. It <b>does not</b> add supplied stop
 * words to the index. Extends IndexSetup. The text, dictionary and stop words
 * are to be <b>supplied by the user</b> using setter methods of IndexSetup.
 * Class is intended to be operated from the <b>user interface Menu</b>.
 * 
 * A concrete implementation of <code>Indexer.interface</code> and the abstract
 * <code>IndexSetup.class</code>.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 *
 * @see Menu
 * @see Indexer
 * @see IndexSetup
 * 
 */
public class IndexBuilder extends IndexSetup {
	private ConcurrentSkipListMap<String, WordDetail> index = new ConcurrentSkipListMap<>();// thread safe map
	private Set<String> stopWords = new ConcurrentSkipListSet<>();// thread safe set
	private int lineCounter; // counter as each line is parsed
	private int page = 1;// page number, one page = 40 lines

	/**
	 * Constructor of the IndexBuilder class, creates a new instance of the class.
	 */
	public IndexBuilder() {
		super();
	}

	/**
	 * {@inheritDoc} All file locations should be set before use.
	 * 
	 * @see #setTextFile(String)
	 * @see #setDictionaryFile(String)
	 * @see #setStopWordsFile(String)
	 * @see #setOutputFile(String)
	 */
	public void buildIndex() {// O(n) due to called methods
		int progress = 0;
		int progressSize = 100;

		virtualThreadParse(super.stopWordsFile, 1);// O(n) method
		printProgress(progress += 20, progressSize);// O(n) method

		parse(super.textFile);
		printProgress(progress += 20, progressSize);// O(n) method

		virtualThreadParse(super.dictionaryFile, 2);// O(n) method
		printProgress(progress += 20, progressSize);// O(n) method

		removeStopWords();// O(n) method
		if (super.removeNullDefinition == true) {
			removeNullDefinition();// O(n) method
		}
		printProgress(progress += 20, progressSize);// O(n) method

		outputIndex();// O(n) method
		printProgress(progress += 20, progressSize);// O(n) method
	}

	/**
	 * {@inheritDoc} This implementation of parse is intended for the use of parsing
	 * the text file supplied by the user. It internally passes the parsed lines to
	 * the processText method. Parsing of the dictionary and stop words files is
	 * handled by buildIndex. Text File should be set first before use.
	 * 
	 * @see #processText(String)
	 * @see #buildIndex()
	 * @see #setTextFile(String)
	 */
	public void parse(String file) {// O(n) where n is the amount of lines to parse. There is a loop
		try {
			Files.lines(Path.of(file)).forEach(line -> processText(line));// a loop.

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * A virtual thread variation of parse. Kept private as it calls another method
	 * based on parameters. Knowledge of method workings required to use it. Parses
	 * the dictionary and stop word files.
	 * 
	 * @param file: the file to parsed
	 * 
	 * @param method: the method to call
	 */
	private void virtualThreadParse(String file, int method) {// O(n) worst case.
		try (var es = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Path.of(file)).forEach(line -> es.execute(() -> {// a loop
				if (method == 1) {
					processStopWords(line);// O(n) method
				} else if (method == 2) {
					processDictionary(line);// O(log n) method
				}
			}));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc} Text File should be set first before use.
	 * 
	 * @see #setTextFile(String)
	 */
	public void processText(String line) {// O(n) where n is the amount of words to process.
		line = line.toLowerCase().trim().replaceAll("[^a-zA-Z]", " ");
		Arrays.stream(line.split("\\s+")).forEach(word -> {// a loop.
			if (index.containsKey(word)) {// O(log n)
				var wordDetail = index.get(word);// O(log n)
				wordDetail.addPage(page);
			} else {
				var wordDetail = new WordDetail(word);
				wordDetail.addPage(page);
				index.put(word, wordDetail);// O(log n)
			}
		});
		lineCounter++;
		if (lineCounter % 40 == 0) {
			page++;
		}
	}

	/**
	 * {@inheritDoc} Dictionary File should be set first before use.
	 * 
	 * @see #setDictionaryFile(String)
	 */
	public void processDictionary(String line) {// O(log n) average skip list cost
		String str[] = line.toLowerCase().trim().split(",");
		if (index.containsKey(str[0])) {// O(log n)
			var wordDetail = index.get(str[0]);// O(log n)
			wordDetail.setDefinition(line);
		} else {
			return;
		}
	}

	/**
	 * {@inheritDoc} Stop Words File should be set first before use.
	 * 
	 * @see #setStopWordsFile(String)
	 */
	public void processStopWords(String line) {// O(n) where n is the amount of words.
		Arrays.stream(line.split("\\s+")).forEach(word -> stopWords.add(word.toLowerCase()));// a loop.

	}

	/*
	 * Removes all stop words from the index as well as the "" key that results from
	 * the parsing of the files. Private as only used in buildIndex and not required
	 * to be public from implemented interface.
	 */
	private void removeStopWords() {// O(n) where n is amount of words s in stop words.
		index.remove("");
		for (String s : stopWords) {// a loop.
			index.remove(s);// O(log n)
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int uniqueWordCount() {// O(n) worst case the loop on entry set
		int counter = 0;
		Set<Entry<String, WordDetail>> keys = index.entrySet(); // O(1)
		for (Entry<String, WordDetail> e : keys) {// a loop.
			if (e.getValue().isUnique() == true)// O(log n)
				counter++;
		}
		return counter;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeNullDefinition() {// O(n) worst case loop on entry set
		Set<Entry<String, WordDetail>> keys = index.entrySet();// O(1)
		for (Entry<String, WordDetail> e : keys) {// a loop.
			if (e.getValue().hasdefinition() != true)// O(log n)
				index.remove(e.getKey());// O(log n)
		}
	}

	/**
	 * Prints all words in index to console. Five words per line in natural order
	 * (a-z) or reverse order (z-a).
	 * 
	 * @param naturalOrder <b>true</b> for natural, <b>false</b> for reverse
	 */
	public void printAllWords(boolean naturalOrder) {
		var order = naturalOrder ? index.keySet() : index.descendingKeySet();
		int counter = 0;
		for (String string : order) {
			System.out.print(string + ", ");
			counter++;
			if (counter % 5 == 0) {
				System.out.println();
			}
		}
	}

	/**
	 * {@inheritDoc} Output File should be set first before use.
	 * 
	 * @see #setOutputFile(String)
	 */
	public void outputIndex() {// O(n) worst case from loop
		try (FileWriter fw = new FileWriter(new File(super.outputFile))) {
			if (super.displayUniqueWordCount == true) {
				fw.write("Total Unique Words: " + uniqueWordCount() + "\n\n");// O(n) method
			}

			Set<Entry<String, WordDetail>> keys = index.entrySet();
			for (Entry<String, WordDetail> e : keys) {// a loop
				var wordDetail = e.getValue();// O(1) instead of O(log n) as its a set
				fw.write(e.getKey() + "\n");// O(1)
				fw.write("\tDefinitions:\n");
				fw.write("\t" + wordDetail.getDefinition() + "\n\n");
				fw.write("\tPages:\n");
				fw.write("\t" + wordDetail.getPages() + "\n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
