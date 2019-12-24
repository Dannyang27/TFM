package com.example.tfm.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.LanguageCode
import com.example.tfm.enum.MessageType
import com.example.tfm.model.*
import com.example.tfm.room.dao.ConversationDAO
import com.example.tfm.room.dao.EmojiDAO
import com.example.tfm.room.dao.MessageDAO
import com.example.tfm.room.dao.UserDAO
import kotlinx.coroutines.*

@Database(entities = [User::class, Conversation::class, Message::class, PlainMessageRoomModel::class, GifRoomModel::class, ImageRoomModel::class, LocationRoomModel::class, EmojiFrequency::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase: RoomDatabase(), CoroutineScope{
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

    abstract fun userDao(): UserDAO
    abstract fun conversationDao(): ConversationDAO
    abstract fun messageDao(): MessageDAO
    abstract fun emojiDao(): EmojiDAO

    companion object{
        var INSTANCE: MyRoomDatabase? = null

        fun getMyRoomDatabase(context: Context): MyRoomDatabase?{
            if(INSTANCE == null){
                synchronized(MyRoomDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MyRoomDatabase::class.java, "myDatabase").build()
                }
            }
            return INSTANCE
        }

        fun destroyDatabase(){
            INSTANCE = null
        }
    }

    fun addUser(user: User){
        launch {
            userDao().add(user)
        }
    }

    fun getUserByEmail(email: String): User{
        return userDao().getByEmail(email)
    }

    fun updateUser(user: User){
        launch {
            userDao().update(user)
        }
    }

    fun addConversation(conversation: Conversation){
        launch {
            conversationDao().add(conversation)
        }
    }

    fun addMessage(message: Message){
        launch {
            messageDao().add(message)
            val content = message.body as MessageContent
            var lastMessage = ""
            when(MessageType.fromInt(message.messageType)) {
                MessageType.MESSAGE -> {
                    addPlainMessage(message.id, content)

                    lastMessage = if( DataRepository.languagePreferenceCode == content.fieldThree.toInt() ||
                        LanguageCode.ENGLISH.code == content.fieldThree.toInt()){

                        content.fieldOne
                    }else{
                        content.fieldTwo
                    }
                }
                MessageType.GIF -> {
                    addGif(message.id, content)
                    lastMessage = "GIF"
                }
                MessageType.IMAGE -> {
                    addImage(message.id, content)
                    lastMessage = "Image"
                }
                MessageType.LOCATION -> {
                    addLocation(message.id, content)
                    lastMessage = "Location"
                }
                MessageType.ATTACHMENT -> {
                    lastMessage = "Attachment"
                }
            }

            updateConversationWithTranslatedLastMessage(message, lastMessage)
        }
    }

    private suspend fun updateConversationWithTranslatedLastMessage(message: Message, lastMessage: String){

        launch {
            val conversation = conversationDao().getById(message.ownerId)

            conversation?.let {
                if(MessageType.fromInt(message.messageType) != MessageType.MESSAGE){
                    conversation.lastMessage = lastMessage
                    conversationDao().update(conversation)
                    return@launch
                }

                if(DataRepository.languagePreferenceCode == message.body?.fieldThree?.toInt()){

                    withContext(Dispatchers.Main){
                        conversation.lastMessage = lastMessage
                        conversation.timestamp = message.timestamp
                    }

                    conversationDao().update(conversation)
                }else if( DataRepository.languagePreferenceCode == LanguageCode.ENGLISH.code){

                    withContext(Dispatchers.Main){
                        conversation.lastMessage = message.body?.fieldTwo
                        conversation.timestamp = message.timestamp
                    }

                    conversationDao().update(conversation)
                }else{
                    // Translation
                    val translator = DataRepository.fromEnglishTranslator
                    translator?.let {
                        withContext(Dispatchers.Main) {
                            it.translate(lastMessage).addOnSuccessListener { lastMessageTranslated ->
                                conversation.lastMessage = lastMessageTranslated
                                conversation.timestamp = message.timestamp

                                CoroutineScope(Dispatchers.IO).launch {
                                    conversationDao().update(conversation)
                                }

                            }.addOnFailureListener {
                                conversation.lastMessage = lastMessage
                                conversation.timestamp = message.timestamp

                                CoroutineScope(Dispatchers.IO).launch {
                                    conversationDao().update(conversation)
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun addPlainMessage(id: Long, content: MessageContent){
        launch {
            val message = PlainMessageRoomModel(id, content.fieldOne, content.fieldTwo, content.fieldThree)
            messageDao().addPlainMessage(message)
        }
    }

    private fun addGif(id: Long, content: MessageContent){
        launch {
            val gif = GifRoomModel(id, content.fieldOne)
            messageDao().addGif(gif)
        }
    }

    private fun addImage(id: Long, content: MessageContent){
        launch {
            val image = ImageRoomModel(id, content.fieldOne)
            messageDao().addImage(image)
        }
    }

    private fun addLocation(id: Long, content: MessageContent){
        launch {
            val location = LocationRoomModel(id, content.fieldOne.toDouble(), content.fieldTwo.toDouble(), content.fieldThree)
            messageDao().addLocation(location)
        }
    }
}