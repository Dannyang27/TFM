package com.example.tfm.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.model.Conversation
import com.example.tfm.viewHolder.ConversationViewHolder

class ConversationAdapter(private val conversations: MutableList<Conversation>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = conversations.size
}