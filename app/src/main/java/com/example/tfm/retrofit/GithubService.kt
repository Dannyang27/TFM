package com.example.tfm.retrofit

import com.example.tfm.model.giphy.GiphyPojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("trending?")
    fun getGifFromGiphy(@Query("api_key") token: String,
                       @Query("limit") language: Int,
                       @Query("rating") rating: String) : Call<GiphyPojo>
}