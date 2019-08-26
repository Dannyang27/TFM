package com.example.tfm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(@PrimaryKey(autoGenerate = false)
                val email: String = "",
                val name: String = "",
                val status: String = "",
                val profilePhoto: String = "")
