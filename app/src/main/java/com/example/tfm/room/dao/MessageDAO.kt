package com.example.tfm.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfm.model.*

@Dao
interface MessageDAO {
    @Query("SELECT * FROM Message")
    fun getAll() : MutableList<Message>

    @Query("SELECT * FROM Message WHERE id = :id")
    fun getMessageById( id: Int) : Message

    @Query("SELECT * FROM Message WHERE senderName = :name")
    fun getSentMessages( name: String) : MutableList<Message>

    @Query("SELECT * FROM Message WHERE receiverName = :name")
    fun getReceivedMessages( name: String) : MutableList<Message>

    @Query("SELECT * FROM Message WHERE ownerId = :conversationId ORDER BY timestamp")
    fun getConversationMessages(conversationId: String): LiveData<MutableList<Message>>

    @Query("SELECT * FROM (SELECT * FROM Message WHERE ownerId = :conversationId ORDER BY timestamp DESC LIMIT :limit) ORDER BY timestamp ASC")
    fun getConversationMessagesWithLimit(conversationId: String, limit: Int = 20): LiveData<MutableList<Message>>

    @Query("SELECT COUNT(*) FROM Message WHERE (ownerId = :conversationId AND isRead = :unread) ")
    fun getUnreadMessagesFromConversation(conversationId: String, unread: Boolean = false): Int

    @Query("SELECT COUNT(*) FROM Message")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)

    //PLAIN MESSAGE
    @Query("SELECT * FROM PlainMessage WHERE id= :id")
    fun getPlainMessageById(id: Long) : PlainMessageRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPlainMessage(message: PlainMessageRoomModel)

    //GIFS
    @Query("SELECT * FROM Gif WHERE id= :id")
    fun getGifById(id: Long) : GifRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addGif(gif: GifRoomModel)

    //IMAGES
    @Query("SELECT * FROM Image WHERE id = :id")
    fun getImageById(id: Long) : ImageRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addImage(image: ImageRoomModel)

    //LOCATION
    @Query("SELECT * FROM Location WHERE id = :id")
    fun getLocationById( id: Long) : LocationRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLocation(location: LocationRoomModel)
}