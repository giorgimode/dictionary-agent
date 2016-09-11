package com.giorgimode.dictionary.api;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by modeg on 9/11/2016.
 */
public class WordnetServiceTest {
    @Test
    public void retrieveDefinitions() throws Exception {
        WordnetService wordnetService = WordnetService.getInstance();
        String[] wordsToTranslate = new String[]{"letters"};
        Map<String, String> definitions = wordnetService.retrieveDefinitions(wordsToTranslate);

        definitions.entrySet().forEach(d -> System.out.println(d + "\n"));
    }

}