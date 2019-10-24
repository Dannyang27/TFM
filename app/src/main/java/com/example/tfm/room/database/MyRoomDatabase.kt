package com.example.tfm.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tfm.enum.MessageType
import com.example.tfm.model.*
import com.example.tfm.room.dao.ConversationDAO
import com.example.tfm.room.dao.MessageDAO
import com.example.tfm.room.dao.UserDAO
import com.example.tfm.util.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(entities = [User::class, Conversation::class, Message::class, PlainMessageRoomModel::class, GifRoomModel::class, ImageRoomModel::class, LocationRoomModel::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase: RoomDatabase(), CoroutineScope{
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

    abstract fun userDao(): UserDAO
    abstract fun conversationDao(): ConversationDAO
    abstract fun messageDao(): MessageDAO

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

    fun getAllMessages(){
        launch {
            val messages = messageDao().getAll()
            messages.forEach {
                when(MessageType.fromInt(it.messageType)){
                    MessageType.MESSAGE -> {
                        val plainMessage = messageDao().getPlainMessageById(it.id)
                        Log.d(LogUtil.TAG, "FieldOne: ${plainMessage.originalText} | FieldTwo: ${plainMessage.englishText} | FieldThree: ${plainMessage.language}")
                    }
                    MessageType.GIF -> {
                        val gif = messageDao().getGifById(it.id)
                        Log.d(LogUtil.TAG, "FieldOne: ${gif.url}")
                    }
                    MessageType.IMAGE -> {
                        val image = messageDao().getImageById(it.id)
                        Log.d(LogUtil.TAG, "FieldOne: ${image.encodedImage}")
                    }
                    MessageType.LOCATION -> {
                        val location = messageDao().getLocationById(it.id)
                        Log.d(LogUtil.TAG, "FieldOne: ${location.latitude} | FieldTwo: ${location.longitude} | FieldThree: ${location.addressLine}")
                    }
                    MessageType.ATTACHMENT -> {}
                }
            }
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
            when(MessageType.fromInt(message.messageType)) {
                MessageType.MESSAGE -> addPlainMessage(message.id, content)
                MessageType.GIF -> addGif(message.id, content)
                MessageType.IMAGE -> addImage(message.id, content)
                MessageType.LOCATION -> addLocation(message.id, content)
                MessageType.ATTACHMENT -> {}
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
        }.also{
            Log.d(LogUtil.TAG, "Image added to Room")
        }
    }

    private fun addLocation(id: Long, content: MessageContent){
        launch {
            val location = LocationRoomModel(id, content.fieldOne.toDouble(), content.fieldTwo.toDouble(), content.fieldThree)
            messageDao().addLocation(location)
        }.also{
            Log.d(LogUtil.TAG, "Location added to Room")
        }
    }
}