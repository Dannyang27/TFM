package com.example.tfm.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.room.database.MyRoomDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
                val currentUser = FirebaseAuth.getInstance().currentUser
                val roomDatabase = MyRoomDatabase.getMyRoomDatabase(username.context)
                roomDatabase?.getMutualConversation(username.context, currentUser?.email!!, email)
            }
        }
    }
}