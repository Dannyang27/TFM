package com.example.tfm.viewHolder

import android.support.text.emoji.widget.EmojiTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.tfm.R

class SenderMessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val text: EmojiTextView = view.findViewById(R.id.senderMessage)
}