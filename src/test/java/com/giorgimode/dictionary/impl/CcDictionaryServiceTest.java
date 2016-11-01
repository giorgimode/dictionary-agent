package com.giorgimode.dictionary.impl;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by modeg on 11/1/2016.
 */
public class CcDictionaryServiceTest {
    @Test
    public void retrieveDefinitions() throws Exception {
//  String[] wordsToTranslate = new String[]{"breaking"};
        String[] wordsToTranslate = new String[]{"letters", "books", "driving", "take", "let"};
        Map<String, Map<String, List<String>>> definitions = retrieveDefinitions(wordsToTranslate);
        definitions.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(list -> {
                System.out.println(list.getKey() + ": ");
                list.getValue().forEach(word -> System.out.println(word));
            });
        });
    }


    private Map<String, Map<String, List<String>>> retrieveDefinitions(String[] wordsToTranslate){
        CcDictionaryService ccDictionaryService = CcDictionaryService.getInstance(CcLanguageEnum.EN_DE);
        long start = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> definitions = ccDictionaryService.retrieveDefinitions(wordsToTranslate);
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS:\n" + timespent);

        return definitions;
    }

}