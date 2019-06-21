package com.example.tfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import com.example.tfm.R
import com.example.tfm.model.Emoji
import org.jetbrains.anko.toast

class EmojiGridViewAdapter : BaseAdapter{

    var emojis = arrayListOf<Emoji>()
    var context : Context? = null

    constructor(context: Context, emojis: ArrayList<Emoji>){
        this.context = context
        this.emojis = emojis
    }

    override fun getCount() = emojis.size

    override fun getItem(position: Int) = emojis[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val columns = 9
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val emojiView = inflater.inflate(R.layout.emoji_viewholder, null)
        val width = parent?.width

        if(width?.compareTo(0)!! > 0){
            val value = width.div(columns)
            emojiView.layoutParams = ViewGroup.LayoutParams(value, value)
        }

        val emoji = emojis[position]
        val imgBtn = emojiView.findViewById(R.id.emoji_button) as ImageButton
        imgBtn.setImageDrawable(emoji.drawable)
        imgBtn.setOnClickListener {
            context?.toast("EmojiTab: " + emoji.unicodeChar)
        }

        return emojiView
    }
}