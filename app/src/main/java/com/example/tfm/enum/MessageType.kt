package com.example.tfm.enum

enum class MessageType constructor(val value: Int) {
    MESSAGE(0), IMAGE(1), GIF(1), LOCATION(2);

    companion object{
        private val map = values().associateBy(MessageType::value)
        fun fromInt(type: Int) : MessageType = map[type]!!
    }
}