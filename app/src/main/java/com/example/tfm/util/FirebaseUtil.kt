package com.example.tfm.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.tfm.activity.SignupActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.viewmodel.LoginViewModel
import com.example.tfm.viewmodel.SignupViewModel
import com.example.tfm.viewmodel.UserSearcherViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun login(context: Context, user: String, password: String){
        firebaseAuth?.signInWithEmailAndPassword(user, password)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    prefs.updateCurrentUser(user, password)

                    CoroutineScope(Dispatchers.IO).launch {
                        val loginTask = FirebaseFirestore.getInstance().collection(FIREBASE_USER_PATH).document(user).get().await()
                        DataRepository.user = loginTask.toObject(User::class.java)
                        DataRepository.currentUserEmail = user
                        Log.d("TFM", "CurrentUserEmail: $user")
                        LoginViewModel.isSuccessful.postValue(true)
                    }

                }else{
                    context.toast("Wrong user/password")
                    LoginViewModel.isLoading.postValue(false)
                }
            }
    }

    fun createNewUser(context: Context, username: String, email: String, password: String){
        val user = User("", email, username, "", "")
        val hashcode = user.hashCode().toString()
        user.id = hashcode

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{
                FirebaseFirestore.getInstance().addUser(context, user)
                SignupActivity.currentUserEmail = email
                SignupActivity.currentUserPassword = password
            }.addOnFailureListener {
                SignupViewModel.isJoinUsSuccessful.postValue(false)
                context.toast("Cannot create user with those inputs")
            }
    }


    fun addUser(context: Context, user: User){
        database.child(FIREBASE_USER_PATH)
            .child(user.id).setValue(user)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "FirebaseDatabase user added successfully")

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                prefs.updateCurrentUser(SignupActivity.currentUserEmail, SignupActivity.currentUserPassword)

                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
                roomDatabase?.addUser(user)

                SignupViewModel.isJoinUsSuccessful.postValue(true)
            }.addOnFailureListener {
                SignupViewModel.isJoinUsSuccessful.postValue(false)
            }
    }

    fun loadUserConversation(conversations: MutableLiveData<MutableList<Conversation>>, userHash: String){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(@NonNull p0: DatabaseError) {}

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    val list = mutableListOf<Conversation>()
                    dataSnapshot.children.forEach{
                        val conv = it.getValue(Conversation::class.java)!!
                        if(conv.id.contains(userHash)){
                            CoroutineScope(Dispatchers.IO).launch {
                                val jobOne = launch {
                                    val taskOne = FirebaseFirestore.getInstance()
                                        .collection(FIREBASE_USER_PATH)
                                        .document(conv.userOneEmail).get().await()
                                    val user = taskOne.toObject(User::class.java)
                                    conv.userOneUsername = user?.name.toString()
                                    conv.userOnePhoto = user?.profilePhoto.toString()
                                }
                                val jobTwo = launch {
                                    val taskTwo = FirebaseFirestore.getInstance()
                                        .collection(FIREBASE_USER_PATH)
                                        .document(conv.userTwoEmail).get().await()

                                    val user = taskTwo.toObject(User::class.java)
                                    conv.userTwoUsername = user?.name.toString()
                                    conv.userTwoPhoto = user?.profilePhoto.toString()
                                }

                                jobOne.join()
                                jobTwo.join()
                                list.add(conv)
                                conversations.postValue(list)
                            }
                        }
                    }
                }
            })
    }

    fun loadMessageFromConversation(messages: MutableLiveData<MutableList<Message>>, conversationId: String){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversationId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(snapShot: DataSnapshot) {
                    val messageList = mutableListOf<Message>()
                    snapShot.children.forEach{
                        val message = it.getValue(Message::class.java)!!
                        messageList.add(message)
                    }

                    messages.postValue(messageList)
                }
            })
    }

//    fun getUsersByName(name: String){
//        if(name.isNotEmpty()){
//            database.child(FIREBASE_USER_PATH).addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.d(LogUtil.TAG, databaseError.message)
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val users = mutableListOf<User>()
//                    dataSnapshot.children.forEach {
//                        val user = it.getValue(User::class.java)
//                        user?.let {
//                            if(it.name.contains(name, ignoreCase = true)){
//                                users.add(it)
//                            }
//                        }
//                    }
//                    UserSearcherActivity.updateList(users)
//                }
//            })
//        }
//    }


    fun loadAllUsers( users: MutableLiveData<MutableList<User>>, filterText: String?){
        database.child(FIREBASE_USER_PATH).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if(user?.email != DataRepository.currentUserEmail && user?.name?.contains(filterText.toString(), ignoreCase = true)!!){
                        UserSearcherViewModel.allUserList.add(user)
                    }
                }
                users.postValue(UserSearcherViewModel.allUserList)
            }
        })
    }

//    fun loadUserConversation(context: Context, user: String){
//        val userConversations = MyRoomDatabase.getMyRoomDatabase(context)?.conversationDao()?.getUserConversations(user)!!
//
//        if(userConversations.isEmpty()){
//            context.launchMainActivity()
//        }else{
//            userConversations.forEach {
//                val conversation = it
//                database.child(FIREBASE_PRIVATE_CHAT_PATH).child(conversation.id).child(
//                    FIREBASE_PRIVATE_MESSAGE_PATH).limitToLast(10)
//                    .addListenerForSingleValueEvent(object: ValueEventListener{
//                    override fun onCancelled(p0: DatabaseError) {}
//
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        dataSnapshot.children.forEach{
//                            val message = it.getValue(Message::class.java)!!
//                            conversation.messages.add(message)
//                        }
//                        DataRepository.addConversation(it.id, conversation)
//                        context.launchMainActivity()
//                    }
//                })
//            }
//        }
//    }

    fun addPrivateChat(context: Context, conversation: Conversation){
        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(conversation.id)
            .setValue(conversation)
//            .addOnSuccessListener {
//                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(context)
//                roomDatabase?.addConversation(conversation)
//                DataRepository.addConversation(conversation.id, conversation)
//                PrivateFragment.updateConversation(DataRepository.getConversations())
//
//                var userToCreate: String?
//                if(conversation.userOneEmail.equals(DataRepository.currentUserEmail)){
//                    userToCreate = conversation.userTwoEmail
//                }else{
//                    userToCreate = conversation.userOneEmail
//                }
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    val user = FirebaseFirestore.getInstance().createRoomUser(userToCreate)
//                    roomDatabase?.addUser(user!!)
//                    Log.d(LogUtil.TAG, "User added to room: ${user.toString()}")
//                }
//
//                context.launchChatActivity(conversation.id, userToCreate.toString(), false)
//        }
    }

//    fun addMessageLocal(message: Message){
//        val newMessages: MutableList<Message> = mutableListOf()
//        newMessages.addAll(ChatActivity.messages)
//        newMessages.add(message)
//        ChatActivity.updateList(newMessages)
//    }

    fun addMessageFirebase(message: Message){
        database.child(FIREBASE_PRIVATE_CHAT_PATH).child(message.ownerId)
            .child(FIREBASE_PRIVATE_MESSAGE_PATH)
            .child(message.timestamp.toString())
            .setValue(message)
            .addOnSuccessListener {
//                DataRepository.addMessage(message)
                updateConversation(message)

            }
            .addOnFailureListener {
                Log.d(LogUtil.TAG, "Error while sending message")
            }
    }

    fun addTranslatedMessage(context: Context, message: Message){
        val fromLanguage: String = PreferenceManager.getDefaultSharedPreferences(context).getString("chatLanguage", "ENGLISH")!!
        val languageCode = FirebaseTranslator.languageCodeFromString(fromLanguage)

        val translator = DataRepository.toEnglishTranslator
        translator?.translate(message.body?.fieldOne.toString())
            ?.addOnSuccessListener {
                val textInEnglish = it
                message.body?.fieldTwo = textInEnglish
                message.body?.fieldThree = languageCode.toString()
                addMessageFirebase(message)
                Log.d(LogUtil.TAG, "Text translated: $textInEnglish")
            }
            ?.addOnFailureListener {
                Log.d(LogUtil.TAG, "Could not translated text")
            }
    }

    fun updateUser(user: User){
        database.child(FIREBASE_USER_PATH)
            .child(user.id)
            .setValue(user)
            .addOnSuccessListener {
                Log.d(LogUtil.TAG, "User ${user.email} has been updated in RealtimeDatabase")
            }
    }

    private fun updateConversation(message: Message){

        var lastMessage: String?
        when(MessageType.fromInt(message.messageType)){
            MessageType.MESSAGE -> lastMessage = message.body?.fieldOne
            MessageType.GIF -> lastMessage = "[GIF]"
            MessageType.IMAGE -> lastMessage = "[Image]"
            MessageType.ATTACHMENT -> lastMessage = "[Attachment]"
            MessageType.LOCATION -> lastMessage = "[Location]"
        }

        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(message.ownerId)
            .child(FIREBASE_LAST_MESSAGE)
            .setValue(lastMessage)

        database.child(FIREBASE_PRIVATE_CHAT_PATH)
            .child(message.ownerId)
            .child(FIREBASE_TIMESTAMP)
            .setValue(message.timestamp)

//        MyRoomDatabase.getMyRoomDatabase(context)?.updateConversation(message, lastMessage.toString())
//        DataRepository.updateConversation(message, lastMessage.toString())
//        PrivateFragment.updateConversation(DataRepository.getConversations())
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

suspend fun FirebaseFirestore.getConversation(conversationId: String): Conversation?{
    val task = collection(FirebaseUtil.FIREBASE_PRIVATE_CHAT_PATH)
        .document(conversationId)
        .get().await()

    val conversation = task.toObject(Conversation::class.java)
    return conversation
}

suspend fun FirebaseFirestore.createNewConversation(context: Context, email: String, newEmail: String){
    val firestore = FirebaseFirestore.getInstance()

    val taskOne = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(email).get().await()
    val userOne = taskOne.toObject(User::class.java)

    val taskTwo = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(newEmail).get().await()
    val userTwo = taskTwo.toObject(User::class.java)

    var userOneHash = userOne?.id?.toLong()!!
    var userTwoHash = userTwo?.id?.toLong()!!

    if(userOneHash > userTwoHash){
        val tmp = userOneHash
        userOneHash = userTwoHash
        userTwoHash = tmp
    }

    val hashcode = userOneHash.toString().plus(userTwoHash.toString())
    val conversation = Conversation(hashcode, userOne.email, "", "", userTwo.email, "","",
        mutableListOf(), "",System.currentTimeMillis(), mutableListOf(), true )

    firestore.addConversation(context, conversation)
}

suspend fun FirebaseFirestore.loadUserConversation(userHash: String): MutableList<Conversation>{
    val conversationList = mutableListOf<Conversation>()
    collection(FirebaseUtil.FIREBASE_PRIVATE_CHAT_PATH)
        .get()
        .addOnSuccessListener { chats ->
            for(chat in chats){
                val conv = chat.toObject(Conversation::class.java)
                Log.d("TFM", "Chat id: $conv.id}")
                if(conv.id.contains(userHash)){
                    CoroutineScope(Dispatchers.IO).launch {
                        val jobOne = launch {
                            val taskOne = collection(FirebaseUtil.FIREBASE_USER_PATH)
                                .document(conv.userOneEmail).get().await()
                            val user = taskOne.toObject(User::class.java)
                            conv.userOneUsername = user?.name.toString()
                            conv.userOnePhoto = user?.profilePhoto.toString()
                        }
                        val jobTwo = launch {
                            val taskTwo = collection(FirebaseUtil.FIREBASE_USER_PATH)
                                .document(conv.userTwoEmail).get().await()

                            val user = taskTwo.toObject(User::class.java)
                            conv.userTwoUsername = user?.name.toString()
                            conv.userTwoPhoto = user?.profilePhoto.toString()
                        }

                        jobOne.join()
                        jobTwo.join()
                        conversationList.add(conv)
//                        conversations.postValue(list)
                    }
                }
            }
        }.await()

    Log.d("TFM", "Finished")
    return conversationList
}