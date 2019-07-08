package com.example.tfm.retrofit

import android.util.Log
import com.example.tfm.fragments.GifFragment
import com.example.tfm.model.giphy.GiphyPojo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val token = "1ILTocSRwQgQXWluqsVMTjkpfSGBm2zD"
    private const val baseUrl = "https://api.giphy.com/v1/gifs/"
    private const val limit = 26
    private const val rating = "G"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(GithubService::class.java)

    fun getGifsFromGiphy(limit: Int = this.limit, rating: String = this.rating){
        val call = service.getGifFromGiphy(token, limit, rating)

        call.enqueue(object: Callback<GiphyPojo> {

            override fun onResponse(call: Call<GiphyPojo>, response: Response<GiphyPojo>) {
                val gifs = response.body()
                gifs?.data?.forEach {
                    GifFragment.gifImages.add(it.images.previewGif.url)
                }
            }

            override fun onFailure(call: Call<GiphyPojo>, t: Throwable) {
                Log.d("DEBUG", "Could not get gifs")
            }
        })
    }

    fun getSearchGifFromGiphy(searchQuery: String, limit: Int = this.limit, rating: String = this.rating){
        val call = service.getSearchGifFromGiphy(token, searchQuery, limit, 0, rating, "en")
        call.enqueue(object: Callback<GiphyPojo> {

            override fun onResponse(call: Call<GiphyPojo>, response: Response<GiphyPojo>) {
                val gifs = response.body()
                gifs?.data?.forEach {
                    GifFragment.gifImages.add(it.images.previewGif.url)
                }
            }

            override fun onFailure(call: Call<GiphyPojo>, t: Throwable) {
                Log.d("DEBUG", "Could not get gifs")
            }
        })
    }
}