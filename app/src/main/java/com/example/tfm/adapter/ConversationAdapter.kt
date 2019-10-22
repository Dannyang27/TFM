package com.example.tfm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
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
//        val roomDatabase =  MyRoomDatabase.getMyRoomDatabase(holder.name.context)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            if(conversation.userOne == DataRepository.currentUserEmail){
//                roomDatabase?.getUserNameByEmail(holder.name, conversation.userTwo.toString())
//                holder.email = conversation.userTwo.toString()
//            }else{
//                roomDatabase?.getUserNameByEmail(holder.name, conversation.userOne.toString())
//                holder.email = conversation.userOne.toString()
//            }
//        }
//
//
//
//        holder.itemView.setOnClickListener {
//            val context = it.context
//            val languagePreference = PreferenceManager.getDefaultSharedPreferences(context).getLanguage()
//            if(languagePreference == "Default"){
//                context.toast(context.getString(R.string.selectLanguagePreference))
//            }else{
//                context.launchChatActivity(conversation.id, holder.email, true)
//            }
//        }
        holder.bindViewHolder(conversation)
    }

    override fun getItemCount() = conversations.size

    fun updateList( newConversations : MutableList<Conversation>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ConversationDiffCallback(conversations, newConversations))
        conversations.clear()
        conversations.addAll(newConversations.sortedByDescending { it.timestamp })
        diffResult.dispatchUpdatesTo(this)
    }
}