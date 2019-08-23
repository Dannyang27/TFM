package com.example.tfm.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val photo: ImageView = view.findViewById(R.id.user_photo)
    val username: EmojiTextView = view.findViewById(R.id.user_username)
    val status: EmojiTextView = view.findViewById(R.id.user_status)
}