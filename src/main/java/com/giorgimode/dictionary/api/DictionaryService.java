package com.giorgimode.dictionary.api;

import java.io.File;
import java.util.Map;

/**
 * Created by modeg on 9/4/2016.
 */
public interface DictionaryService {

/*    *//*
    * @param file dictionary text file to be parsed
    * @returns Map of words and its definitions
    * *//*
    public Map<String, String> parseDictionary(File dictionaryFile);*/

    public Map<String, String> retrieveDefinitions(String[] words);
}
