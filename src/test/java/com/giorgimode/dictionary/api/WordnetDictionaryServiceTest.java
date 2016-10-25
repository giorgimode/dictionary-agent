package com.giorgimode.dictionary.api;

import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import edu.mit.jwi.data.ILoadPolicy;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by modeg on 9/11/2016.
 */
@Ignore
public class WordnetDictionaryServiceTest {
    @Test
    public void retrieveDefinitionsBothTest() throws Exception {
        String[] wordsToTranslate = new String[]{"letters", "books", "driving", "take", "let"};

        Map<String, Map<String, List<String>>> definitions = retrieveDefinitions(wordsToTranslate);
        Map<String, Map<String, List<String>>> definitionsInMemory = retrieveDefinitionsInMemory(wordsToTranslate);
        //  definitions.entrySet().forEach(d -> System.out.println(d + "\n"));
    }

    @Test
    public void retrieveDefinitionsTest() throws Exception {
        String[] wordsToTranslate = new String[]{"breaking"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitions(wordsToTranslate);
        definitions.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(list -> {
                System.out.println(list.getKey() + ": ");
                list.getValue().forEach(word -> System.out.println(word));
            });
        });
    }

    @Test
    public void retrieveDefinitionsInMemoryTest() throws Exception {
        String[] wordsToTranslate = new String[]{"letters", "books", "driving", "take", "let"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitionsInMemory(wordsToTranslate);
    }

    private Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate){
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getInstance();
        long start = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> definitions = wordnetDictionary.retrieveDefinitions(wordsToTranslate);
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS:\n" + timespent);

        return definitions;
    }

    private Map<String, Map<String, List<String>>> retrieveDefinitionsInMemory(String[] wordsToTranslate){
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.IMMEDIATE_LOAD);
        long start = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> definitions = wordnetDictionary.retrieveDefinitions(wordsToTranslate);
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS IN_MEMORY:\n" + timespent);

        return definitions;
    }

    @Test
    public void retrieveDefinitionsInMemoryTest2() throws Exception {
        String[] wordsToTranslate = new String[]{"letters"};
        WordnetDictionaryService wordnetDictionary = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.NO_LOAD);
        wordnetDictionary.loadInMemoryDictionary();
        long start = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> definitions = wordnetDictionary.retrieveDefinitions(wordsToTranslate);
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS IN_MEMORY:\n" + timespent);
    }

}