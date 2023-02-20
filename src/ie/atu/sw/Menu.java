package ie.atu.sw;

import java.util.Scanner;

/**
 * Menu is a command line <b>User Interface</b> for the application, it presents
 * a menu with multiple options for setting file locations, building an index,
 * actions upon that index and quitting the application.
 * 
 * @author Richard Daly
 * @version 1
 * @since Java 19.
 * 
 */
public class Menu {
	private IndexBuilder index; // Instance of IndexBuilder used by class
	private AlternateIndexBuilder altindex; // Instance of AlternateIndexBuilder used by class
	private boolean keepRunning = true; // Boolean value that keeps application running
	private Scanner scanner; // Instance of Scanner used by class

	/**
	 * Constructor of the Menu class, creates a new instance of the class.
	 */
	public Menu() {// O(1) just creating a new instance of menu.
		this.scanner = new Scanner(System.in); // Set Scanner to read system input
		// One instance of each index so can be passed to method execute in LSP fashion
		index = new IndexBuilder();
		altindex = new AlternateIndexBuilder();
	}

	/**
	 * Starts the instance of the class and presents the command line user
	 * interface.
	 */
	public void run() { // O(n) because of loop where n is the amount of input from user
		while (keepRunning) { // while loop with a boolean to keep application running
			try {
				showOptions();
				int choice = Integer.parseInt(scanner.next()); // parse an integer from the next input
				switch (choice) {
				case 1 -> specifyTextFile();
				case 2 -> configureDictionary();
				case 3 -> configureStopWords();
				case 4 -> specifyOutputFile();
				case 5 -> buildAndOutput();
				case 6 -> printWords();
				case 7 -> quit();
				default -> invalidChoice();
				}
			} catch (Exception e) {// catch exceptions caused by scanner
				invalidInput();
			}
		}
	}

	/*
	 * Private method that prints to console the user interface and associated
	 * options available.
	 */
	private void showOptions() {// O(1) straight forward print statements
		System.out.println("************************************************************");
		System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
		System.out.println("*                                                          *");
		System.out.println("*              Virtual Threaded Text Indexer               *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Text File");
		System.out.println("(2) Configure Dictionary");
		System.out.println("(3) Configure Stop Words");
		System.out.println("(4) Specify Output File");
		System.out.println("(5) Build Index and Output");
		System.out.println("(6) Print all words to console");
		System.out.println("(7) Quit");

		System.out.print("Select Option [1-7]>");
		System.out.println();
	}

	/*
	 * Private method that sets Text File location to input for each index instance.
	 * 
	 * @see IndexSetup
	 */
	private void specifyTextFile() {// 0(1) no loops, runs the same every time
		System.out.println("Please input path to the Text File >");
		String choice = scanner.next();
		index.setTextFile(choice);
		altindex.setTextFile(choice);
		System.out.println("Text File Set.\n");
	}

	/*
	 * Private method that sets Dictionary File location to input for each index
	 * instance.
	 * 
	 * @see IndexSetup
	 */
	private void configureDictionary() {// 0(1) no loops, runs the same every time
		System.out.println("Please input path to the Dictionary File >");
		String choice = scanner.next();
		index.setDictionaryFile(choice);
		altindex.setDictionaryFile(choice);
		System.out.println("Dictionary File Set.\n");
	}

	/*
	 * Private method that sets Stop Words File location to input for each index
	 * instance.
	 * 
	 * @see IndexSetup
	 */
	private void configureStopWords() {// 0(1) no loops, runs the same every time
		System.out.println("Please input path to the Stop Words File >");
		String choice = scanner.next();
		index.setStopWordsFile(choice);
		altindex.setStopWordsFile(choice);
		System.out.println("Stop Words File Set.\n");
	}

	/*
	 * Private method that sets Output File location to input for each index
	 * instance.
	 * 
	 * @see IndexSetup
	 */
	private void specifyOutputFile() {// 0(1) no loops, runs the same every time
		System.out.println("Please input path to the output location >");
		String choice = scanner.next();
		index.setOutputFile(choice);
		altindex.setOutputFile(choice);
		System.out.println("Output File Set.\n");
	}

	/*
	 * Private method that presents choice on indexer to be used, builds it and then
	 * outputs to file.
	 * 
	 * @throws Exception if file locations cannot be accessed
	 */
	private void buildAndOutput() throws Exception {// O(n) due to execute method
		System.out.println("What index do you wish to execute?");
		System.out.println("(1) Index that excludes Stop Words.");
		System.out.println("(2) Alternate Index of Stop Words only.");
		int choice = Integer.parseInt(scanner.next()); // parse an integer from the next input
		switch (choice) {
		case 1:
			execute(index);// O(n) method
			break;
		case 2:
			execute(altindex);// O(n) method
			break;
		default:
			invalidInput();
			break;
		}
	}

	/*
	 * Private method that presents choice to display unique words count in index.
	 * 
	 * @see IndexSetup
	 */
	private void displayUniqueWords() {// 0(1) no loops, runs the same every time
		System.out.println("Do you wish to display total unique words at the start of the index?");
		System.out.println("(1) Yes");
		System.out.println("(2) No");
		int choice = Integer.parseInt(scanner.next()); // parse an integer from the next input
		switch (choice) {
		case 1:
			index.setDisplayUniqueWordCount(true);
			altindex.setDisplayUniqueWordCount(true);
			break;
		case 2:
			index.setDisplayUniqueWordCount(false);
			altindex.setDisplayUniqueWordCount(false);
			break;
		default:
			invalidInput();
			break;
		}
	}

	/*
	 * Private method that presents choice to remove words with null definitions
	 * from index. If no definitions were found in the dictionary file for the word.
	 * 
	 * @see IndexSetup
	 */
	private void removeNullDefinition() {// 0(1) no loops, runs the same every time
		System.out.println("Do you wish to remove words with no definition from the index?");
		System.out.println("(1) Yes");
		System.out.println("(2) No");
		int choice = Integer.parseInt(scanner.next());
		switch (choice) {
		case 1:
			index.setRemoveNullDefinition(true);
			altindex.setRemoveNullDefinition(true);
			break;
		case 2:
			index.setRemoveNullDefinition(false);
			altindex.setRemoveNullDefinition(false);
			break;
		default:
			invalidInput();
			break;
		}
	}

	/*
	 * Private method begins execution of chosen Indexer. As Indexer is an
	 * interface, a concrete implementation of Indexer must be used. LSP in action.
	 * 
	 * @param index: the indexer to be used
	 * 
	 * @throws Exception if file locations cannot be accessed
	 * 
	 * @see Indexer
	 */
	private void execute(Indexer index) throws Exception {// O(n) due to buildIndex()
		displayUniqueWords();// O(1) method
		removeNullDefinition();// O(1) method
		index.buildIndex();// O(n) method
	}

	/*
	 * Private method presenting multiple choices in preparation for printing all
	 * words from index to the console. Warns that index should have been built
	 * prior to using this option.
	 * 
	 * @see IndexBuilder
	 * 
	 * @see AlternateIndexBuilder
	 */
	private void printWords() {// O(n) method due to called methods
		System.out.println("[NB] Index should be built first before using this option");
		System.out.println("(1) Continue");
		System.out.println("(2) Go Back");
		int choice1 = Integer.parseInt(scanner.next());
		switch (choice1) {
		case 1:
			break;
		case 2:
			return;
		default:
			invalidInput();
			break;
		}
		boolean order = true;// variable to store selection to be used later in method
		System.out.println("What order do you want words to print in?");
		System.out.println("(1) Natural Order (a-z)");
		System.out.println("(2) Reverse Order (z-a)");
		int choice2 = Integer.parseInt(scanner.next());
		switch (choice2) {
		case 1:
			order = true;
			break;
		case 2:
			order = false;
			break;
		default:
			invalidInput();
			break;
		}
		System.out.println("What index did you build?");
		System.out.println("(1) Index that excludes Stop Words.");
		System.out.println("(2) Alternate Index of Stop Words only.");
		int choice3 = Integer.parseInt(scanner.next());
		switch (choice3) {
		case 1:
			index.printAllWords(order);// O(n) method
			System.out.println();
			break;
		case 2:
			altindex.printAllWords(order);// O(n) method
			System.out.println();
			break;
		default:
			invalidInput();
		}
	}

	/*
	 * Private method to quit the application.
	 */
	private void quit() {// 0(1) no loops, runs the same every time
		System.out.println("[INFO] Shutting down");
		keepRunning = false; // closes the while loop in run
	}

	/*
	 * Private method that prints message that an invalid choice was chosen.
	 */
	private void invalidChoice() {// 0(1) no loops, runs the same every time
		System.out.println("[ERROR] Invalid Option: Please select from the options shown");
		System.out.println();
	}

	/*
	 * Private method that prints message that an invalid input entered.
	 */
	private void invalidInput() {// 0(1) no loops, runs the same every time
		System.out.println("[ERROR] Invalid Input, Please try again");
		System.out.println();
	}
}
