package com.giorgimode.dictionary;

import java.util.Comparator;

/**
 * Created by modeg on 4/8/2017.
 */

public final class DictionaryUtil {
    public static Comparator<String> treemapComparator(String rootWord) {
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

    private DictionaryUtil() {
    }
}
