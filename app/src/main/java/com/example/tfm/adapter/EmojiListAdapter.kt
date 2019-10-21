package com.example.tfm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.viewHolder.EmojiViewHolder

class EmojiListAdapter (private val emojis: ArrayList<String>): RecyclerView.Adapter<EmojiViewHolder>(){

    override fun getItemCount() = emojis.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_emojitext, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val emoji = emojis[position]
        holder.emojiText.text = emoji
        holder.itemView.setOnClickListener {
            ChatActivity.emojiEditText.requestFocus()
            ChatActivity.emojiEditText.append(emoji)
        }
    }
}