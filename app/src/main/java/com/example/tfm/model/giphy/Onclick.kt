package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class Onclick(
    @SerializedName("url")
    val url: String
)