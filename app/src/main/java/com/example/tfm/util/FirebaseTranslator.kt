package com.example.tfm.util

import android.util.Log
import com.example.tfm.enum.LanguageCode
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

object FirebaseTranslator {

    fun downloadModelIfNeeded(targetCode: String): FirebaseTranslator{
        val targetLanguage = languageCodeFromString(targetCode)

        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "Ok to start translating...")
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Failed model")
            }

        return translator
    }

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