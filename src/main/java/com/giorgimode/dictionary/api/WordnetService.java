package com.giorgimode.dictionary.api;

import com.giorgimode.dictionary.exception.DictionaryException;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WordnetService implements DictionaryService {

    private static WordnetService INSTANCE = new WordnetService();
    private static final String path = ".\\src\\main\\resources\\wordnet\\dict";
    private URL url;
    private IDictionary dict;
    private WordnetStemmer wordnetStemmer;

    private WordnetService() {
        try {
            url = new URL("file", null, path);
        } catch (MalformedURLException e) {
            throw new DictionaryException("No dictionary data found at " + url, e);
        }

        dict = new Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            throw new DictionaryReaderException("Error opening dictionary.", e);
        }
        wordnetStemmer = new WordnetStemmer(dict);
    }

    public static WordnetService getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, String> retrieveDefinitions(String[] wordsToTranslate) {
        Map<String, String> definitons = new HashMap<>(wordsToTranslate.length);
        Arrays.stream(wordsToTranslate).forEach(wordToTranslate ->
                definitons.put(wordToTranslate, retrieveDefiniton(wordToTranslate)));

        return definitons;
    }

    private String retrieveDefiniton(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(POS.values()).forEach(wordType -> {
            List<String> rootWords = wordnetStemmer.findStems(word, wordType);

            rootWords.forEach(rootWord -> {
                IIndexWord idxWord = dict.getIndexWord(rootWord, wordType);
                IWordID wordID = idxWord.getWordIDs().get(0);
                IWord iWord = dict.getWord(wordID);

                String lemma = iWord.getLemma().toLowerCase().equals(word.toLowerCase()) ? "" : " (root: " + iWord.getLemma() + ")";

                // synonyms
                ISynset synset = iWord.getSynset();
                stringBuilder.append("\n")
                        .append(WordTypeAlias.getByName(wordType.name().toLowerCase()))
                        .append(lemma)
                        .append("\n")
                        .append(synset.getGloss());

                // iterate over words associated with the synset
                String synonyms = synset.getWords().stream()
                        .map(IWord::getLemma)
                        .filter(w -> !w.toLowerCase().equals(rootWord.toLowerCase()))
                        .collect(Collectors.joining(", "));

                if (StringUtils.isNotBlank(synonyms)){

                    stringBuilder.append("\nsyn: ")
                            .append(synonyms);
                }

                stringBuilder.append("\n---");
            });
        });

        return stringBuilder.toString();
    }

/*    private Map<POS, String> wordTypeAlias = ImmutableMap.<POS, String>builder()
            .put(POS.ADJECTIVE, "adv.")
            .put(POS.ADVERB, "adj.")
            .put(POS.NOUN, "noun.")
            .put(POS.VERB, "verb.")
            .build();*/

    private enum WordTypeAlias{
        adjective("adjective", "adj."),
        adverb("adverb" , "adv."),
        noun("noun", "noun."),
        verb("verb", "verb.")
        ;

        private final String alias;
        private final String name;
        WordTypeAlias(String name, String alias) {
            this.alias = alias;
            this.name = name;
        }

        public static String getByName(String name) {
        Optional<String> aliasz =    Arrays.stream(WordTypeAlias.values()).map(WordTypeAlias::getName)
                    .filter(w -> w.equals(name)).findFirst();
            return  aliasz.get();
        }

        public String getAlias() {
            return alias;
        }

        public String getName() {
            return name;
        }
    }
}
