package com.example.tfm.viewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import androidx.emoji.widget.EmojiTextView
import com.example.tfm.R

class ReceiverImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val image: ImageView = view.findViewById(R.id.receiver_image_viewholder)
    val caption: EmojiTextView = view.findViewById(R.id.receiver_caption_viewholder)
}