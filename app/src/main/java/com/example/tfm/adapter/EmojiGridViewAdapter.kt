package com.example.tfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.tfm.R
import com.example.tfm.activity.ChatActivity
import kotlinx.coroutines.*

class EmojiGridViewAdapter : BaseAdapter, CoroutineScope{

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    var emojis = arrayListOf<String>()
    var context : Context? = null


    constructor(context: Context, emojis: ArrayList<String>){
        this.context = context
        this.emojis = emojis
    }

    override fun getCount() = emojis.size

    override fun getItem(position: Int) = emojis[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val columns = 9
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val emojiView = inflater.inflate(R.layout.viewholder_emoji, null)
        val width = parent?.width

        if(width?.compareTo(0)!! > 0){
            val value = width.div(columns)
            emojiView.layoutParams = ViewGroup.LayoutParams(value, value)
        }

        val imgBtn = emojiView.findViewById(R.id.emoji_button) as Button

        launch {
            withContext(Dispatchers.IO) {
                val emoji = emojis[position]

                withContext(Dispatchers.Main) {
                    imgBtn.text = emoji

                    imgBtn.setOnClickListener {
                        ChatActivity.emojiEditText.requestFocus()
                        ChatActivity.emojiEditText.append(emoji)
                    }
                }
            }
        }

        return emojiView
    }
}