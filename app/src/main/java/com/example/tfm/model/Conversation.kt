package com.example.tfm.model

class Conversation (private var userOne: User? = null,
                    private var userTwo : User? = null,
                    private val messages: MutableList<Message> = mutableListOf(),
                    private var timestamp: Long = -1,
                    private val userTyping: MutableList<User> = mutableListOf(),
                    private val isPrivate: Boolean = true)