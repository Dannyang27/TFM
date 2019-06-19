package com.example.tfm.model

import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender

data class Message (val sender: Sender,
                    val messageType: MessageType,
                    val body: Any?,
                    val timestamp: Int,
                    val languageCode: String)