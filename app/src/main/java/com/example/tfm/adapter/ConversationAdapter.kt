package com.example.tfm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.diffUtil.ConversationDiffCallback
import com.example.tfm.model.Conversation
import com.example.tfm.viewHolder.ConversationViewHolder

class ConversationAdapter(private val conversations: MutableList<Conversation>): RecyclerView.Adapter<ConversationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]

        //TODO need to evaluate which user is you or friend
        holder.name.text = conversation.userOne

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = conversations.size

    fun updateList( newConversations : MutableList<Conversation>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ConversationDiffCallback(conversations, newConversations))
        conversations.clear()
        conversations.addAll(newConversations)
        diffResult.dispatchUpdatesTo(this)
    }
}