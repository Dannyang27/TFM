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
import kotlin.coroutines.CoroutineContext

class EmojiFlagAdapter : BaseAdapter, CoroutineScope {

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    var flags = arrayListOf<String>()
    var context : Context? = null

    constructor(context: Context, emojiUnicodes: ArrayList<String>){
        this.context = context
        this.flags = emojiUnicodes
    }

    override fun getCount() = flags.size

    override fun getItem(position: Int) = flags[position]

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

        val imgBtn = emojiView.findViewById(R.id.emoji_button) as Button

        launch {
            val job = async {
                withContext(Dispatchers.IO) {
                    val unicode = flags[position]

                    withContext(Dispatchers.Main){
                        imgBtn.text = unicode

                        imgBtn.setOnClickListener {
                            ChatActivity.emojiEditText.requestFocus()
                            ChatActivity.emojiEditText.append(unicode)
                        }
                    }
                }
                emojiView
            }
            job.await()
        }

        return emojiView
    }
}