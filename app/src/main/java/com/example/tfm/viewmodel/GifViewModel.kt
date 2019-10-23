package com.example.tfm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.model.giphy.Gifs
import com.example.tfm.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifViewModel : ViewModel(){
    private val gifs = MutableLiveData<MutableList<Gifs>>()

    fun getGifs(): LiveData<MutableList<Gifs>> = gifs

    fun getTrendyGiphyGifs(){
        CoroutineScope(Dispatchers.IO).launch{
            val gifList = RetrofitClient.getGifsFromGiphy()
            gifs.postValue(gifList)
        }
    }

    fun searchGiphyGifs(query: String?){
        CoroutineScope(Dispatchers.IO).launch{
            query?.let {
                val gifList = RetrofitClient.getSearchGifFromGiphy(query)
                gifs.postValue(gifList)
            }
        }
    }
}