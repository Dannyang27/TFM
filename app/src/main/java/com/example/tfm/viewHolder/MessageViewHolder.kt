package com.example.tfm.viewHolder

import android.content.Context
import android.view.Gravity
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.tfm.R
import com.example.tfm.enum.Sender
import com.example.tfm.model.Message
import com.example.tfm.util.TimeUtil
import org.jetbrains.anko.displayMetrics

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val layout : RelativeLayout = view.findViewById(R.id.message_layout)
    val placeholder: LinearLayout = view.findViewById(R.id.message_placeholder)
    val userPhoto: ImageView = view.findViewById(R.id.message_image)
    val body: EmojiTextView = view.findViewById(R.id.senderMessage)
    val time: TextView = view.findViewById(R.id.sender_message_time)

    fun initMessageViewHolder(message: Message){
        if(message.sender == Sender.OWN){
            setSenderViewHolder()
        }else{
            setReceiverViewHolder()
        }

        setBody(message.body as String)
        setTime(message.timestamp)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(getDpValue(context,60), 0, 0, 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.RIGHT
        placeholder.background = context.getDrawable(R.drawable.sender_message)
        layout.setPadding(0,getDpValue(context, 10),getDpValue(context, 15), getDpValue(context, 10))
        userPhoto.visibility = View.GONE
        body.setTextColor(context.getColor(R.color.colorWhite))
        time.gravity = Gravity.RIGHT
        time.setTextColor(context.getColor(R.color.imageSenderBackground))
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(0, 0, getDpValue(context,60), 0)
        layout.layoutParams = layoutParams
        layout.gravity = Gravity.LEFT
        placeholder.background = context.getDrawable(R.drawable.receiver_message)
        layout.setPadding(getDpValue(context,15),getDpValue(context, 10), 0, getDpValue(context, 10))
        userPhoto.visibility = View.VISIBLE
        body.setTextColor(context.getColor(R.color.colorReceiverMessage))
        time.gravity = Gravity.LEFT
        time.setTextColor(context.getColor(R.color.imageSenderBackground))
    }

    private fun setBody( body: String){
        this.body.text = body
    }

    private fun setTime(time: Long){
        this.time.text = TimeUtil.setTimeFromTimeStamp(time)
    }

    private fun getDpValue( context: Context, dpValue: Int): Int = (dpValue * context.displayMetrics.density).toInt()
}