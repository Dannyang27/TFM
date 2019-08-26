package com.example.tfm.room.dao

import androidx.room.*
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

    @Query("SELECT COUNT(*) FROM Message")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)

    @Query("DELETE FROM Message")
    fun deleteAll()
}