package com.example.tfm.room.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson

class AnyTypeConverter {
    @TypeConverter
    fun fromJson(json: String) =  Gson().fromJson(json, Any::class.java)

    @TypeConverter
    fun toJson(any: Any?) = Gson().toJson(any)
}