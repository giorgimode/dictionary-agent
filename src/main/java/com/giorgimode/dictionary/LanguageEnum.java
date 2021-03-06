package com.giorgimode.dictionary;

/**
 * Created by modeg on 10/30/2016.
 */
public enum LanguageEnum {
    BG_DE("bg-de"),
    BG_EN("bg-en"),
    BS_DE("bs-de"),
    BS_EN("bs-en"),
    CS_DE("cs-de"),
    CS_EN("cs-en"),
    DA_DE("da-de"),
    DA_EN("da-en"),
    DE_BG("de-bg"),
    DE_BS("de-bs"),
    DE_CS("de-cs"),
    DE_DA("de-da"),
    DE_EL("de-el"),
    DE_EN("de-en"),
    DE_EO("de-eo"),
    DE_ES("de-es"),
    DE_FI("de-fi"),
    DE_FR("de-fr"),
    DE_HR("de-hr"),
    DE_HU("de-hu"),
    DE_IS("de-is"),
    DE_IT("de-it"),
    DE_LA("de-la"),
    DE_NL("de-nl"),
    DE_NO("de-no"),
    DE_PL("de-pl"),
    DE_PT("de-pt"),
    DE_RO("de-ro"),
    DE_RU("de-ru"),
    DE_SK("de-sk"),
    DE_SQ("de-sq"),
    DE_SR("de-sr"),
    DE_SV("de-sv"),
    DE_TR("de-tr"),
    EL_DE("el-de"),
    EL_EN("el-en"),
    EN_BG("en-bg"),
    EN_BS("en-bs"),
    EN_CS("en-cs"),
    EN_DA("en-da"),
    EN_DE("en-de"),
    EN_EL("en-el"),
    EN_EN("en-en"),
    EN_EO("en-eo"),
    EN_ES("en-es"),
    EN_FI("en-fi"),
    EN_FR("en-fr"),
    EN_HR("en-hr"),
    EN_HU("en-hu"),
    EN_IS("en-is"),
    EN_IT("en-it"),
    EN_LA("en-la"),
    EN_NL("en-nl"),
    EN_NO("en-no"),
    EN_PL("en-pl"),
    EN_PT("en-pt"),
    EN_RO("en-ro"),
    EN_RU("en-ru"),
    EN_SK("en-sk"),
    EN_SQ("en-sq"),
    EN_SR("en-sr"),
    EN_SV("en-sv"),
    EN_TR("en-tr"),
    EO_DE("eo-de"),
    EO_EN("eo-en"),
    ES_DE("es-de"),
    ES_EN("es-en"),
    FI_DE("fi-de"),
    FI_EN("fi-en"),
    FR_DE("fr-de"),
    FR_EN("fr-en"),
    HR_DE("hr-de"),
    HR_EN("hr-en"),
    HU_DE("hu-de"),
    HU_EN("hu-en"),
    IS_DE("is-de"),
    IS_EN("is-en"),
    IT_DE("it-de"),
    IT_EN("it-en"),
    LA_DE("la-de"),
    LA_EN("la-en"),
    NL_DE("nl-de"),
    NL_EN("nl-en"),
    NO_DE("no-de"),
    NO_EN("no-en"),
    PL_DE("pl-de"),
    PL_EN("pl-en"),
    PT_DE("pt-de"),
    PT_EN("pt-en"),
    RO_DE("ro-de"),
    RO_EN("ro-en"),
    RU_DE("ru-de"),
    RU_EN("ru-en"),
    SK_DE("sk-de"),
    SK_EN("sk-en"),
    SQ_DE("sq-de"),
    SQ_EN("sq-en"),
    SR_DE("sr-de"),
    SR_EN("sr-en"),
    SV_DE("sv-de"),
    SV_EN("sv-en"),
    TR_DE("tr-de"),
    TR_EN("tr-en");

    private final String value;

    LanguageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LanguageEnum fromString(String lang) {
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.value.equalsIgnoreCase(lang)) {
                return languageEnum;
            }
        }
        return null;
    }
}
