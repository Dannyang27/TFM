package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("images")
    val images: Images
)