# Dictionary Agent #


### What is this repository for? ###
This is a helper library for subzero player application.
**Dictionary Agent** is responsible for retrieving translations for a given set of words. Since there are multiple sources of dictionaries (e.g. MIT Wordnet binaries for English to English, simple text files and so on), there are different implementations of the interface, each one responsible for specific type of dictionary source. The interface can be extended and implemented for other kinds of sources in the future:

     public interface DictionaryService {
        Map<String, Map<String, List<String>>> retrieveDefinitions(String[] words);
    }
	
For each root word, dictionary agent returns words that are stemmed from this root word, and translation list for each stem word. E.g searching a word book would result in:
book    -  	List<String> translations
booking -   List<String> translations
booked 	-	List<String> translations
and so on. 
All  of these stem words and their translations are returned in Map<String, Map<String, List<String>>>.

### How do I contribute
Pull requests are very welcome. Feel free to provide new implementations for the interface. Biggest challenge currently is finding open source dictionary data for different languages 
(text files, binaries or anything that can be parsed and queried at runtime for stem words in minimal amount of time).


### Who do I talk to? ###

Contact me at sub0.project@gmail.com