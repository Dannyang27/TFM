package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class Gifs(
    @SerializedName("original")
    val original: Original,
    @SerializedName("preview_gif")
    val previewGif: PreviewGif
)