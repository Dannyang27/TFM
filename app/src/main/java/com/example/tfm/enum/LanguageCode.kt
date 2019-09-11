package com.example.tfm.enum

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage

enum class LanguageCode constructor(val code: Int) {
    ARABIC(FirebaseTranslateLanguage.AR),
    DANISH(FirebaseTranslateLanguage.DA),
    DUTCH(FirebaseTranslateLanguage.NL),
    CATALAN(FirebaseTranslateLanguage.CA),
    CHINESE(FirebaseTranslateLanguage.ZH),
    ENGLISH(FirebaseTranslateLanguage.EN),
    FRENCH(FirebaseTranslateLanguage.FR),
    GERMAN(FirebaseTranslateLanguage.DE),
    GREEK(FirebaseTranslateLanguage.EL),
    HEBREW(FirebaseTranslateLanguage.HE),
    INDIAN(FirebaseTranslateLanguage.HI),
    ITALIAN(FirebaseTranslateLanguage.IT),
    JAPANESE(FirebaseTranslateLanguage.JA),
    KOREAN(FirebaseTranslateLanguage.KO),
    NORWEGIAN(FirebaseTranslateLanguage.NO),
    POLISH(FirebaseTranslateLanguage.PL),
    PORTUGUESE(FirebaseTranslateLanguage.PT),
    RUSSIAN(FirebaseTranslateLanguage.RU),
    SPANISH(FirebaseTranslateLanguage.ES);


    companion object{
        private val map = values().associateBy(LanguageCode::code)
        fun fromInt(type: Int) : LanguageCode = map[type]!!
    }

}