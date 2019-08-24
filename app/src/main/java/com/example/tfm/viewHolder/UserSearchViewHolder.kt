package com.example.tfm.viewHolder

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.fragments.PrivateFragment
import com.example.tfm.model.Conversation
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.checkIfConversationExists
import org.jetbrains.anko.toast

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view){
    lateinit var email: String
    val photo: ImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)

    init {
        view.setOnClickListener {
            val context = username.context
            context.toast("Email: $email")
            FirebaseUtil.database.checkIfConversationExists(email)
//            val intent = Intent(context, ChatActivity::class.java)
//            context.startActivity(intent)
        }
    }
}