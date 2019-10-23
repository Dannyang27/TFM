package com.example.tfm.retrofit

import com.example.tfm.model.giphy.Gifs
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val token = "1ILTocSRwQgQXWluqsVMTjkpfSGBm2zD"
    private const val baseUrl = "https://api.giphy.com/v1/gifs/"
    private const val limit = 26
    private const val rating = "G"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(GithubService::class.java)

    suspend fun getGifsFromGiphy(limit: Int = this.limit, rating: String = this.rating): MutableList<Gifs>{
        val response = service.getGifFromGiphy(token, limit, rating)
        val gifList = mutableListOf<Gifs>()

        try{
            if(response.isSuccessful){
                val gifs = response.body()
                gifs?.data?.forEach {
                    gifList.add(it.gifs)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return gifList
    }

    suspend fun getSearchGifFromGiphy(searchQuery: String, limit: Int = this.limit, rating: String = this.rating): MutableList<Gifs>{
        val response = service.getSearchGifFromGiphy(token, searchQuery, limit, 0, rating, "en")
        val gifList = mutableListOf<Gifs>()

        try{
            if(response.isSuccessful){
                val gifs = response.body()
                gifs?.data?.forEach {
                    gifList.add(it.gifs)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return gifList
    }
}