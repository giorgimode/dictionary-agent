package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.api.Dictionary;
import com.giorgimode.dictionary.exception.DictionaryException;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class WordnetDictionary implements Dictionary {

    private static       WordnetDictionary instance  = new WordnetDictionary();
    private static final String            DICT_PATH = ".\\src\\main\\resources\\wordnet\\dict";
    private URL url;
    private IDictionary dict;
    private WordnetStemmer wordnetStemmer;

    private WordnetDictionary() {
        try {
            url = new URL("file", null, DICT_PATH);
        } catch (MalformedURLException e) {
            throw new DictionaryException("No dictionary data found at " + url, e);
        }

        dict = new edu.mit.jwi.Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            throw new DictionaryReaderException("Error opening dictionary.", e);
        }
        wordnetStemmer = new WordnetStemmer(dict);
    }

    public static WordnetDictionary getInstance() {
        return instance;
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
                List<IWordID> wordIDList = idxWord.getWordIDs();
                wordIDList.forEach(iWordID -> {
                    IWord iWord = dict.getWord(iWordID);

                    // root word
                    String lemma = iWord.getLemma().toLowerCase().equals(word.toLowerCase()) ? "" : " (root: " + iWord.getLemma() + ")";

                    // definitions
                    ISynset synset = iWord.getSynset();
                    stringBuilder
                            .append(WordTypeAlias.getByName(wordType.name().toLowerCase()))
                            .append(lemma)
                            .append("\n")
                            .append(synset.getGloss())
                            .append("\n");

                    stringBuilder.append("---\n");
                });

            });
        });

        return stringBuilder.toString();
    }

    private enum WordTypeAlias {
        ADJECTIVE("adjective", "(adj.)"),
        ADVERB("adverb", "(adv.)"),
        NOUN("noun", "(n.)"),
        VERB("verb", "(v.)");

        private final String alias;
        private final String name;

        WordTypeAlias(String name, String alias) {
            this.alias = alias;
            this.name = name;
        }

        public static String getByName(String name) {
            Optional<String> aliasz = Arrays.stream(WordTypeAlias.values())
                    .filter(w -> w.name.equals(name)).findFirst().map(WordTypeAlias::getAlias);
            return aliasz.get();
        }

        public String getAlias() {
            return alias;
        }

        public String getName() {
            return name;
        }
    }
}
