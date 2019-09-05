package com.example.tfm.data

import com.example.tfm.model.Conversation

object DataRepository{
    private val conversations: MutableMap<String, Conversation> = mutableMapOf()

    fun addConversation(key: String, conversation: Conversation){
        conversations.putIfAbsent(key, conversation)
    }

    fun existConversation(key: String) = conversations.containsKey(key)

    fun getConversation(key: String) = conversations[key]
}