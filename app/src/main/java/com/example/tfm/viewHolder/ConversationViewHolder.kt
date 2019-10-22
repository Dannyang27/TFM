package com.example.tfm.viewHolder

import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.model.Conversation
import com.example.tfm.util.launchChatActivity
import com.example.tfm.util.setTime
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.toast

class ConversationViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private var id: String = ""
    private var email: String = ""
    private val image: CircleImageView = view.findViewById(R.id.profile_image)
    private val name :EmojiTextView= view.findViewById(R.id.name_chat)
    private val lastTime: TextView = view.findViewById(R.id.last_message_time_chat)
    private val lastMessage :EmojiTextView = view.findViewById(R.id.last_message_chat)

    init {
        view.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(it.context)
            val language = pref.getString("chatLanguage", "Default")
            if(language == "Default"){
                it.context.toast("Please select a language Settings > Download Model")
            }else{
                it.context.launchChatActivity(id, email, false)
            }
        }
    }

    fun bindViewHolder(conversation: Conversation){
        id = conversation.id
        if(conversation.userOneEmail.contains(DataRepository.currentUserEmail)){
            email = conversation.userTwoEmail
            name.text = conversation.userTwoUsername
        }else{
            email = conversation.userOneEmail
            name.text = conversation.userOneUsername
        }

        lastMessage.text = if(conversation.lastMessage.toString().isNotEmpty()) conversation.lastMessage else image.context.getString(R.string.bethefirst)
        setTime(lastTime, conversation.timestamp)
    }
}