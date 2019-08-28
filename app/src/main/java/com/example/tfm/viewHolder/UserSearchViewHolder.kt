package com.example.tfm.viewHolder

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Conversation
import com.example.tfm.model.Message
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.LogUtil
import com.example.tfm.util.addConversation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view), CoroutineScope{
    private val job = Job()
    override val coroutineContext = job + Main

    lateinit var email: String
    val photo: ImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)

    init {
        view.setOnClickListener {
            launch {
                val firestore = FirebaseFirestore.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser
                val taskOne = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(currentUser?.email!!).get().await()
                val userOne = taskOne.toObject(User::class.java)

                val taskTwo = firestore.collection(FirebaseUtil.FIREBASE_USER_PATH).document(email).get().await()
                val userTwo = taskTwo.toObject(User::class.java)

                var userOneHash = userOne.hashCode().toLong()
                var userTwoHash = userTwo.hashCode().toLong()

                if(userOneHash > userTwoHash){
                    val tmp = userOneHash
                    userOneHash = userTwoHash
                    userTwoHash = tmp
                }

                val hashcode = userOneHash.toString().plus(userTwoHash.toString())
                val message = Message(1, "1", userOne?.email!!, userTwo?.email!!, MessageType.MESSAGE, "Hello World", 1,false, false, "EN")
                val conversation = Conversation(hashcode, userOne.email, userTwo.email, mutableListOf(message), 1, mutableListOf(), true )

                firestore.addConversation(username.context, conversation)
            }
        }
    }
}