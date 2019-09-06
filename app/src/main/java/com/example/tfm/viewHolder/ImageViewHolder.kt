package com.example.tfm.viewHolder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import com.example.tfm.util.showUsernameIfGroup
import com.example.tfm.util.toBitmap
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val layout: RelativeLayout = view.findViewById(R.id.media_layout)
    private val placeholder: LinearLayout = view.findViewById(R.id.media_placeholder)
    private val userPhoto: ImageView = view.findViewById(R.id.media_photo)
    private val username: EmojiTextView = view.findViewById(R.id.media_username)
    private val media: ImageView = view.findViewById(R.id.media_image)
    private val caption: EmojiTextView = view.findViewById(R.id.media_caption)
    private val time: TextView = view.findViewById(R.id.media_time)

    fun initImageViewHolder(message: Message){
        if(message.senderName == DataRepository.currentUserEmail){
            setSenderViewHolder()
        }else {
            setReceiverViewHolder()
            username.showUsernameIfGroup(false, message.senderName)
        }

        setImageOrGif(message)
        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.isSent)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(60), getDpValue(10), getDpValue(15), getDpValue(10))
        layout.gravity = Gravity.RIGHT
        userPhoto.visibility = View.GONE
        time.gravity = Gravity.RIGHT
        placeholder.setBackgroundColor(context.getColor(R.color.colorAccent))
        caption.setTextColor(context.getColor(R.color.colorPrimaryText))
        username.visibility = View.GONE
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.LEFT
        userPhoto.visibility = View.VISIBLE
        time.gravity = Gravity.LEFT
        placeholder.setBackgroundColor(context.getColor(R.color.colorWhite))
        caption.setTextColor(context.getColor(R.color.colorPrimaryText))
    }

    private fun setImageOrGif(message: Message){
        val caption = message.body?.fieldTwo
        if(MessageType.fromInt(message.messageType) == MessageType.IMAGE){
            media.setImageBitmap(message.body?.fieldOne?.toBitmap())
        }else{
            val url = message.body?.fieldOne

            Glide.with(layout.context)
                .asGif()
                .centerCrop()
                .load(url)
                .into(media)
        }

        if(caption!!.isNotEmpty()){
            setCaption(caption)
        }

        media.setOnClickListener {
            layout.context.toast("Image pressed, expanding...")
        }
    }

    private fun setCaption(caption: String){
        this.caption.text = caption
        this.caption.visibility = View.VISIBLE
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}