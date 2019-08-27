package com.example.tfm.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tfm.room.typeconverters.MessageListConverter
import com.example.tfm.room.typeconverters.UserConverter
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Conversation")
class Conversation (@PrimaryKey
                    @NotNull var id: String = "",
                    @TypeConverters(UserConverter::class)
                    var userOne: User? = null,
                    @TypeConverters(UserConverter::class)
                    var userTwo : User? = null,
                    @TypeConverters(MessageListConverter::class)
                    var messages: MutableList<Message> = mutableListOf(),
                    var timestamp: Long = -1,
                    @Ignore
                    val userTyping: MutableList<User> = mutableListOf(),
                    var isPrivate: Boolean = true)