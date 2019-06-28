package com.example.tfm.viewHolder

import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.example.tfm.R

class SenderMessageViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
    val text: EmojiTextView = view.findViewById(R.id.senderMessage)
}