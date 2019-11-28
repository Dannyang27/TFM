package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Emoji")
data class EmojiFrequency(@PrimaryKey
                          val emojiCode: String,
                          var frequency: Int = 1)