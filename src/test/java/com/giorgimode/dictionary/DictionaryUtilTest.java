package com.giorgimode.dictionary;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by modeg on 4/26/2017.
 */
public class DictionaryUtilTest {

    @Test
    public void testComparator() {
        Map<String, String> treemap = new TreeMap<>(DictionaryUtil.treemapComparator("root"));
        treemap.put("rooter", "root definition3");
        treemap.put("roots", "root definition2");
        treemap.put("root", "root definition");
        treemap.put("bazooka", "root definition4");
        treemap.put("plumbus", "no need to define");
        treemap.put("ROOT", "BIG LETTER ROOT DEFINITION");

        assertThat(treemap.keySet(), Matchers.contains("root", "bazooka", "plumbus", "rooter", "roots"));
        assertThat(treemap.get("root"), is("BIG LETTER ROOT DEFINITION"));
    }

}