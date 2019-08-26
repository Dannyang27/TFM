package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tfm.enum.MessageType
import com.example.tfm.room.typeconverters.AnyTypeConverter

@Entity(tableName = "Message")
data class Message (@PrimaryKey(autoGenerate = true)
                    var id: Long = 0,
                    var senderName: String = "",
                    var receiverName: String = "",
                    val messageType: MessageType = MessageType.MESSAGE,
                    @TypeConverters(AnyTypeConverter::class)
                    var body: Any? = null,
                    var timestamp: Long = -1,
                    var isSent: Boolean = false,
                    var isReceived: Boolean = false,
                    var languageCode: String = "")