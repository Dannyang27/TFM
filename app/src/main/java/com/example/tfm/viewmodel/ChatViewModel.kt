package com.example.tfm.viewmodel

import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfm.model.Message
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.getDrawable

class ChatViewModel : ViewModel(){

    private val languageFlag = MutableLiveData<Int>()
    private val translatedModel = MutableLiveData<String>()
    private val showEmojiKeyboard = MutableLiveData<Boolean>()

    companion object{
        private val chatMessages = MutableLiveData<MutableList<Message>>()

        fun addMessage(message: Message){
            val messages = chatMessages.value
            messages?.add(message)
            chatMessages.postValue(messages)

            FirebaseUtil.addMessageFirebase(message)
        }

    }

    fun getChatMessages(): LiveData<MutableList<Message>> = chatMessages
    fun getLanguageFlag(): LiveData<Int> = languageFlag
    fun getTranslatedModel(): LiveData<String> = translatedModel
    fun getShowEmojiKeyboard(): LiveData<Boolean> = showEmojiKeyboard

    fun showKeyboard(activate: Boolean){
        showEmojiKeyboard.postValue(activate)
    }

    fun initLanguageFlag(context: Context){
        val translateModel = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("chatLanguage", "Default")

        this.translatedModel.postValue(translateModel)

        translateModel?.let{
            languageFlag.postValue(it.getDrawable())
        }
    }

    fun initMessages(conversationId: String){
        FirebaseUtil.loadMessageFromConversation(chatMessages, conversationId)
    }
}