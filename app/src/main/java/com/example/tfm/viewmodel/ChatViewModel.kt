package com.example.tfm.viewmodel

import android.content.Context
import android.preference.PreferenceManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.model.MessageContent
import com.example.tfm.room.database.MyRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel(){

    private var roomDatabase: MyRoomDatabase? = null
    private val languageFlag = MutableLiveData<String>()
    private val translatedModel = MutableLiveData<String>()
    private val showEmojiKeyboard = MutableLiveData<Boolean>()
    private val chatMessages = MutableLiveData<MutableList<Message>>()

    init{
        clearMessages()
    }

    fun getChatMessages(): LiveData<MutableList<Message>> = chatMessages
    fun getLanguageFlag(): LiveData<String> = languageFlag
    fun getTranslatedModel(): LiveData<String> = translatedModel
    fun getShowEmojiKeyboard(): LiveData<Boolean> = showEmojiKeyboard

    fun showKeyboard(activate: Boolean){
        showEmojiKeyboard.postValue(activate)
    }

    fun saveMessage(message: Message){
        roomDatabase?.addMessage(message)
    }

    fun initLanguageFlag(context: Context){
        val tModel = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("chatLanguage", "English")

        translatedModel.postValue(tModel)
        languageFlag.postValue(tModel)
    }

    private fun clearMessages(){
        chatMessages.postValue(mutableListOf())
    }

    fun initRoomObserver(activity: FragmentActivity, conversationId: String){
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity)
        roomDatabase?.messageDao()?.getConversationMessagesWithLimit(conversationId)?.observe(activity, Observer { messages ->
            CoroutineScope(Dispatchers.IO).launch {
                val messageList = mutableListOf<Message>()
                messages.forEach {msg ->
                    var content: MessageContent? = null

                    when(MessageType.fromInt(msg.messageType)){
                        MessageType.MESSAGE -> {
                            val plainMessage = roomDatabase?.messageDao()?.getPlainMessageById(msg.id)
                            content = MessageContent(plainMessage?.originalText.toString(), plainMessage?.englishText.toString(), plainMessage?.language.toString())
                        }
                        MessageType.IMAGE -> {
                            val image = roomDatabase?.messageDao()?.getImageById(msg.id)
                            content = MessageContent(image?.encodedImage.toString())
                        }
                        MessageType.GIF -> {
                            val gif = roomDatabase?.messageDao()?.getGifById(msg.id)
                            content = MessageContent(gif?.url.toString())
                        }
                        MessageType.LOCATION -> {
                            val location = roomDatabase?.messageDao()?.getLocationById(msg.id)
                            content = MessageContent(location?.latitude.toString(), location?.longitude.toString(), location?.addressLine.toString())
                        }
                        MessageType.ATTACHMENT -> { }
                    }

                    msg.body = content
                    setMessageToRead(msg)
                    messageList.add(msg)
                }

                chatMessages.postValue(messageList)
            }
        })
    }

    private fun setMessageToRead(message: Message){
        if(!message.isRead){
            message.isRead = true
            roomDatabase?.messageDao()?.update(message)
        }
    }
}