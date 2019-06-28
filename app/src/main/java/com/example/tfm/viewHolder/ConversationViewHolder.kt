package com.example.tfm.viewHolder

import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.tfm.R
import de.hdodenhof.circleimageview.CircleImageView

class ConversationViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val image = view.findViewById(R.id.profile_image) as CircleImageView
    val name :EmojiTextView= view.findViewById(R.id.name_chat)
    val lastTime = view.findViewById(R.id.last_message_time_chat) as TextView
    val lastMessage :EmojiTextView = view.findViewById(R.id.last_message_chat)
}