package com.example.tfm.viewHolder

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.LogUtil
import com.example.tfm.util.createNewConversation
import com.example.tfm.util.getConversation
import com.example.tfm.util.launchChatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view), CoroutineScope{
    private val job = Job()
    override val coroutineContext = job + Main

    lateinit var email: String
    var id: Long = -1
    val photo: ImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)

    init {
        view.setOnClickListener {
            val userId = DataRepository.user?.id?.toLong()
            val conversationId = userId?.getConversation(id)
            username.context.toast(conversationId.toString())
            launch {
                val firestore = FirebaseFirestore.getInstance()
                val conversation = firestore.getConversation(conversationId.toString())
                if(conversation != null){
                    val roomDatabase = MyRoomDatabase.getMyRoomDatabase(username.context)
                    roomDatabase?.addConversation(conversation)
                    username.context.launchChatActivity(conversationId.toString(), email, false)
                }else{
                    firestore.createNewConversation(username.context, DataRepository.currentUserEmail, email)
                }
            }
        }
    }
}