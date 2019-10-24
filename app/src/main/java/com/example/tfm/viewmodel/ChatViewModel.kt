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
import com.example.tfm.util.getDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel(){

    private var roomDatabase: MyRoomDatabase? = null
    private val languageFlag = MutableLiveData<Int>()
    private val translatedModel = MutableLiveData<String>()
    private val showEmojiKeyboard = MutableLiveData<Boolean>()
    private val chatMessages = MutableLiveData<MutableList<Message>>()

    fun getChatMessages(): LiveData<MutableList<Message>> = chatMessages
    fun getLanguageFlag(): LiveData<Int> = languageFlag
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
            .getString("chatLanguage", "Default")

        tModel?.let{
            translatedModel.postValue(tModel)
            languageFlag.postValue(it.getDrawable())
        }
    }

    fun clearMessages(){
        chatMessages.postValue(mutableListOf())
    }

    fun initRoomObserver(activity: FragmentActivity, conversationId: String){
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity)
        roomDatabase?.messageDao()?.getConversationMessages(conversationId)?.observe(activity, Observer { messages ->
            CoroutineScope(Dispatchers.IO).launch {
                val messageList = mutableListOf<Message>()
                messages.forEach {
                    var content: MessageContent? = null
                    when(MessageType.fromInt(it.messageType)){
                        MessageType.MESSAGE -> {
                            val plainMessage = roomDatabase?.messageDao()?.getPlainMessageById(it.id)
                            content = MessageContent(plainMessage?.originalText.toString(), plainMessage?.englishText.toString(), plainMessage?.language.toString())
                        }
                        MessageType.IMAGE -> {
                            val image = roomDatabase?.messageDao()?.getImageById(it.id)
                            content = MessageContent(image?.encodedImage.toString())
                        }
                        MessageType.GIF -> {
                            val gif = roomDatabase?.messageDao()?.getGifById(it.id)
                            content = MessageContent(gif?.url.toString())
                        }
                        MessageType.LOCATION -> {
                            val location = roomDatabase?.messageDao()?.getLocationById(it.id)
                            content = MessageContent(location?.latitude.toString(), location?.longitude.toString(), location?.addressLine.toString())
                        }
                        MessageType.ATTACHMENT -> { }
                    }

                    it.body = content
                    messageList.add(it)
                }

                chatMessages.postValue(messageList)
            }
        })
    }
}