package com.example.tfm.diffUtil

import androidx.recyclerview.widget.DiffUtil

class EmojiDiffCallback(private val oldEmojis: ArrayList<String>, private val newEmojis: ArrayList<String>) : DiffUtil.Callback(){
    override fun getOldListSize() = oldEmojis.size
    override fun getNewListSize() = newEmojis.size
    override fun areItemsTheSame(oldPos: Int, newPos: Int) = oldEmojis[oldPos] == newEmojis[newPos]
    override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldEmojis[oldPos] == newEmojis[newPos]
}
