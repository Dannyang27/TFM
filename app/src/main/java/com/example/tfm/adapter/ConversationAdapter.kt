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
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.viewHolder.ConversationViewHolder
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ConversationAdapter(private val conversations: MutableList<Conversation>): RecyclerView.Adapter<ConversationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]

        val currentUser = FirebaseAuth.getInstance().currentUser?.email
        val roomDatabase =  MyRoomDatabase.getMyRoomDatabase(holder.name.context)
        if(conversation.userOne == currentUser){
            roomDatabase?.getUserNameByEmail(holder.name, conversation.userTwo!!)
        }else{
            roomDatabase?.getUserNameByEmail(holder.name, conversation.userOne!!)
        }

        holder.lastMessage.text = if(conversation.lastMessage!!.isNotEmpty()) conversation.lastMessage else "Be the first to start the conversation"
        holder.lastTime.text = "${SimpleDateFormat("HH:mm").format(Date(conversation.timestamp))}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("conversationId", conversation.id)
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