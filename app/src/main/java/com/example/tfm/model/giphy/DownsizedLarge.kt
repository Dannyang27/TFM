package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class DownsizedLarge(
    @SerializedName("height")
    val height: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: String
)