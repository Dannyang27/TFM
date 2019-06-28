package com.example.tfm.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.viewHolder.ConversationViewHolder
import org.jetbrains.anko.toast

class ConversationAdapter(private val conversations: MutableList<String>): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_viewholder, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = conversations.size
}