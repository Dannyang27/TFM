package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class WStill(
    @SerializedName("height")
    val height: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: String
)