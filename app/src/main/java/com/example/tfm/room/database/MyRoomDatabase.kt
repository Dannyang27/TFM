package com.example.tfm.room.database

import android.content.Context
import android.util.Log
import androidx.emoji.widget.EmojiTextView
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.*
import com.example.tfm.room.dao.ConversationDAO
import com.example.tfm.room.dao.MessageDAO
import com.example.tfm.room.dao.UserDAO
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.LogUtil
import com.example.tfm.util.addConversation
import com.example.tfm.util.launchChatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@Database(entities = [User::class, Conversation::class, Message::class, GifRoomModel::class, ImageRoomModel::class, LocationRoomModel::class], version = 1, exportSchema = false)
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

    fun addUser(user: User){
        launch {
            userDao().add(user)
        }.also {
            Log.d(LogUtil.TAG, "User ${user.email} has been added into dabatase")
        }
    }

    fun updateUser(user: User){
        launch {
            userDao().update(user)
        }.also {
            Log.d(LogUtil.TAG, "User ${user.email} updated in RoomDatabase")
        }
    }

    fun deleteAllUsers(){
        launch{
            userDao().deleteAll()
        }.also {
            Log.d(LogUtil.TAG, "All users removed from ROOM")
        }

    }

    suspend fun getUserNameByEmail(emojiTv: EmojiTextView, email: String){
        withContext(Dispatchers.IO){
            val name = userDao().getNameByEmail(email)
            withContext(Dispatchers.Main){
                emojiTv.text = name
            }
        }
    }

    fun showAllUserInLog(){
        launch {
                Log.d(LogUtil.TAG, "Email: | Name: | Status: | PhotoPath: ")
                userDao().getAll().forEach {
                    Log.d(LogUtil.TAG, "${it.email} | ${it.name} | ${it.status} | ${it.profilePhoto}")
                }
        }
    }

    fun addConversation(conversation: Conversation){
        launch {
            conversationDao().add(conversation)
        }.also {
            Log.d(LogUtil.TAG, "Conversation ${conversation.id} has been added into database")
        }
    }

    fun deleteConversation(id: String){
        launch {
            conversationDao().deleteConversationById(id)
        }.also {
            Log.d(LogUtil.TAG, "Conversation id: $id is deleted and all its messages")
        }
    }

    fun showAllConversationInLog(){
        launch {
            Log.d(LogUtil.TAG, "Id: | UserOne: | UserTwo: ")
            conversationDao().getAll().forEach {
                Log.d(LogUtil.TAG, "${it.id} | ${it.userOne} | ${it.userTwo}")
            }
        }
    }

    fun updateConversation( message: Message, content: String){
        launch {
            val conv = conversationDao().getById(message.ownerId)
            conv.lastMessage = content
            conv.timestamp = message.timestamp
            conversationDao().update(conv)
        }.also {
            Log.d(LogUtil.TAG, "Conversation with id: ${message.ownerId} has been updated")
        }
    }

    fun deleteAllConversation(){
        launch {
            conversationDao().deleteAll()
        }.also {
            Log.d(LogUtil.TAG, "All conversations removed also its messages")
        }
    }

    fun addMessage(message: Message){
        launch {
            messageDao().add(message)
            when(MessageType.fromInt(message.messageType)) {
                MessageType.GIF -> addGif(message.body as GifRoomModel)
                MessageType.IMAGE -> addImage(message.body as ImageRoomModel)
                MessageType.LOCATION -> addLocation(message.body as LocationRoomModel)
                MessageType.ATTACHMENT -> {}
                else -> {
                    Log.d(LogUtil.TAG, "MessageType not specified")
                }
            }

        }.also {
            Log.d(LogUtil.TAG, "Message id: ${message.ownerId} added in Room and MessageType ${message.body} ")
        }
    }

    fun getReceiverUser(conversationId: String): String{
        val conversation = conversationDao().getById(conversationId)

        if(conversation.userOne == DataRepository.currentUserEmail){
            return conversation.userTwo.toString()
        }else{
            return conversation.userOne.toString()
        }
    }

    fun addGif(gif: GifRoomModel){
        launch {
            messageDao().addGif(gif)
        }.also{
            Log.d(LogUtil.TAG, "Gif added to Room | Url: ${gif.url}")
        }
    }

    fun addImage(image: ImageRoomModel){
        launch {
            messageDao().addImage(image)
        }.also{
            Log.d(LogUtil.TAG, "Image added to Room | Url: ${image.encodedImage}")
        }
    }

    fun addLocation(location: LocationRoomModel){
        launch {
            messageDao().addLocation(location)
        }.also{
            Log.d(LogUtil.TAG, "Location added to Room | Latitude: ${location.latitude} | Longitude: ${location.longitude} ")
        }
    }
}