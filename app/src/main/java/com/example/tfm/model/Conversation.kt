package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tfm.room.typeconverters.MessageListConverter
import com.example.tfm.room.typeconverters.UserConverter
import com.example.tfm.room.typeconverters.UserListConverter
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Conversation")
class Conversation (@PrimaryKey
                    @NotNull var id: String = "",
                    @TypeConverters(UserConverter::class)
                    val userOne: User? = null,
                    @TypeConverters(UserConverter::class)
                    val userTwo : User? = null,
                    @TypeConverters(MessageListConverter::class)
                    val messages: MutableList<Message> = mutableListOf(),
                    var timestamp: Long = -1,
                    @TypeConverters(UserListConverter::class)
                    val userTyping: MutableList<User> = mutableListOf(),
                    val isPrivate: Boolean = true)