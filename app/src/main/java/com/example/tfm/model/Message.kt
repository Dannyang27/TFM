package com.example.tfm.model

import androidx.room.*

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
                    var messageType: Int = -1,
                    @Ignore
                    var body: MessageContent? = null,
                    var timestamp: Long = -1,
                    var isSent: Boolean = false,
                    var isReceived: Boolean = false)

@Entity(tableName = "Gif")
class GifRoomModel(@PrimaryKey
                   var id: Long = -1,
                   var url: String = "",
                   var caption: String = "")

@Entity(tableName = "Image")
class ImageRoomModel(@PrimaryKey
                     var id: Long = -1,
                     var encodedImage: String = "")

@Entity(tableName = "Location")
class LocationRoomModel(@PrimaryKey
                        var id: Long = -1,
                        var latitude: Double = -1.0,
                        var longitude: Double = -1.0,
                        var addressLine: String = "")