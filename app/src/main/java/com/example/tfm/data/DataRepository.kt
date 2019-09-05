package com.example.tfm.data

import com.example.tfm.model.Conversation
import com.example.tfm.model.Message

object DataRepository{
    private val conversations: MutableMap<String, Conversation> = mutableMapOf()

    fun addConversation(key: String, conversation: Conversation){
        conversations.putIfAbsent(key, conversation)
    }

    fun addMessage(message: Message){
        conversations[message.ownerId]?.messages?.add(message)
    }

    fun existConversation(key: String) = conversations.containsKey(key)

    fun getConversation(key: String) = conversations[key]
}