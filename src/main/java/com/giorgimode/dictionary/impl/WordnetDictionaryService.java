package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.DictionaryUtil;
import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.exception.DictionaryException;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static java.util.Collections.singletonList;

@Slf4j
public final class WordnetDictionaryService implements DictionaryService {

    private static final String EN_EN = "en-en";
    private static URL            url;
    private static IDictionary    dict;
    private static WordnetStemmer wordnetStemmer;

    private WordnetDictionaryService(int loadPolicy, String path) {
        try {
            url = new URL("file", null, path + EN_EN);
        } catch (MalformedURLException e) {
            log.error("No dictionary data found at {}. {}", url, e);
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
            log.error("Error opening dictionary. {}", e);
            throw new DictionaryReaderException("Error opening dictionary.", e);
        }
        if (!dict.isOpen()) {
            log.error("Dictionary failed to open at path {}", path);
            throw new DictionaryReaderException("Dictionary failed to open at path " + path);
        }
        wordnetStemmer = new WordnetStemmer(dict);
    }

    public static WordnetDictionaryService getInstance(String path) {
        return new WordnetDictionaryService(0, path);
    }

    public static WordnetDictionaryService getInMemoryInstance(int loadPolicy, String path) {
        return new WordnetDictionaryService(loadPolicy, path);
    }

    public static WordnetDictionaryService getImmediateInMemoryInstance(String path) {
        return new WordnetDictionaryService(ILoadPolicy.IMMEDIATE_LOAD, path);
    }

    public void loadInMemoryDictionary() {
        if (dict instanceof IRAMDictionary && !((IRAMDictionary) dict).isLoaded()) {
            ((IRAMDictionary) dict).load();
        }
    }

    @Override
    public Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate) {
        log.debug("translating words {}", Arrays.toString(wordsToTranslate));
        Map<String, Map<String, List<String>>> definitons = new HashMap<>(wordsToTranslate.length);
        Arrays.stream(wordsToTranslate).forEach(wordToTranslate -> {
            if (wordToTranslate != null && wordToTranslate.length() > 2) {
                definitons.put(wordToTranslate, retrieveDefiniton(wordToTranslate));
            }
        });
        return definitons;
    }

    private Map<String, List<String>> retrieveDefiniton(String word) {
        Map<String, List<String>> definitionMap = new TreeMap<>(DictionaryUtil.treemapComparator(word));
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
                                root = iWord.getLemma().toLowerCase().equals(word.toLowerCase()) ? word.toLowerCase()
                                        : iWord.getLemma().toLowerCase();
                            }

                            // definitions
                            String gloss = iWord.getSynset() != null ? iWord.getSynset().getGloss() : null;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder
                                    .append(WordTypeAlias.getByName(wordType.name().toLowerCase()))
                                    .append(" ")
                                    .append(gloss);

                            List<String> list = definitionMap.get(root);
                            if (list == null) {
                                definitionMap.put(root, new ArrayList<>(singletonList(stringBuilder.toString())));
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
                                            .filter(w -> w.name.equals(name))
                                            .findFirst()
                                            .map(WordTypeAlias::getAlias);
            return aliasz.orElseGet(null);
        }

        public String getAlias() {
            return alias;
        }

        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }
    }


}
