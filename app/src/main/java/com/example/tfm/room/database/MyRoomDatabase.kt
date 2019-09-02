package com.example.tfm.room.database

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.emoji.widget.EmojiTextView
import androidx.room.*
import com.example.tfm.activity.ChatActivity
import com.example.tfm.enum.MessageType
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.model.*
import com.example.tfm.room.dao.ConversationDAO
import com.example.tfm.room.dao.MessageDAO
import com.example.tfm.room.dao.UserDAO
import com.example.tfm.room.typeconverters.AnyTypeConverter
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.LogUtil
import com.example.tfm.util.addConversation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@Database(entities = [User::class, Conversation::class, Message::class, GifRoomModel::class, ImageRoomModel::class, LocationRoomModel::class], version = 1, exportSchema = false)
@TypeConverters(AnyTypeConverter::class)
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

    fun getMutualConversation(context: Context, email: String, newEmail: String): Conversation?{
        var conversation: Conversation? = null
        launch {
            conversation = conversationDao().getMutualConversation(email, newEmail)
            if(conversation != null){
                Log.d(LogUtil.TAG, "Conversation exists: " + conversation?.id)
                val intent = Intent(context, ChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("conversationId", conversation?.id)
                context.startActivity(intent)
            }else{
                Log.d(LogUtil.TAG, "Conversation does not exist, do some stuff")
                createNewConversation(context, email, newEmail)
            }
        }
        return conversation
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

    fun loadUserConversation(email: String){
        launch {
            PrivateFragment.updateConversation(conversationDao().getUserConversations(email))
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

    fun getAllMessagesFromConversation(conversationId: String){
        launch {
            val roomMessages = messageDao().getConversationMessages(conversationId)
            roomMessages.forEach {
                when(MessageType.fromInt(it.messageType)){
                    MessageType.MESSAGE -> { }
                    MessageType.IMAGE -> {
                        val image = messageDao().getImageById(it.id)
                        it.body = image
                    }
                    MessageType.GIF -> {
                        val gif = messageDao().getGifById(it.id)
                        it.body = gif
                    }
                    MessageType.LOCATION -> {
                        val location = messageDao().getLocationById(it.id)
                        it.body = location
                    }

                    MessageType.ATTACHMENT -> {
                        Log.d(LogUtil.TAG, "Adding attachemtn to messageType: ${it.messageType}")
                    }
                }
            }

            val messages = mutableListOf<Message>()
            messages.addAll(roomMessages)
            ChatActivity.updateList(messages)

        }.also {
            Log.d(LogUtil.TAG, "All conversations retrieved for $conversationId")
        }
    }

    private suspend fun createNewConversation(context: Context, email: String, newEmail: String){
        val firestore = FirebaseFirestore.getInstance()

        val taskOne = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(email).get().await()
        val userOne = taskOne.toObject(User::class.java)

        val taskTwo = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(newEmail).get().await()
        val userTwo = taskTwo.toObject(User::class.java)

        var userOneHash = userOne.hashCode().toLong()
        var userTwoHash = userTwo.hashCode().toLong()

        if(userOneHash > userTwoHash){
            val tmp = userOneHash
            userOneHash = userTwoHash
            userTwoHash = tmp
        }

        val hashcode = userOneHash.toString().plus(userTwoHash.toString())
        val conversation = Conversation(hashcode, userOne?.email, userTwo?.email, mutableListOf(), "",System.currentTimeMillis(), mutableListOf(), true )

        firestore.addConversation(context, conversation)
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