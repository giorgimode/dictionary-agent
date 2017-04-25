package com.giorgimode.dictionary.api;

import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by modeg on 11/1/2016.
 */
public class CcDictionaryServiceTest {
    private static final String PATH = ".\\src\\test\\resources\\";

    @Test
    public void retrieveDefinitions() throws Exception {
        CcDictionaryService ccDictionaryService = CcDictionaryService.getInMemoryInstance(LanguageEnum.EN_DE, PATH);

        String[] wordsToTranslate = new String[]{"adverseness"};
        Map<String, Map<String, List<String>>> definitions = ccDictionaryService.retrieveDefinitions(wordsToTranslate);

        assertEquals(definitions.size(), 1);
        assertTrue(definitions.keySet().contains("adverseness"));
        Map<String, List<String>> adverseness = definitions.get("adverseness");
        assertNotNull(adverseness);
        assertEquals(adverseness.get("adverseness").size(), 1);
        assertEquals(adverseness.get("adverseness").get(0), "(noun) Widrigkeit {f}");
    }

    @Test
    public void retrieveDefinitions2() throws Exception {
        CcDictionaryService ccDictionaryService = CcDictionaryService.getInMemoryInstance(LanguageEnum.EN_DE, PATH);

        String[] wordsToTranslate = new String[]{"advertent"};
        Map<String, Map<String, List<String>>> definitions = ccDictionaryService.retrieveDefinitions(wordsToTranslate);

        assertEquals(definitions.size(), 1);
        assertTrue(definitions.keySet().contains("advertent"));
        Map<String, List<String>> advertent = definitions.get("advertent");
        assertNotNull(advertent);
        assertEquals(advertent.get("advertent").size(), 2);
        assertTrue(advertent.get("advertent").contains("(adj.) aufmerksam"));
        assertTrue(advertent.get("advertent").contains("(adj.) hinweisend"));
    }

    @Test
    public void retrieveDefinitions3() throws Exception {
        CcDictionaryService ccDictionaryService = CcDictionaryService.getInMemoryInstance(LanguageEnum.EN_DE, PATH);
        assertDefinitions(ccDictionaryService);
    }

    @Test
    public void retrieveDefinitions4() throws Exception {
        CcDictionaryService ccDictionaryService = CcDictionaryService.getInstance(LanguageEnum.EN_DE, PATH);
        assertDefinitions(ccDictionaryService);
    }

    private void assertDefinitions(DictionaryService dictionaryService) {
        String[] wordsToTranslate = new String[]{"cab"};
        Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(wordsToTranslate);

        Map<String, List<String>> rootWord = definitions.get("cab");
        assertNotNull(rootWord);
        assertEquals(rootWord.get("cab").size(), 4);
        assertTrue(rootWord.get("cab").containsAll(Arrays
                .asList("(noun) Cab {n}", "(noun) Droschke {f}", "(noun) Fiaker {m} [österr.]", "(noun) Taxi {n} [schweiz. auch: {m}]")));
        assertEquals(rootWord.get("cab [driver's cab]").size(), 1);
        assertTrue(rootWord.get("cab [driver's cab]").contains("(noun) Führerhaus {n}"));

        assertEquals(rootWord.get("cab [driver's cab]").size(), 1);
        assertTrue(rootWord.get("to cab [coll.]").contains("(verb) mit dem Taxi fahren"));
    }
}