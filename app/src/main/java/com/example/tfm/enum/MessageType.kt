package com.example.tfm.enum

enum class MessageType constructor(val value: Int) {
    MESSAGE(0), IMAGE(1), GIF(2), LOCATION(3), ATTACHMENT(4);

    companion object{
        private val map = values().associateBy(MessageType::value)
        fun fromInt(type: Int) : MessageType = map[type]!!
    }
}