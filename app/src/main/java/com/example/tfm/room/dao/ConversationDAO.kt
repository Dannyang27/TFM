package com.example.tfm.room.dao

import androidx.room.*
import com.example.tfm.model.Conversation

@Dao
interface ConversationDAO {
    @Query("SELECT * FROM Conversation")
    fun getAll() : MutableList<Conversation>

    @Query("SELECT * FROM Conversation WHERE id = :id")
    fun getById( id: String) : Conversation

    @Query("SELECT COUNT(*) FROM Conversation")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(conversation: Conversation)

    @Update
    fun update(conversation: Conversation)

    @Delete
    fun delete(conversation: Conversation)
}