package com.example.tfm.room.typeconverters

import androidx.room.TypeConverter
import com.example.tfm.model.User
import com.google.gson.Gson

class UserConverter {
    @TypeConverter
    fun fromJson(json: String) =  Gson().fromJson(json, User::class.java)

    @TypeConverter
    fun toJson(user: User?) = Gson().toJson(user)
}