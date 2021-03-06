package com.example.tfm.enum

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage

enum class LanguageCode constructor(val code: Int) {
    ARABIC(FirebaseTranslateLanguage.AR),
    DUTCH(FirebaseTranslateLanguage.NL),
    CATALAN(FirebaseTranslateLanguage.CA),
    CHINESE(FirebaseTranslateLanguage.ZH),
    ENGLISH(FirebaseTranslateLanguage.EN),
    FRENCH(FirebaseTranslateLanguage.FR),
    GERMAN(FirebaseTranslateLanguage.DE),
    INDIAN(FirebaseTranslateLanguage.HI),
    ITALIAN(FirebaseTranslateLanguage.IT),
    JAPANESE(FirebaseTranslateLanguage.JA),
    KOREAN(FirebaseTranslateLanguage.KO),
    RUSSIAN(FirebaseTranslateLanguage.RU),
    SPANISH(FirebaseTranslateLanguage.ES);


    companion object{
        private val map = values().associateBy(LanguageCode::code)
        fun fromInt(type: Int) : LanguageCode = map[type]!!

        fun getLanguageBCP47Code(code: Int?): String = when(code){
                FirebaseTranslateLanguage.AR -> {"ar-SA"}
                FirebaseTranslateLanguage.NL -> {"nl-NL"}
                FirebaseTranslateLanguage.CA -> {"ca-ES"}
                FirebaseTranslateLanguage.ZH -> {"zh"}
                FirebaseTranslateLanguage.EN -> {"en-GB"}
                FirebaseTranslateLanguage.FR -> {"fr-FR"}
                FirebaseTranslateLanguage.DE -> {"de-DE"}
                FirebaseTranslateLanguage.HI -> {"hi-IN"}
                FirebaseTranslateLanguage.IT -> {"it-IT"}
                FirebaseTranslateLanguage.JA -> {"ja-JA"}
                FirebaseTranslateLanguage.KO -> {"ko-KR"}
                FirebaseTranslateLanguage.RU -> {"ru-RU"}
                FirebaseTranslateLanguage.ES -> {"es-ES"}

                else -> {"en-GB"}
        }
    }

}