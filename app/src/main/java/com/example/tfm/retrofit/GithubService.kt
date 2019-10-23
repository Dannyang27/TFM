package com.example.tfm.retrofit

import com.example.tfm.model.giphy.GiphyPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("trending?")
    suspend fun getGifFromGiphy(@Query("api_key") token: String,
                       @Query("limit") limit: Int,
                       @Query("rating") rating: String) : Response<GiphyPojo>


    @GET("search?")
    suspend fun getSearchGifFromGiphy(@Query("api_key") token: String,
                        @Query("q") search: String,
                        @Query("limit") limit: Int,
                        @Query("offset") offset: Int,
                        @Query("rating") rating: String,
                        @Query("lang") language: String) : Response<GiphyPojo>
}