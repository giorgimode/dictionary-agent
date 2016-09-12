package com.giorgimode.dictionary.api;

import com.giorgimode.dictionary.impl.WordnetDictionary;
import org.junit.Test;

import java.util.Map;

/**
 * Created by modeg on 9/11/2016.
 */
public class WordnetDictionaryTest {
    @Test
    public void retrieveDefinitions() throws Exception {
        WordnetDictionary wordnetDictionary = WordnetDictionary.getInstance();
        String[] wordsToTranslate = new String[]{"letters"};
        Map<String, String> definitions = wordnetDictionary.retrieveDefinitions(wordsToTranslate);

        definitions.entrySet().forEach(d -> System.out.println(d + "\n"));
    }

}