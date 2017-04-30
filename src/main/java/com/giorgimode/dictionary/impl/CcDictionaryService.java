package com.giorgimode.dictionary.impl;

import com.giorgimode.dictionary.DictionaryUtil;
import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.dictionary.api.DictionaryService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static java.util.Arrays.asList;

/**
 * Created by modeg on 10/30/2016.
 */
@Slf4j
public final class CcDictionaryService implements DictionaryService {
    private static String dictionaryDataPath;
    private static final String KEY_VALUE_SPLIT               = "-->";
    private static final String DEFINITION_SPLIT              = " && ";
    private static final String FILE_FORMAT_PROPERTY          = ".properties";
    private static final String DUPLICATE_KEY_PATTERN         = "^.*~\\d+~$";
    private static final String DUPLICATE_KEY_COUNTER_PATTERN = "~\\d+~";
    private static LanguageEnum            language;
    private static Map<String, Properties> allProperties;

    private CcDictionaryService() {
    }

    @Override
    public Map<String, Map<String, List<String>>> retrieveDefinitions(String[] words) {
        log.debug("retrieving definitions for words: {}", asList(words).toArray());
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

    @SuppressWarnings("WeakerAccess")
    public static CcDictionaryService getInstance(LanguageEnum lang, String path) {
        log.debug("loading CcDictionaryService with language {} on path {}", lang.name(), path);
        dictionaryDataPath = path;
        language = lang;
        return new CcDictionaryService();
    }

    @SuppressWarnings("WeakerAccess")
    public static CcDictionaryService getInMemoryInstance(LanguageEnum lang, String path) {
        log.debug("loading InMemory CcDictionaryService with language {} on path {}", lang.name(), path);
        dictionaryDataPath = path;
        language = lang;
        allProperties = loadAllProperties();
        return new CcDictionaryService();
    }

    private Map<String, List<String>> getMap(String rootWord, Properties prop) {
        // entries with multiple definitions in the file have keys appended with ~1~, ~2~ etc., therefore
        // duplicateKeyMap contains keys in a following way: book~1~, book~2~... These entries need to be merged
        Map<String, String> duplicateKeyMap = getValueMap(rootWord, prop);
        Map<String, List<String>> keyValueMap = new TreeMap<>(DictionaryUtil.treemapComparator(rootWord));
        duplicateKeyMap.forEach((key, value) -> {
            if (key.matches(DUPLICATE_KEY_PATTERN)) {
                key = key.replaceAll(DUPLICATE_KEY_COUNTER_PATTERN, "");
            }
            if (keyValueMap.containsKey(key)) {
                keyValueMap.get(key).add(value);
            } else {
                keyValueMap.put(key, new ArrayList<>(Collections.singletonList(value)));
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
                log.error("Invalid formatting on line: {}", definition);
                continue;
            }
            valueMap.put(valueAndDefinition[0], valueAndDefinition[1]);
        }

        return valueMap;
    }

    private Map<String, Properties> getProperties(String[] words) {
        if (allProperties != null && allProperties.size() > 0) {
            return allProperties;
        }
        Map<String, Properties> propertiesMap = new TreeMap<>();
        Arrays.stream(words)
              .filter(word -> word != null && !word.isEmpty())
              .map(word -> word.toLowerCase().substring(0, 1))
              .filter(letter -> !propertiesMap.containsKey(letter))
              .forEach(letter -> {
                  Properties prop = loadProperyFile(letter);
                  if (prop != null && !prop.isEmpty()) {
                      propertiesMap.put(letter, prop);
                  }
              });
        return propertiesMap;
    }

    private static Properties loadProperyFile(String letter) {
        Properties prop = new Properties();
        String propertyPath = dictionaryDataPath + language.getValue() + "\\" + letter + FILE_FORMAT_PROPERTY;
        try {
            prop.load(new FileReader(propertyPath));
        } catch (IOException e) {
            log.error("Property file not found at: {}", propertyPath);
            return null;
        }
        return prop;
    }

    private static Map<String, Properties> loadAllProperties() {
        Map<String, Properties> allPropertyMap = new TreeMap<>();
        String propertyDirectoryPath = dictionaryDataPath + language.getValue() + "\\";
        File folder = new File(propertyDirectoryPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            log.error("Property files not found at: {}", folder.getAbsolutePath());
            return Collections.emptyMap();
        }
        for (File listOfFile : listOfFiles) {
            String fileName = listOfFile.getName();
            if (listOfFile.isFile() && fileName.endsWith(FILE_FORMAT_PROPERTY)) {
                try {
                    Properties prop = new Properties();
                    prop.load(new FileReader(listOfFile));
                    allPropertyMap.put(fileName.replace(FILE_FORMAT_PROPERTY, ""), prop);
                } catch (IOException e) {
                    log.error("Property file not found at: {}", listOfFile.getAbsolutePath());
                }
            }
        }

        return allPropertyMap;
    }

}
