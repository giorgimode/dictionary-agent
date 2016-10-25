package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.exception.DictionaryException;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class WordnetDictionaryService implements DictionaryService {

    private static final String DICT_PATH = ".\\src\\main\\resources\\wordnet\\dict";
    private static URL            url;
    private static IDictionary    dict;
    private static WordnetStemmer wordnetStemmer;

    private WordnetDictionaryService(int loadPolicy) {
        try {
            url = new URL("file", null, DICT_PATH);
        } catch (MalformedURLException e) {
            throw new DictionaryException("No dictionary data found at " + url, e);
        }
        if (loadPolicy != 0) {
            dict = new RAMDictionary(url, loadPolicy);
        } else {
            dict = new Dictionary(url);
        }
        try {
            dict.open();
        } catch (IOException e) {
            throw new DictionaryReaderException("Error opening dictionary.", e);
        }
        wordnetStemmer = new WordnetStemmer(dict);
    }

    public static WordnetDictionaryService getInstance() {
        return new WordnetDictionaryService(0);
    }

    public static WordnetDictionaryService getInMemoryInstance(int loadPolicy) {
        return new WordnetDictionaryService(loadPolicy);
    }

    public void loadInMemoryDictionary() {
        if (dict instanceof IRAMDictionary && !((IRAMDictionary) dict).isLoaded()) {
            System.out.println("tarzan");
            ((IRAMDictionary) dict).load();
        }
    }

    @Override
    public Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate) {
        Map<String, Map<String, List<String>>> definitons = new HashMap<>(wordsToTranslate.length);
        Arrays.stream(wordsToTranslate).forEach(wordToTranslate -> {
            if (wordToTranslate != null && wordToTranslate.length() > 2) {
                definitons.put(wordToTranslate, retrieveDefiniton(wordToTranslate));
            }
        });
        return definitons;
    }

    private Map<String, List<String>> retrieveDefiniton(String word) {
        Map<String, List<String>> definitionMap = new HashMap<>();
        Arrays.stream(POS.values()).forEach(wordType -> {
            List<String> stemWords = wordnetStemmer.findStems(word, wordType);
            stemWords.forEach(stemWord -> {
                IIndexWord idxWord = dict.getIndexWord(stemWord, wordType);
                if (idxWord != null) {
                    List<IWordID> wordIDList = idxWord.getWordIDs();
                    wordIDList.forEach(iWordID -> {
                        IWord iWord = dict.getWord(iWordID);
                        if (iWord != null) {

                            String root = "";
                            if (iWord.getLemma() != null) {
                                // root word
                                root = iWord.getLemma().toLowerCase().equals(word.toLowerCase()) ? word.toLowerCase() : iWord.getLemma();
                            }

                            // definitions
                            String gloss = iWord.getSynset() != null ? iWord.getSynset().getGloss() : null;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder
                                    .append(WordTypeAlias.getByName(wordType.name().toLowerCase()))
                                    .append(" ")
                                    .append(gloss);

                            //   stringBuilder.append("---\n");
                            root = root.isEmpty() ? word : root;
                            List<String> list = definitionMap.get(root);
                            if (list == null) {
                                definitionMap.put(root, new ArrayList<>(Arrays.asList(stringBuilder.toString())));
                            } else {
                                list.add(stringBuilder.toString());
                            }

                        }
                    });
                }
            });
        });

        return definitionMap;
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
