package com.example.tfm.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.room.dao.ConversationDAO
import com.example.tfm.room.dao.MessageDAO
import com.example.tfm.room.dao.UserDAO
import com.example.tfm.room.typeconverters.*
import com.example.tfm.util.LogUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@Database(entities = [User::class, Conversation::class, Message::class], version = 1)
@TypeConverters(DateTypeConverter::class, UserConverter::class, UserListConverter::class, MessageConverter::class, MessageListConverter::class, AnyTypeConverter::class)
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
}

fun MyRoomDatabase.addUser(context: Context, user: User){
    launch {
        val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)?.userDao()
        val job = async{ roomDatabase?.add(user) }.also { Log.d(LogUtil.TAG, "User ${user.email} has been added into dabatase") }
        job.await()
    }
}

fun MyRoomDatabase.addConversation(context: Context, conversation: Conversation){
    launch {
        val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)?.conversationDao()
        val job = async{ roomDatabase?.add(conversation) }
            .also { Log.d(LogUtil.TAG, "Conversation ${conversation.id} has been added into dabatase") }
        job.await()
    }
}

fun MyRoomDatabase.addMessage(context: Context, message: Message){
    launch {
        val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)?.messageDao()
        val job = async{ roomDatabase?.add(message) }
            .also { Log.d(LogUtil.TAG, "Message with conversation id: ${message.ownerId} has been added into dabatase") }
        job.await()
    }
}

fun MyRoomDatabase.deleteConversation(context: Context, id: String){
    launch {
        val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)?.conversationDao()
        val job = async{
            roomDatabase?.deleteConversationById(id)
        }

        job.await()
    }
}