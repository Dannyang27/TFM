package com.example.tfm.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.NonNull
import com.example.tfm.activity.SignupActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.notification.MyNotificationManager
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.viewmodel.LoginViewModel
import com.example.tfm.viewmodel.SignupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

object FirebaseUtil {
    const val FIREBASE_USER_PATH = "users"
    const val FIREBASE_PRIVATE_CHAT_PATH = "chats"
    private const val FIREBASE_PRIVATE_MESSAGE_PATH = "messages"
    private const val FIREBASE_LAST_MESSAGE = "lastMessage"
    private const val FIREBASE_TIMESTAMP = "timestamp"

    private val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private lateinit var prefs: SharedPreferences
    private val database = FirebaseDatabase.getInstance().reference
    private lateinit var roomDatabase: MyRoomDatabase

    fun initRoomDatabase(context: Context) {
        roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)!!
    }

    fun login(context: Context, email: String, password: String) {
        firebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    prefs.updateCurrentUser(email, password)

                    CoroutineScope(Dispatchers.IO).launch {
                        val loginTask =
                            FirebaseFirestore.getInstance().collection(FIREBASE_USER_PATH)
                                .document(email).get().await()
                        DataRepository.user = loginTask.toObject(User::class.java)
                        DataRepository.currentUserEmail = email
                        LoginViewModel.isSuccessful.postValue(true)
                    }

                } else {
                    context.toast("Wrong user/password")
                    LoginViewModel.isLoading.postValue(false)
                }
            }
    }

    fun createNewUser(context: Context, username: String, email: String, password: String) {
        val user = User("", email, username, "", "")
        val hashcode = user.hashCode().toString()
        user.id = hashcode

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                FirebaseFirestore.getInstance().addUser(context, user)
                SignupActivity.currentUserEmail = email
                SignupActivity.currentUserPassword = password
            }.addOnFailureListener {
                SignupViewModel.isJoinUsSuccessful.postValue(false)
                context.toast("Cannot create user with those inputs")
            }
    }


    fun addUser(context: Context, user: User) {
        database.child(FIREBASE_USER_PATH)
            .child(user.id).setValue(user)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "FirebaseDatabase user added successfully")

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                prefs.updateCurrentUser(
                    SignupActivity.currentUserEmail,
                    SignupActivity.currentUserPassword
                )

                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addUser(user)

                SignupViewModel.isJoinUsSuccessful.postValue(true)
            }.addOnFailureListener {
                SignupViewModel.isJoinUsSuccessful.postValue(false)
            }
    }

    fun loadUsers() {
        database.child(FIREBASE_USER_PATH)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(@NonNull p0: DatabaseError) {}

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        user?.let {
                            roomDatabase.addUser(it)
                        }
                    }

                    loadUserConversations(DataRepository.user?.id)
                }
            })
    }

    fun loadUserConversations(userId: String?) {
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(@NonNull p0: DatabaseError) {}

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val conv = it.getValue(Conversation::class.java)

                        conv?.let {
                            if (conv.id.contains(userId.toString())) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    launch {
                                        loadConversationMessages(conv.id)
                                    }

                                    val jobOne = async {
                                        val userOne = roomDatabase.getUserByEmail(conv.userOneEmail)
                                        conv.userOneUsername = userOne.name
                                        conv.userOnePhoto = userOne.profilePhoto
                                        roomDatabase.addUser(userOne)

                                    }

                                    val jobTwo = async {
                                        val userTwo = roomDatabase.getUserByEmail(conv.userTwoEmail)
                                        conv.userTwoUsername = userTwo.name
                                        conv.userTwoPhoto = userTwo.profilePhoto
                                        roomDatabase.addUser(userTwo)
                                    }

                                    jobOne.await()
                                    jobTwo.await()
                                    async { roomDatabase.addConversation(conv) }.await()
                                }
                            }
                        }
                    }
                }
            })
    }

    fun loadConversationMessages(conversationId: String) {
        Log.d(LogUtil.TAG, "Conversation id: $conversationId")

        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversationId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(@NonNull p0: DatabaseError) {}

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    Log.d(LogUtil.TAG, "Conversation id: $conversationId")

                    dataSnapshot.children.forEach { mess ->
                        val message = mess.getValue(Message::class.java)
                        message?.let {
                            roomDatabase.addMessage(message)
                        }
                    }
                }
            })
    }

    fun addPrivateChat(context: Context, conversation: Conversation) {
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversation.id)
            .setValue(conversation)
            .addOnSuccessListener {
                val email: String
                val username: String
                val photo: String

                if (conversation.userOneEmail == DataRepository.currentUserEmail) {
                    email = conversation.userTwoEmail
                    username = conversation.userTwoUsername
                    photo = conversation.userTwoPhoto
                } else {
                    email = conversation.userOneEmail
                    username = conversation.userOneUsername
                    photo = conversation.userOnePhoto
                }

                launchListener(context, conversation.id)

                context.launchChatActivity(conversation.id, email, username, photo, false)
            }
    }

    fun addMessageFirebase(message: Message) {
        database.child(FIREBASE_PRIVATE_CHAT_PATH).child(message.ownerId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .child(message.timestamp.toString())
            .setValue(message)
            .addOnSuccessListener {
                message.isSent = true
                CoroutineScope(Dispatchers.IO).launch {
                    roomDatabase.addMessage(message)
                    updateConversation(message)
                }
            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Error while sending message")
            }
    }

    fun updateUser(user: User) {
        database.child(FIREBASE_USER_PATH)
            .child(user.id)
            .setValue(user)
    }

    private fun updateConversation(message: Message) {
        val lastMessage = when (MessageType.fromInt(message.messageType)) {
            MessageType.MESSAGE ->  message.body?.fieldOne
            MessageType.GIF ->  "GIF"
            MessageType.IMAGE ->  "Image"
            MessageType.ATTACHMENT -> "Attachment"
            MessageType.LOCATION -> "Location"
        }

        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(message.ownerId)
            .child(FIREBASE_LAST_MESSAGE)
            .setValue(lastMessage)

        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(message.ownerId)
            .child(FIREBASE_TIMESTAMP)
            .setValue(message.timestamp)

        CoroutineScope(Dispatchers.IO).launch {
            roomDatabase.messageDao().update(message)
        }
    }

    fun startConversationListener(appContext: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = roomDatabase.conversationDao()
                .getConvesationDataFromEmail(DataRepository.currentUserEmail)

            data.forEach {
                launchListener(appContext, it.id)
            }
        }
    }

    // listen for user updates such as Profile photo, status, username
    fun launchUserListener(){
        database.child(FIREBASE_USER_PATH)
            .addValueEventListener( object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { userSnapshot ->
                        val user = userSnapshot.getValue(User::class.java)
                        user?.let {
                            roomDatabase.addUser(user)
                            updateConversation(user)
                        }
                    }
                }
            })
    }

    // listen for new upcoming conversations
    fun listenForNewConversations(){
        CoroutineScope(Dispatchers.IO).launch {
            val user = DataRepository.user

            database.child(FIREBASE_PRIVATE_CHAT_PATH)
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach { conversationId ->
                            val conversation = conversationId.getValue(Conversation::class.java)

                            if(user != null && conversation != null){
                                if(user.id in conversation.id){

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val conversationExist = roomDatabase.conversationDao().getById(conversation.id)
                                        if(conversationExist != null){
                                            roomDatabase.conversationDao().update(conversation)
                                        }else{
                                            roomDatabase.conversationDao().add(conversation)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
        }
    }

    // listener for ChatActivity
    private fun launchListener(appContext: Context, conversationId: String) {
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversationId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                override fun onChildRemoved(p0: DataSnapshot) {}

                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                    val message = dataSnapshot.getValue(Message::class.java)
                    message?.let {
                        roomDatabase.addMessage(it)

                        if(it.receiverName == DataRepository.currentUserEmail){
                            removeMessageFromFirebase(it)
                        }

                        if(DataRepository.appIsInBackground()){
                            MyNotificationManager.displayNotification(appContext, 1, message)
                        }
                    }
                }
            })
    }

    private fun removeMessageFromFirebase(message: Message){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(message.ownerId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .child(message.id.toString())
            .removeValue()
    }

    private fun updateConversation( user: User){
        CoroutineScope(Dispatchers.IO).launch {
            val conversation = roomDatabase.conversationDao().getConversationByUserEmail(user.email)

            conversation?.let {
                if(conversation.userOneEmail == user.email){
                    conversation.userOneUsername = user.name
                    conversation.userOnePhoto = user.profilePhoto
                }else{
                    conversation.userTwoUsername = user.name
                    conversation.userTwoPhoto = user.profilePhoto
                }

                roomDatabase.conversationDao().update(conversation)
            }
        }
    }
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

fun FirebaseFirestore.updateCurrentUser(context: Context, user: User){
    collection(FirebaseUtil.FIREBASE_USER_PATH)
        .document(user.email)
        .set(user)
        .addOnSuccessListener {
            MyRoomDatabase.getMyRoomDatabase(context)?.updateUser(user)
            context.toast("User updated")
            FirebaseUtil.updateUser(user)
        }
        .addOnFailureListener {
            Log.d(LogUtil.TAG, "Error while updating user")
        }
}