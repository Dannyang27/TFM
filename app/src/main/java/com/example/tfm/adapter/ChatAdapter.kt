package com.example.tfm.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tfm.R
import com.example.tfm.viewHolder.ConversationViewHolder
import org.jetbrains.anko.toast

class ChatAdapter(private val conversations: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_viewholder, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            it.context.toast("Working")
        }
    }

    override fun getItemCount() = conversations.size
}