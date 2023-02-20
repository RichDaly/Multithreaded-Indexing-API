package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

/**
 * The class WordDetail associates a word with a definition and the pages it
 * occurs on. It has suite of methods to get/set and return boolean values on
 * the information it contains.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 *
 */
public class WordDetail {
	private String word;// word the class relates to
	private String definition;// definition of the word
	private List<Integer> pages = new ArrayList<>();// pages the word occurs on

	/**
	 * Sole Constructor of the class, creates a new instance of the class with an
	 * associated word.
	 * 
	 * @param word the word to be associated with instance of class
	 */
	public WordDetail(String word) {// O(1) just creates a new instance
		this.word = word;
	}

	/**
	 * Checks if the word has a definition.
	 * 
	 * @return true if <b>has</b> definition / false is definition is <b>null</b>
	 */
	public boolean hasdefinition() {// O(1) one action only
		return definition != null ? true : false;
	}

	/**
	 * Checks to see if the word is unique: classified as having <b>only one</b>
	 * occurrence.
	 * 
	 * @return true if unique / false if not unique
	 */
	public boolean isUnique() {// O(1) one action only
		return pages.size() == 1 ? true : false;
	}

	/**
	 * Sets the definition for the word.
	 * 
	 * @param definition the definition of the word
	 */
	public void setDefinition(String definition) {// O(1) one action only
		this.definition = definition;
	}

	/**
	 * Gets a copy of the word's definition.
	 * 
	 * @return String the word definition
	 */
	public String getDefinition() {// O(1) one action only
		var copy = definition;
		return copy;
	}

	/**
	 * Adds the page to a List of pages the word occurs on.
	 * 
	 * @param page the page number
	 */
	public void addPage(int page) {// O(1) one action only
		pages.add(page);// O(1) ArrayList action
	}

	/**
	 * Gets a copy of the List of pages the word occurs on.
	 * 
	 * @return List of page numbers
	 */
	public List<Integer> getPages() {// O(1) direct copy of list
		var copy = pages;
		return copy;
	}

	/**
	 * Gets a copy of the word.
	 * 
	 * @return the word
	 */
	public String getWord() {// O(1) one action only
		var copy = word;
		return copy;
	}

}
