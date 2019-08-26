package com.example.tfm.room.typeconverters

import androidx.room.TypeConverter
import com.example.tfm.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserListConverter {
    @TypeConverter
    fun fromJson(json: String): MutableList<User>{
        val listType = object : TypeToken<MutableList<User>>(){}.type
        return Gson().fromJson<MutableList<User>>(json, listType)
    }

    @TypeConverter
    fun toJson(users: MutableList<User>) = Gson().toJson(users)
}