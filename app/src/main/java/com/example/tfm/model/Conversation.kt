package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Conversation")
class Conversation (@PrimaryKey(autoGenerate = true)
                    private val id: String? = null,
                    private var userOne: User? = null,
                    private var userTwo : User? = null,
                    private val messages: MutableList<Message> = mutableListOf(),
                    private var timestamp: Long = -1,
                    private val userTyping: MutableList<User> = mutableListOf(),
                    private val isPrivate: Boolean = true)