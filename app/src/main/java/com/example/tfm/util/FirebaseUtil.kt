package com.example.tfm.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import com.example.tfm.activity.ChatActivity
import com.example.tfm.activity.MainActivity
import com.example.tfm.activity.SignupActivity
import com.example.tfm.activity.UserSearcherActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

object FirebaseUtil {
    const val FIREBASE_USER_PATH = "users"
    const val FIREBASE_PRIVATE_CHAT_PATH = "chats"
    const val FIREBASE_PRIVATE_MESSAGE_PATH = "messages"

    private val database = FirebaseDatabase.getInstance().reference

    fun addUser(context: Context, user: User){
        database.child(FIREBASE_USER_PATH)
            .child(user.hashCode().toString()).setValue(user)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "FirebaseDatabase user added successfully")

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                prefs.updateCurrentUser(SignupActivity.currentUserEmail, SignupActivity.currentUserPassword)

                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addUser(user)

                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
        }
    }

    fun getUsersByName(name: String){
        if(name.isNotEmpty()){
            database.child(FIREBASE_USER_PATH).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(LogUtil.TAG, databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val users = mutableListOf<User>()
                    dataSnapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        user?.let {
                            if(it.name.contains(name, ignoreCase = true)){
                                users.add(it)
                            }
                        }
                    }
                    UserSearcherActivity.updateList(users)
                }
            })
        }
    }

    fun loadAllUsers( cacheList: MutableList<User>){
        database.child(FIREBASE_USER_PATH).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if(user?.email != DataRepository.currentUserEmail){
                        cacheList.add(user!!)
                    }
                }
                UserSearcherActivity.updateList(cacheList)
            }
        })
    }

    fun loadUserConversation(context: Context, user: String){
        val userConversations = MyRoomDatabase.getMyRoomDatabase(context)?.conversationDao()?.getUserConversations(user)

        if(userConversations!!.isEmpty()){
            launchMainActivity(context)
        }else{
            userConversations.forEach {
                val conversation = it
                database.child(FIREBASE_PRIVATE_CHAT_PATH).child(conversation.id).child(
                    FIREBASE_PRIVATE_MESSAGE_PATH).limitToLast(20).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach{
                            val message = it.getValue(Message::class.java)!!
                            conversation.messages.add(message)
                        }
                        DataRepository.addConversation(it.id, conversation)
                        launchMainActivity(context)
                    }
                })
            }
        }
    }

    fun addPrivateChat(context: Context, conversation: Conversation){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversation.id).setValue(conversation)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "Conversation added into FirebaseFirestore")
                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addConversation(conversation)

                var userToCreate: String?
                if(conversation.userOne.equals(DataRepository.currentUserEmail)){
                    userToCreate = conversation.userTwo
                }else{
                    userToCreate = conversation.userOne
                }

                GlobalScope.launch {
                    val user = FirebaseFirestore.getInstance().createRoomUser(userToCreate)
                    roomDatabase?.addUser(user!!)
                    Log.d(LogUtil.TAG, "User added to room: ${user.toString()}")
                }

                val intent = Intent(context, ChatActivity::class.java)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("conversationId", conversation.id)
                intent.putExtra("receiverEmail", userToCreate.toString())
                context.startActivity(intent)
        }
    }

    fun addMessage(message: Message){
        val newMessages: MutableList<Message> = mutableListOf()
        newMessages.addAll(ChatActivity.messages)
        newMessages.add(message)
        ChatActivity.updateList(newMessages)

        database.child(FIREBASE_PRIVATE_CHAT_PATH).child(message.ownerId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .child(message.timestamp.toString())
            .setValue(message)
            .addOnSuccessListener {
                DataRepository.addMessage(message)
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Error while sending message")
            }
    }
}

suspend fun FirebaseFirestore.createRoomUser(email: String?): User?{
    return collection(FirebaseUtil.FIREBASE_USER_PATH).document(email.toString()).get().await().toObject(User::class.java)
}

fun FirebaseFirestore.addUser(context: Context, user: User){
    collection(FirebaseUtil.FIREBASE_USER_PATH)
        .document(user.email)
        .set(user)
        .addOnSuccessListener {
            Log.d(LogUtil.TAG, "User added into FirebaseFirestore")
            FirebaseUtil.addUser(context, user)
        }.addOnFailureListener {
            Log.d(LogUtil.TAG, "Remove user from firestore")
        }
}

fun FirebaseFirestore.addConversation(context: Context, conversation: Conversation){
    collection(FirebaseUtil.FIREBASE_PRIVATE_CHAT_PATH)
        .document(conversation.id)
        .set(conversation)
        .addOnSuccessListener {
            FirebaseUtil.addPrivateChat(context, conversation)
        }.addOnFailureListener {
            Log.d(LogUtil.TAG, "Remove chat from Firestore")
        }
}

fun FirebaseFirestore.updateCurrentUser(context: Context, user: User, input: String, field: TextView){
    collection(FirebaseUtil.FIREBASE_USER_PATH)
        .document(user.email)
        .set(user)
        .addOnSuccessListener {
            MyRoomDatabase.getMyRoomDatabase(context)?.updateUser(user)
            context.toast("User updated")
            field.text = input
        }
        .addOnFailureListener {
            Log.d(LogUtil.TAG, "Error while updating user")
        }
}

private fun launchMainActivity(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

