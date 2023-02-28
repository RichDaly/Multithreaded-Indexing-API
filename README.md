# Multithreaded-Indexing-API
College Assignment to create a Multithreaded Indexing API that has a menu driven UI using virtual threads. 
Submitted January 2023 for Advanced Object Orientated Programming Module.

# Running

Runnable using the following command: java --enable-preview -cp ./indexer.jar ie.atu.sw.Runner which
enables the virtual threads preview features.

Command Line User Interface:

Options 1 through 4 are used to specify paths to files and index output location. These should all be
complete before using option 5 to build and output the index.

Option 5 presents multiple choices: the first choice is two index types are available, the first excludes
stop words and the second an index of the stop words. The second choice is to display a unique word
count (one occurrence) at the beginning of the index. The third choice is to remove words with null
definitions (no definition was found in dictionary supplied). The index will then be built and output to
specified output location. If you want to build both indexes in one instance of the running the
application, specify a new output location (option 4) prior to building the second index to avoid
overwriting previous index.

Option 6 presents options to print all words in index (5 per line) to the console in natural or reverse
alphabetical order. The index you wish to use should have been built prior using option 5, the UI warns
the user of this.

Option 7 quits the application.

Design:

Designed in a tree hierarchy fashion, with an interface indexer as the root, an abstract class in the
middle that implements multiple interfaces (see design.png) and two concrete implementations of
both as the children.

The menu class builds both indexes through LSP by using the interface indexer as a parameter to
execute.

Extras:
➢ Alternate Index
➢ Total Unique words
➢ Words printed in sorted/reverse sorted order
➢ Remove words with null definition (no definition in dictionary)
