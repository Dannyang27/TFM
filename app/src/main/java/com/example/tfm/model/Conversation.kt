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
                    var userOne: String? = "",
                    var userTwo : String? = "",
                    @Ignore
                    var messages: MutableList<Message> = mutableListOf(),
                    var timestamp: Long = -1,
                    @Ignore
                    val userTyping: MutableList<User> = mutableListOf(),
                    var isPrivate: Boolean = true)