package com.example.tfm.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = "Conversation")
data class Conversation (@PrimaryKey
                    var id: String = "",
                    var userOneEmail: String = "",
                    var userOneUsername: String = "",
                    var userOnePhoto: String = "",
                    var userTwoEmail : String = "",
                    var userTwoUsername: String = "",
                    var userTwoPhoto: String = "",
                    @Ignore @get:Exclude
                    var messages: MutableList<Message> = mutableListOf(),
                    var lastMessage: String? = "",
                    var timestamp: Long = -1)