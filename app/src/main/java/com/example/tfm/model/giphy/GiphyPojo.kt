package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class GiphyPojo(
    @SerializedName("data")
    val data: List<Data>
)