package com.example.tfm.util

import com.example.tfm.enum.LanguageCode

object FirebaseTranslator {

    fun languageCodeFromString(languageStr: String): Int{
        var targetLanguage: Int = LanguageCode.ENGLISH.code

        when(languageStr.toUpperCase()){
            LanguageCode.ARABIC.name -> targetLanguage = LanguageCode.ARABIC.code
            LanguageCode.DANISH.name -> targetLanguage = LanguageCode.DANISH.code
            LanguageCode.DUTCH.name -> targetLanguage = LanguageCode.DUTCH.code
            LanguageCode.CATALAN.name -> targetLanguage = LanguageCode.CATALAN.code
            LanguageCode.CHINESE.name -> targetLanguage = LanguageCode.CHINESE.code
            LanguageCode.FRENCH.name -> targetLanguage = LanguageCode.FRENCH.code
            LanguageCode.GERMAN.name -> targetLanguage = LanguageCode.GERMAN.code
            LanguageCode.GREEK.name -> targetLanguage = LanguageCode.GREEK.code
            LanguageCode.HEBREW.name -> targetLanguage = LanguageCode.HEBREW.code
            LanguageCode.INDIAN.name -> targetLanguage = LanguageCode.INDIAN.code
            LanguageCode.ITALIAN.name -> targetLanguage = LanguageCode.ITALIAN.code
            LanguageCode.JAPANESE.name -> targetLanguage = LanguageCode.JAPANESE.code
            LanguageCode.KOREAN.name -> targetLanguage = LanguageCode.KOREAN.code
            LanguageCode.NORWEGIAN.name -> targetLanguage = LanguageCode.NORWEGIAN.code
            LanguageCode.POLISH.name -> targetLanguage = LanguageCode.POLISH.code
            LanguageCode.PORTUGUESE.name -> targetLanguage = LanguageCode.PORTUGUESE.code
            LanguageCode.RUSSIAN.name -> targetLanguage = LanguageCode.RUSSIAN.code
            LanguageCode.SPANISH.name -> targetLanguage = LanguageCode.SPANISH.code
        }

        return targetLanguage
    }
}