package com.example.tfm.model

import androidx.room.*
import com.example.tfm.enum.MessageType
import com.example.tfm.room.typeconverters.AnyTypeConverter

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Conversation::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("ownerId"),
                        onDelete = ForeignKey.CASCADE)))

data class Message (@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "id", index = true)
                    var id: Long = 0,
                    @ColumnInfo(name = "ownerId", index = true)
                    var ownerId: String = "",
                    var senderName: String = "",
                    var receiverName: String = "",
                    val messageType: MessageType = MessageType.MESSAGE,
                    @TypeConverters(AnyTypeConverter::class)
                    var body: Any? = null,
                    var timestamp: Long = -1,
                    var isSent: Boolean = false,
                    var isReceived: Boolean = false,
                    var languageCode: String = "")