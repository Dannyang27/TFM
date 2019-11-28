package com.example.tfm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import com.example.tfm.diffUtil.EmojiDiffCallback
import com.example.tfm.model.EmojiFrequency
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.viewHolder.EmojiViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmojiListAdapter (private val emojis: ArrayList<String>): RecyclerView.Adapter<EmojiViewHolder>(){

    override fun getItemCount() = emojis.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_emojitext, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val emoji = emojis[position]
        holder.emojiText.text = emoji
        holder.itemView.setOnClickListener {
            appendEmoji(emoji)
            incrementEmojiUsage(emoji)
        }
    }

    fun updateEmoji(newEmojis: ArrayList<String>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(EmojiDiffCallback(emojis, newEmojis))
        emojis.clear()
        emojis.addAll(newEmojis)
        diffResult.dispatchUpdatesTo(this)
    }


    private fun appendEmoji(emoji: String){
        ChatActivity.emojiEditText.requestFocus()
        ChatActivity.emojiEditText.append(emoji)
    }

    private fun incrementEmojiUsage(emojiCode: String){
        CoroutineScope(Dispatchers.IO).launch {
            val roomDatabase = MyRoomDatabase.INSTANCE
            roomDatabase?.let {
                val emoji = roomDatabase.emojiDao().getEmojiIfExist(emojiCode)

                if(emoji != null){
                    emoji.frequency = emoji.frequency + 1
                    roomDatabase.emojiDao().update(emoji)
                }else{
                    roomDatabase.emojiDao().add(EmojiFrequency(emojiCode))
                }
            }
        }
    }
}