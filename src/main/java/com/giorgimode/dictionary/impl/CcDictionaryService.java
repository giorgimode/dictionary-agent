package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.exception.DictionaryReaderException;
import com.giorgimode.dictionary.exception.InvalidDictionaryLineException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by modeg on 10/30/2016.
 */
public final class CcDictionaryService implements DictionaryService {
    private static final String DICT_PATH                     = ".\\src\\main\\resources\\cc";
    private static       String dictionaryDataPath            = ".\\src\\main\\resources\\cc\\";
    private static final String KEY_VALUE_SPLIT               = "-->";
    private static final String DEFINITION_SPLIT              = " && ";
    private static final String FILE_FORMAT_PROPERTY          = ".properties";
    private static final String SYNTAX_FILE_NAME              = "syntax.txt";
    private static final String DUPLICATE_KEY_PATTERN         = "^.*~\\d+~$";
    private static final String DUPLICATE_KEY_COUNTER_PATTERN = "~\\d+~";
    private static Properties              properties;
    private static CcLanguageEnum          language;
    private static Map<String, Properties> allProperties;

    private CcDictionaryService(CcLanguageEnum language) {
        if (CcDictionaryService.language == null) {
            CcDictionaryService.language = language;
        }
    }

    @Override
    public Map<String, Map<String, List<String>>> retrieveDefinitions(String[] words) {
        Map<String, Properties> propertiesMap = getProperties(words);
        Map<String, Map<String, List<String>>> finalMap = new LinkedHashMap<>(words.length);
        for (String rootWord : words) {
            if (rootWord != null && !rootWord.trim().isEmpty()) {
                Properties prop = propertiesMap.get(rootWord.toLowerCase().substring(0, 1));
                if (prop != null && !prop.isEmpty()) {
                    finalMap.put(rootWord, getMap(rootWord.toLowerCase(), prop));
                }
            }
        }
        return finalMap;
    }

    private Map<String, List<String>> getMap(String rootWord, Properties prop) {
        Map<String, String> duplicateKeyMap = getValueMap(rootWord, prop);
        Map<String, List<String>> keyValueMap = new TreeMap<>(treemapComparator(rootWord));
        duplicateKeyMap.entrySet().forEach(set -> {
            String key = set.getKey();
            if (key.matches(DUPLICATE_KEY_PATTERN)) {
                key = key.replaceAll(DUPLICATE_KEY_COUNTER_PATTERN, "");
            }
            if (keyValueMap.containsKey(key)) {
                keyValueMap.get(key).add(set.getValue());
            } else {
                keyValueMap.put(key, new ArrayList<>(Collections.singletonList(set.getValue())));
            }
        });
        return keyValueMap;
    }

    private Map<String, String> getValueMap(String rootWord, Properties prop) {
        // line:  autositzbezüge=Autositzbezüge {pl}-->auto seat covers noun && Autositzbezüge {pl}(1)-->car seat covers noun &&

        // property value: Autositzbezüge {pl}-->auto seat covers noun && Autositzbezüge {pl}(1)-->car seat covers noun
        Object propertyValueObject = prop.get(rootWord);
        if (propertyValueObject == null) {
            return new TreeMap<>();
        }
        String propertyValue = String.valueOf(propertyValueObject);
        // arrayOfDefinitons: [Autositzbezüge {pl}-->auto seat covers noun], [Autositzbezüge {pl}(1)-->car seat covers noun]
        String[] arrayOfDefinitons = propertyValue.split(DEFINITION_SPLIT);
        Map<String, String> valueMap = new TreeMap<>();
        for (String definition : arrayOfDefinitons) {
            // valueAndDefinition: [Autositzbezüge {pl}], [auto seat covers noun]
            String[] valueAndDefinition = definition.split(KEY_VALUE_SPLIT);
            if (valueAndDefinition.length > 0 && valueAndDefinition.length != 2) {
                throw new InvalidDictionaryLineException("Invalid formatting on line: " + definition);
            }
            valueMap.put(valueAndDefinition[0], valueAndDefinition[1]);

        }

        return valueMap;
    }

    private Map<String, Properties> getProperties(String[] words) {
        if (allProperties != null) {
            return allProperties;
        }
        Map<String, Properties> propertiesMap = new TreeMap<>();
        Arrays.stream(words)
              .filter(word -> word != null && !word.isEmpty())
              .map(word -> word.toLowerCase().substring(0, 1))
              .filter(letter -> !propertiesMap.containsKey(letter))
              .forEach(letter -> {
                  Properties prop = loadProperyFile(letter);
                  if (!prop.isEmpty()) {
                      propertiesMap.put(letter, prop);
                  }
              });
        return propertiesMap;
    }

    public static String getDictionaryDataPath() {
        return dictionaryDataPath;
    }

    public static void setDictionaryDataPath(String dictionaryPath) {
        dictionaryDataPath = dictionaryPath;
    }

    public static CcDictionaryService getInstance(CcLanguageEnum language) {
        return new CcDictionaryService(language);
    }

    public static CcDictionaryService getInMemoryInstance(CcLanguageEnum lang) {
        language = lang;
        allProperties = loadAllProperties();
        return new CcDictionaryService(language);
    }

    private Comparator<String> treemapComparator(String rootWord) {
        return (o1, o2) -> {
            if (o1.equalsIgnoreCase(o2)) {
                return 0;
            }
            if (o1.equalsIgnoreCase(rootWord)) {
                return -1;
            }
            if (o2.equalsIgnoreCase(rootWord)) {
                return 1;
            }
            return o1.compareTo(o2);
        };
    }

    public static CcDictionaryService getInMemoryInstance(CcLanguageEnum lang, String path) {
        setDictionaryDataPath(path);
        return getInMemoryInstance(lang);
    }

    private static Properties loadProperyFile(String letter) {
        Properties prop = new Properties();
        String propertyPath = dictionaryDataPath + language.getValue() + "\\" + letter + FILE_FORMAT_PROPERTY;
        try {
            prop.load(new FileReader(propertyPath));
        } catch (IOException e) {
            throw new DictionaryReaderException("Property file not found at: " + propertyPath);
        }
        return prop;
    }

    private static Map<String, Properties> loadAllProperties() {
        Map<String, Properties> allPropertyMap = new TreeMap<>();
        String propertyDirectoryPath = dictionaryDataPath + language.getValue() + "\\";
        File folder = new File(propertyDirectoryPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            throw new DictionaryReaderException("Property files not found at: " + folder.getAbsolutePath());
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            String fileName = listOfFiles[i].getName();
            if (listOfFiles[i].isFile() && fileName.endsWith(FILE_FORMAT_PROPERTY)) {
                try {
                    Properties prop = new Properties();
                    prop.load(new FileReader(listOfFiles[i]));
                    allPropertyMap.put(fileName.replace(FILE_FORMAT_PROPERTY, ""), prop);
                } catch (IOException e) {
                    throw new DictionaryReaderException("Property file not found at: " + listOfFiles[i].getAbsolutePath());
                }
            }
        }

        return allPropertyMap;
    }

}
