package com.example.tfm.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfm.model.Conversation
import com.example.tfm.model.ConversationTuple

@Dao
interface ConversationDAO {
    @Query("SELECT * FROM Conversation ORDER BY timestamp DESC")
    fun getAll() : LiveData<MutableList<Conversation>>

    @Query("SELECT * FROM Conversation WHERE id = :id")
    fun getById( id: String) : Conversation

    @Query("SELECT id FROM Conversation WHERE userOneEmail = :email or userTwoEmail = :email")
    fun getConvesationIdsFromEmail( email: String) : MutableList<String>

    @Query("SELECT id, timestamp FROM Conversation WHERE userOneEmail = :email or userTwoEmail = :email")
    fun getConvesationDataFromEmail( email: String) : MutableList<ConversationTuple>

    @Query("SELECT * FROM Conversation where userOneEmail = :email or userTwoEmail = :email ORDER BY timestamp DESC")
    fun getUserConversations(email: String): LiveData<MutableList<Conversation>>

    @Query("SELECT * FROM Conversation where (userOneEmail = :email or userTwoEmail = :email )AND (userOneEmail = :newEmail or userTwoEmail = :newEmail)")
    fun getMutualConversation(email: String, newEmail: String) : Conversation

    @Query("SELECT * FROM Conversation where (userOneEmail = :email or userTwoEmail = :email )")
    fun getConversationByUserEmail(email: String) : Conversation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(conversation: Conversation)

    @Update
    fun update(conversation: Conversation)

    @Delete
    fun delete(conversation: Conversation)

    @Query("DELETE FROM Conversation where id = :id")
    fun deleteConversationById(id : String)

    @Query("DELETE FROM Conversation")
    fun deleteAll()
}