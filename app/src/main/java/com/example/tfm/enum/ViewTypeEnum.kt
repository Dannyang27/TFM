package com.example.tfm.enum

enum class ViewTypeEnum constructor(val indexValue: Int) {
    OWN_MESSAGE(0),
    OTHER_MESSAGE(1),
    OWN_MEDIA(2),
    OTHER_MEDIA(3),
    LOCATION(4);

    companion object{
        private val map = values().associateBy(ViewTypeEnum::indexValue)
        fun fromInt(type: Int) : ViewTypeEnum = map[type]!!
    }
}