package com.example.tfm.room.dao

import androidx.room.*
import com.example.tfm.model.GifRoomModel
import com.example.tfm.model.ImageRoomModel
import com.example.tfm.model.LocationRoomModel
import com.example.tfm.model.Message

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
    fun getConversationMessages(conversationId: String): MutableList<Message>

    @Query("SELECT COUNT(*) FROM Message")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)

    @Query("DELETE FROM Message")
    fun deleteAll()

    // Message Type DAO

    @Query("SELECT * FROM Gif WHERE id= :id")
    fun getGifById(id: Long) : GifRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addGif(gif: GifRoomModel)

    @Update
    fun updateGif(gif: GifRoomModel)

    @Delete
    fun deleteGif(gif: GifRoomModel)

    @Query("DELETE FROM Gif where id = :id")
    fun deleteGifById(id : Long)

    @Query("DELETE FROM Gif")
    fun deleteAllGifs()

    @Query("SELECT * FROM Image WHERE id = :id")
    fun getImageById(id: Long) : ImageRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addImage(image: ImageRoomModel)

    @Update
    fun updateImage(image: ImageRoomModel)

    @Delete
    fun deleteImage(image: ImageRoomModel)

    @Query("DELETE FROM Image where id = :id")
    fun deleteImageById(id : Long)

    @Query("DELETE FROM Image")
    fun deleteAllImages()

    @Query("SELECT * FROM Location WHERE id = :id")
    fun getLocationById( id: Long) : LocationRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLocation(location: LocationRoomModel)

    @Update
    fun updateLocation(location: LocationRoomModel)

    @Delete
    fun deleteLocation(location: LocationRoomModel)

    @Query("DELETE FROM Location where id = :id")
    fun deleteLocationById(id : Long)

    @Query("DELETE FROM Location")
    fun deleteAllLocations()
}