package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tfm.enum.MessageType

@Entity(tableName = "Message")
data class Message (@PrimaryKey(autoGenerate = true)
                    val id: Long = 0,
                    val senderName: String = "",
                    val receiverName: String = "",
                    val messageType: MessageType = MessageType.MESSAGE,
                    val body: Any? = null,
                    val timestamp: Long = -1,
                    var isSent: Boolean = false,
                    var isReceived: Boolean = false,
                    val languageCode: String = "")