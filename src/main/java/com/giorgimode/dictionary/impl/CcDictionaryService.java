package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import com.giorgimode.dictionary.exception.InvalidDictionaryLineException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by modeg on 10/30/2016.
 */
public class CcDictionaryService implements DictionaryService {
    private static final String DICT_PATH = ".\\src\\main\\resources\\cc";
    private static String propertyPath;
    private static final String KEY_VALUE_SPLIT               = "-->";
    private static final String DEFINITION_SPLIT              = " && ";
    private static final String FILE_FORMAT_PROPERTY          = ".properties";
    private static final String SYNTAX_FILE_NAME              = "syntax.txt";
    private static final String DUPLICATE_KEY_PATTERN         = "^.*~\\d+~$";
    private static final String DUPLICATE_KEY_COUNTER_PATTERN = "^~\\d+~$";
    private static Properties properties;

    public CcDictionaryService(CcLanguageEnum language) {

    }

    @Override
    public Map<String, Map<String, List<String>>> retrieveDefinitions(String[] words) {
        Map<String, Map<String, List<String>>> finalMap = new HashMap<>(words.length);
        for (String rootWord : words) {
            finalMap.put(rootWord, getMap(rootWord));
        }
        return null;
    }

    private Map<String, List<String>> getMap(String rootWord) {
        Map<String, String> duplicateKeyMap = getValueMap(rootWord);
        Map<String, List<String>> keyValueMap = new TreeMap<>();
        duplicateKeyMap.entrySet().forEach(set -> {
            String key = set.getKey();
            if (key.matches(DUPLICATE_KEY_PATTERN)) {
                key = key.replaceAll(DUPLICATE_KEY_COUNTER_PATTERN, "");
            }
            if (keyValueMap.containsKey(key)) {
                keyValueMap.get(key).add(set.getValue());
            } else {
                keyValueMap.put(key, Arrays.asList(set.getValue()));
            }
        });
        return keyValueMap;
    }

    private Map<String, String> getValueMap(String rootWord) {
        Properties prop = getProperties();
        // line:  autositzbezüge=Autositzbezüge {pl}-->auto seat covers	noun && Autositzbezüge {pl}(1)-->car seat covers	noun &&

        // property value: Autositzbezüge {pl}-->auto seat covers	noun && Autositzbezüge {pl}(1)-->car seat covers	noun
        String propertyValue = String.valueOf(prop.get(rootWord));
        // arrayOfDefinitons: [Autositzbezüge {pl}-->auto seat covers	noun], [Autositzbezüge {pl}(1)-->car seat covers	noun]
        String arrayOfDefinitons[] = propertyValue != null ? propertyValue.split(DEFINITION_SPLIT) : new String[0];
        Map<String, String> valueMap = new TreeMap<>();
        for (String definition : arrayOfDefinitons) {
            // valueAndDefinition: [Autositzbezüge {pl}], [auto seat covers	noun]
            String[] valueAndDefinition = definition.split(KEY_VALUE_SPLIT);
            if (valueAndDefinition.length > 0 && valueAndDefinition.length != 2) {
                throw new InvalidDictionaryLineException("Invalid formatting on line: " + definition);
            }
            valueMap.put(valueAndDefinition[0], valueAndDefinition[1]);

        }

        return valueMap;
    }

    private Properties getProperties() {
        if (properties != null)
            return  properties;
        return loadProperties();
    }

    public static String getPropertyPath() {
        return propertyPath;
    }

    public static void setPropertyPath(String propertyPath) {
        propertyPath = propertyPath;
    }

    public static CcDictionaryService getInstance(CcLanguageEnum language) {
        return new CcDictionaryService(language);
    }

    public static CcDictionaryService getInMemoryInstance(CcLanguageEnum language) {
        properties = loadProperties();
        return new CcDictionaryService(language);
    }

    private static Properties loadProperties(){
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(getPropertyPath()));
        } catch (IOException e) {
            throw new DictionaryReaderException("Property file not found at: " + getPropertyPath());
        }
        return prop;
    }

}
