package com.example.tfm.viewHolder

import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.tfm.R

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val layout : RelativeLayout = view.findViewById(R.id.message_layout)
    val placeholder: LinearLayout = view.findViewById(R.id.message_placeholder)
    val userPhoto: ImageView = view.findViewById(R.id.message_image)
    val text: EmojiTextView = view.findViewById(R.id.senderMessage)
    val time: TextView = view.findViewById(R.id.sender_message_time)
}