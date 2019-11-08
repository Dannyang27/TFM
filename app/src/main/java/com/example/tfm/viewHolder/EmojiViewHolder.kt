package com.example.tfm.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R

class EmojiViewHolder (view: View): RecyclerView.ViewHolder(view){
    @BindView(R.id.adapter_emoji) lateinit var emojiText: TextView

    init {
        ButterKnife.bind(this, view)
    }
}