package ie.atu.sw;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * An alternative to the class IndexBuilder, instead of building an index that
 * excludes common words the index is of their occurrences in the text only.
 * Extends IndexSetup. The text, dictionary and stop words are to be <b>supplied
 * by the user</b> using setter methods of IndexSetup. Class is intended to be
 * operated from the <b>user interface Menu</b>.
 * 
 * A concrete implementation of <code>Indexer.interface</code> and
 * <code>IndexSetup.class</code>.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 *
 * @see Menu
 * @see Indexer
 * @see IndexSetup
 * @see IndexBuilder
 *
 */
public class AlternateIndexBuilder extends IndexSetup {
	private ConcurrentSkipListMap<String, WordDetail> index = new ConcurrentSkipListMap<>();// thread safe map
	private int lineCounter;// counter as each line is parsed
	private int page = 1;// page number, one page = 40 lines

	/**
	 * Constructor of the AlternateIndexBuilder class, creates a new instance of the
	 * class.
	 */
	public AlternateIndexBuilder() {
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
	public void buildIndex() {// O(n) as all methods called are also O(n)
		int progress = 0;
		int progressSize = 100;

		virtualThreadParse(super.stopWordsFile, 1);
		printProgress(progress += 20, progressSize);

		virtualThreadParse(super.dictionaryFile, 2);
		printProgress(progress += 20, progressSize);

		parse(super.textFile);
		printProgress(progress += 20, progressSize);

		if (super.removeNullDefinition == true) {
			removeNullDefinition();
		}
		removeNoOccurance();
		printProgress(progress += 20, progressSize);

		outputIndex();
		printProgress(progress += 20, progressSize);
	}

	/**
	 * {@inheritDoc}This implementation of parse is intended for the use of parsing
	 * the text file supplied by the user. It internally passes the parsed lines to
	 * the processText method. Parsing of the dictionary and stop words files is
	 * handled by buildIndex. Text File should be set first before use.
	 * 
	 * @see #processText(String)
	 * @see #buildIndex()
	 * @see #setTextFile(String)
	 */
	public void parse(String file) {// O(n) because of loop
		try {
			Files.lines(Path.of(file)).forEach(line -> processText(line));// a loop

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
	private void virtualThreadParse(String file, int method) {// O(n) due to loop
		try {
			Files.lines(Path.of(file)).forEach(line -> Thread.startVirtualThread(() -> {// a loop
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
	public void processText(String line) {// O(n) due to loop
		line = line.toLowerCase().trim().replaceAll("[^a-zA-Z]", " ");
		Arrays.stream(line.split("\\s+")).forEach(word -> {// a loop
			if (index.containsKey(word)) {// O(log n)
				var wordDetail = index.get(word);// O(log n)
				wordDetail.addPage(page);
			} else {
				return;
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
	 * Processes each line of the common words text that <b>will</b> be included in
	 * the index. Stop Words File should be set first before use.
	 * 
	 * @param line: the line it will process
	 * @see #setStopWordsFile(String)
	 */
	public void processStopWords(String line) {// O(n) due to loop
		Arrays.stream(line.split("\\s+")).forEach(word -> {// a loop
			index.put(word.toLowerCase(), new WordDetail(word.toLowerCase()));// O(log n)
		});

	}

	/*
	 * Private method that removes any words from the index that had no occurrences
	 * in the text.
	 */
	private void removeNoOccurance() {// O(n) due to loop
		Set<Entry<String, WordDetail>> keys = index.entrySet();
		for (Entry<String, WordDetail> e : keys) {// a loop
			if (e.getValue().getPages().isEmpty() == true)// O(log n)
				index.remove(e.getKey());// O(log n)
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int uniqueWordCount() {// O(n) due to loop
		int counter = 0;
		Set<Entry<String, WordDetail>> keys = index.entrySet();
		for (Entry<String, WordDetail> e : keys) {// a loop
			if (e.getValue().isUnique() == true)// O(log n)
				counter++;
		}
		return counter;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeNullDefinition() {// O(n) due to loop
		Set<Entry<String, WordDetail>> keys = index.entrySet();
		for (Entry<String, WordDetail> e : keys) {// a loop
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
	public void printAllWords(boolean naturalOrder) {// O(n) due to loop
		var order = naturalOrder ? index.keySet() : index.descendingKeySet();
		int counter = 0;
		for (String string : order) {// a loop
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
	public void outputIndex() {// O(n) worst case due to loop
		try (FileWriter fw = new FileWriter(new File(super.outputFile))) {
			if (super.displayUniqueWordCount == true) {
				fw.write("Total Unique Words: " + uniqueWordCount() + "\n\n");// O(n) method
			}

			Set<Entry<String, WordDetail>> keys = index.entrySet();
			for (Entry<String, WordDetail> e : keys) {// a loop
				var wordDetail = e.getValue();// O(log n)
				fw.write(e.getKey() + "\n");
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
