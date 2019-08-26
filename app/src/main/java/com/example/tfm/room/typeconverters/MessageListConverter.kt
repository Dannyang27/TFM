package com.example.tfm.room.typeconverters

import androidx.room.TypeConverter
import com.example.tfm.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageListConverter {
    @TypeConverter
    fun fromJson(json: String): MutableList<Message>{
        val listType = object : TypeToken<MutableList<Message>>(){}.type
        return Gson().fromJson<MutableList<Message>>(json, listType)
    }

    @TypeConverter
    fun toJson(messages: MutableList<Message>) = Gson().toJson(messages)
}