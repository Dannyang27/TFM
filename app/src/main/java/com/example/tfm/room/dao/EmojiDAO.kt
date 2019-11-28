package com.example.tfm.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfm.model.EmojiFrequency

@Dao
interface EmojiDAO {

    @Query("SELECT * FROM Emoji")
    fun getAll() : MutableList<EmojiFrequency>?

    @Query("SELECT * FROM Emoji WHERE frequency > 0 ORDER BY frequency DESC LIMIT 40")
    fun getEmojisLiveData() : LiveData<MutableList<EmojiFrequency>?>

    @Query("SELECT * FROM Emoji WHERE emojiCode = :code LIMIT 1")
    fun getEmojiIfExist(code: String): EmojiFrequency?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(emoji: EmojiFrequency)

    @Update
    fun update(emoji: EmojiFrequency)

    @Delete
    fun delete(emoji: EmojiFrequency)
}