package com.example.tfm.viewHolder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.activity.MainActivity
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import com.example.tfm.util.showUsernameIfGroup
import org.jetbrains.anko.displayMetrics

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val username: EmojiTextView = view.findViewById(R.id.message_username)
    private val layout : RelativeLayout = view.findViewById(R.id.message_layout)
    private val placeholder: LinearLayout = view.findViewById(R.id.message_placeholder)
    private val userPhoto: ImageView = view.findViewById(R.id.message_image)
    private val body: EmojiTextView = view.findViewById(R.id.message_body)
    private val time: TextView = view.findViewById(R.id.message_time)

    fun initMessageViewHolder(message: Message){
        if(message.senderName == MainActivity.currentUserEmail){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
            username.showUsernameIfGroup(true, message.senderName)
        }

        setBody(message.body?.fieldOne.toString())
        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.isSent)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(getDpValue(60), 0, 0, 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.RIGHT
        placeholder.background = context.getDrawable(R.drawable.sender_message)
        layout.setPadding(0,getDpValue(10),getDpValue(15), getDpValue(10))
        userPhoto.visibility = View.GONE
        body.setTextColor(context.getColor(R.color.colorWhite))
        body.gravity = Gravity.RIGHT
        time.gravity = Gravity.RIGHT
        username.visibility = View.GONE
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(0, 0, getDpValue(60), 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.LEFT
        placeholder.background = context.getDrawable(R.drawable.receiver_message)
        layout.setPadding(getDpValue(15),getDpValue(10), 0, getDpValue(10))
        userPhoto.visibility = View.VISIBLE
        body.setTextColor(context.getColor(R.color.colorPrimaryText))
        body.gravity  = Gravity.LEFT
        time.gravity = Gravity.LEFT
    }

    private fun setBody( body: String){
        this.body.text = body
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}