package com.example.tfm.viewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import com.example.tfm.R

class SenderImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val layout: RelativeLayout = view.findViewById(R.id.media_layout)
    val placeholder: LinearLayout = view.findViewById(R.id.media_placeholder)
    val image: ImageView = view.findViewById(R.id.media_image)
    val caption: EmojiTextView = view.findViewById(R.id.media_caption)
    val time: TextView = view.findViewById(R.id.media_time)
}