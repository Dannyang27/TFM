package com.example.tfm.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.tfm.model.Message

class MessageDiffCallback(private val oldMessages: MutableList<Message>, private val newMessages: MutableList<Message>) : DiffUtil.Callback(){
    override fun getOldListSize() = oldMessages.size
    override fun getNewListSize() = newMessages.size
    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = oldMessages[oldPosition].id == newMessages[newPosition].id
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = oldMessages[oldPosition] == newMessages[newPosition]
}