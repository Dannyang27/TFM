package com.example.tfm.viewHolder

import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.tfm.R

class ReceiverMessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val text : EmojiTextView = view.findViewById(R.id.receiverMessage)
    val time: TextView = view.findViewById(R.id.receiver_message_time)
}