package com.giorgimode.dictionary.impl;

import edu.mit.jwi.data.ILoadPolicy;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by modeg on 9/11/2016.
 */

public class WordnetDictionaryServiceTest {
    private String DICT_PATH = ".\\src\\test\\resources\\";

    @Test
    public void retrieveDefinitionsTest() throws Exception {
        String[] wordsToTranslate = new String[]{"letters"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitions(wordsToTranslate);
        assertResults(definitions);
    }

    @Test
    public void retrieveDefinitionsInMemoryTest() {
        String[] wordsToTranslate = new String[]{"letters"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitionsInMemory(wordsToTranslate);
        assertResults(definitions);
    }

    @Test
    public void retrieveDefinitionsInMemoryTest2() {
        String[] wordsToTranslate = new String[]{"letters"};
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.NO_LOAD, DICT_PATH);
        wordnetDictionary.loadInMemoryDictionary();
        Map<String, Map<String, List<String>>> definitions = wordnetDictionary.retrieveDefinitions(wordsToTranslate);
        assertResults(definitions);
    }

    private void assertResults(Map<String, Map<String, List<String>>> definitions) {
        Map<String, List<String>> letters = definitions.get("letters");
        assertNotNull(letters);
        assertThat(letters, is(notNullValue()));
        assertThat(letters.size(), is(2));
        assertThat(letters.get("letters"), hasItems("(n.) the literary culture; \"this book shows American letters at its best\"",
                "(n.) scholarly attainment; \"he is a man of letters\""));
        assertThat(letters.get("letter").size(), is(8));
        assertThat(letters.get("letter"), hasItem(
                "(n.) a written message addressed to a person or organization; \"mailed an indignant letter to the editor\""));
    }

    @Test
    public void wrongPathInMemoryTest() {
        WordnetDictionaryService dictionaryService = WordnetDictionaryService.getImmediateInMemoryInstance("wrong path");
        Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(new String[]{"letters"});
        assertTrue(definitions.isEmpty());
    }

    @Test
    public void wrongPathTest() throws Exception {
        WordnetDictionaryService dictionaryService = WordnetDictionaryService.getInstance("wrong path");
        Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(new String[]{"letters"});
        assertTrue(definitions.isEmpty());
    }

    private Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate) {
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getInstance(DICT_PATH);
        return wordnetDictionary.retrieveDefinitions(wordsToTranslate);
    }

    private Map<String, Map<String, List<String>>> retrieveDefinitionsInMemory(String[] wordsToTranslate) {
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getImmediateInMemoryInstance(DICT_PATH);
        return wordnetDictionary.retrieveDefinitions(wordsToTranslate);
    }
}