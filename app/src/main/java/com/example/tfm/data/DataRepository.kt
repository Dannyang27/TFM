package com.example.tfm.data

import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.google.firebase.auth.FirebaseAuth

object DataRepository{
    private val conversations: MutableMap<String, Conversation> = mutableMapOf()
    var user: User? = null
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

    fun addConversation(key: String, conversation: Conversation){
        conversations.putIfAbsent(key, conversation)
    }

    fun addMessage(message: Message){
        conversations[message.ownerId]?.messages?.add(message)
    }

    fun existConversation(key: String) = conversations.containsKey(key)

    fun getConversation(key: String) = conversations[key]
}