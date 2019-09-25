package com.example.tfm.enum

import com.example.tfm.R

enum class LanguageDrawable constructor(val drawable: Int) {
    ARABIC(R.drawable.ar),
    DUTCH(R.drawable.nl),
    CATALAN(R.drawable.catalan),
    CHINESE(R.drawable.china),
    FRENCH(R.drawable.fr),
    GERMANY(R.drawable.de),
    INDIAN(R.drawable.india),
    ITALIAN(R.drawable.it),
    JAPANESE(R.drawable.jp),
    KOREAN(R.drawable.ko),
    RUSSIAN(R.drawable.ru),
    SPANISH(R.drawable.es);

    companion object{
        private val map = values().associateBy(LanguageDrawable::drawable)
        fun fromInt(type: Int) : LanguageDrawable = map[type]!!
    }

}