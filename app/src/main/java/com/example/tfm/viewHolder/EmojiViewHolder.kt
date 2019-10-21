package com.example.tfm.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R

class EmojiViewHolder (view: View): RecyclerView.ViewHolder(view){
    val emojiText: TextView = view.findViewById(R.id.adapter_emoji)
}