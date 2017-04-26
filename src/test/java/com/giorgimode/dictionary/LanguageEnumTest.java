package com.giorgimode.dictionary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by modeg on 4/26/2017.
 */
public class LanguageEnumTest {

    @Test
    public void testLanguageEnum() {
        assertEquals(LanguageEnum.fromString("en-de"), LanguageEnum.EN_DE);
    }

    @Test
    public void testLanguageEnumCaseInsensitive() {
        assertEquals(LanguageEnum.fromString("SV-EN"), LanguageEnum.SV_EN);
    }

    @Test
    public void testLanguageEnumIncorrect() {
        assertEquals(LanguageEnum.fromString("EN-DZZZ"), null);
    }

}