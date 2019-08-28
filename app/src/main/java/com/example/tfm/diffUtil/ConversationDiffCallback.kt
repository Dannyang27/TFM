package com.example.tfm.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.tfm.model.Conversation

class ConversationDiffCallback(private val oldConversations: MutableList<Conversation>, private val newConversations: MutableList<Conversation>) : DiffUtil.Callback(){
    override fun getOldListSize() = oldConversations.size
    override fun getNewListSize() = newConversations.size
    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = oldConversations[oldPosition].id == newConversations[newPosition].id
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = oldConversations[oldPosition] == newConversations[newPosition]
}