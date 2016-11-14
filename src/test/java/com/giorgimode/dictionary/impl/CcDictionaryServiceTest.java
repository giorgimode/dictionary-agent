package com.giorgimode.dictionary.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by modeg on 11/1/2016.
 */
public class CcDictionaryServiceTest {
    private CcDictionaryService ccDictionaryService;

    @Before
    public void init() {
    }

    @Test
    public void retrieveDefinitions() throws Exception {
        ccDictionaryService = CcDictionaryService.getInMemoryInstance(CcLanguageEnum.EN_DE);

//  String[] wordsToTranslate = new String[]{"breaking"};
        String[] wordsToTranslate = new String[]{"could"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitions(wordsToTranslate);
        definitions.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(list -> {
                System.out.println(list.getKey() + ": ");
                list.getValue().forEach(word -> System.out.println(word));
            });
        });

//        retrieveDefinitions(new String[]{"letters", "books", "driving", "take", "let"});
//        retrieveDefinitions(new String[]{"letters", "books", "driving", "take", "let"});
//        retrieveDefinitions(new String[]{"letters", "books", "driving", "take", "let"});
//        retrieveDefinitions(new String[]{"letters", "books", "driving", "take", "let"});
    }


    private Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate) {
        ccDictionaryService = ccDictionaryService == null ? CcDictionaryService.getInstance(CcLanguageEnum.EN_DE) : ccDictionaryService;
        long start = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> definitions = ccDictionaryService.retrieveDefinitions(wordsToTranslate);
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS:\n" + timespent);

        return definitions;
    }

}