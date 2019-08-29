package com.example.tfm.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.preference.PreferenceManager
import android.util.Log
import com.example.tfm.activity.ChatActivity
import com.example.tfm.activity.MainActivity
import com.example.tfm.activity.SignupActivity
import com.example.tfm.activity.UserSearcherActivity
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object FirebaseUtil {
    const val FIREBASE_USER_PATH = "users"
    const val FIREBASE_PRIVATE_CHAT_PATH = "chats"
    const val FIREBASE_PRIVATE_MESSAGE_PATH = "messages"

    private val database = FirebaseDatabase.getInstance().reference
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

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
                    if(user?.email != currentUserEmail){
                        cacheList.add(user!!)
                    }
                }
                UserSearcherActivity.updateList(cacheList)
            }
        })
    }

    fun addPrivateChat(context: Context, conversation: Conversation){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversation.id).setValue(conversation)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "Conversation added into FirebaseFirestore")
                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addConversation(conversation)

                var userToCreate: String?
                if(conversation.userOne.equals(currentUserEmail)){
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
                context.startActivity(intent)
        }
    }

    fun addMessage(context: Context, message: Message){
        database.child(FIREBASE_PRIVATE_CHAT_PATH).child(message.ownerId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .child(message.hashCode().toString())
            .setValue(message)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "Message added to firebaserealtime: ${message.body}")
                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addMessage(message)
                val newMessages: MutableList<Message> = mutableListOf()
                newMessages.addAll(ChatActivity.messages)
                newMessages.add(message)
                ChatActivity.updateList(newMessages)
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