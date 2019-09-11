package com.example.tfm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.diffUtil.ConversationDiffCallback
import com.example.tfm.model.Conversation
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.setTime
import com.example.tfm.viewHolder.ConversationViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversationAdapter(private val conversations: MutableList<Conversation>): RecyclerView.Adapter<ConversationViewHolder>(), CoroutineScope{
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        val roomDatabase =  MyRoomDatabase.getMyRoomDatabase(holder.name.context)

        launch {
            if(conversation.userOne == DataRepository.currentUserEmail){
                roomDatabase?.getUserNameByEmail(holder.name, conversation.userTwo.toString())
                holder.email = conversation.userTwo.toString()
            }else{
                roomDatabase?.getUserNameByEmail(holder.name, conversation.userOne.toString())
                holder.email = conversation.userOne.toString()
            }
        }

        holder.lastMessage.text = if(conversation.lastMessage.toString().isNotEmpty()) conversation.lastMessage else holder.image.context.getString(R.string.bethefirst)
        holder.setTime(holder.lastTime, conversation.timestamp)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("conversationId", conversation.id)
            intent.putExtra( "receiverEmail", holder.email)
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