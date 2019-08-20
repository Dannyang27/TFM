package com.example.tfm.enum

enum class ViewTypeEnum constructor(val indexValue: Int) {
    MESSAGE(0),
    MEDIA(1),
    LOCATION(2);

    companion object{
        private val map = values().associateBy(ViewTypeEnum::indexValue)
        fun fromInt(type: Int) : ViewTypeEnum = map[type]!!
    }
}