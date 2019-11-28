package com.example.tfm.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tfm.room.database.MyRoomDatabase

class EmojiFragmentViewModel : ViewModel(){

    private val emojis = MutableLiveData<ArrayList<String>>()

    fun getEmojis(): LiveData<ArrayList<String>> = emojis

    fun initEmojiViewModel(activity: FragmentActivity){
        val roomDatabase = MyRoomDatabase.INSTANCE
        roomDatabase?.let {
            it.emojiDao().getEmojisLiveData().observe(activity, Observer {
                emojis.postValue(ArrayList(it?.map { it.emojiCode }))
            })
        }
    }
}