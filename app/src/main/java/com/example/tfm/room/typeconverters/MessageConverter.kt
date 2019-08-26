package com.example.tfm.room.typeconverters

import androidx.room.TypeConverter
import com.example.tfm.model.Message
import com.google.gson.Gson

class MessageConverter {
    @TypeConverter
    fun fromJson(json: String) =  Gson().fromJson(json, Message::class.java)

    @TypeConverter
    fun toJson(message: Message) = Gson().toJson(message)
}