package com.example.tfm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Conversation
import com.example.tfm.util.FirebaseUtil

class ConversationViewModel : ViewModel(){

    private val conversationList: MutableLiveData<MutableList<Conversation>> = MutableLiveData()

    fun getConversations(): LiveData<MutableList<Conversation>>{
        return conversationList
    }

    fun initConversations(){
        val user = DataRepository.user
        FirebaseUtil.loadUserConversation(conversationList, user?.id.toString())
    }
}