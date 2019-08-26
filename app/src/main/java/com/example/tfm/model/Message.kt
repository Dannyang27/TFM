package com.example.tfm.model

import com.example.tfm.enum.MessageType

data class Message (val senderName: String,
                    val receiverName: String,
                    val messageType: MessageType,
                    val body: Any?,
                    val timestamp: Long,
                    var isSent: Boolean,
                    var isReceived: Boolean,
                    val languageCode: String)