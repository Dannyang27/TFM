package com.example.tfm.viewHolder

import android.graphics.Bitmap
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.util.AuthUtil
import com.example.tfm.util.TimeUtil
import com.example.tfm.util.showUsernameIfGroup
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val layout: RelativeLayout = view.findViewById(R.id.media_layout)
    private val placeholder: LinearLayout = view.findViewById(R.id.media_placeholder)
    private val userPhoto: ImageView = view.findViewById(R.id.media_photo)
    private val username: EmojiTextView = view.findViewById(R.id.media_username)
    private val image: ImageView = view.findViewById(R.id.media_image)
    private val caption: EmojiTextView = view.findViewById(R.id.media_caption)
    private val time: TextView = view.findViewById(R.id.media_time)

    fun initImageViewHolder(message: Message){
        if(message.senderName == AuthUtil.getAccountEmail()){
            setSenderViewHolder()
        }else {
            setReceiverViewHolder()
            username.showUsernameIfGroup(message.isPrivateChat, message.senderName)
        }

        setImageOrGif(message)
        setTime(message.timestamp)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(60), getDpValue(10), getDpValue(15), getDpValue(10))
        layout.gravity = Gravity.RIGHT
        userPhoto.visibility = View.GONE
        time.gravity = Gravity.RIGHT
        placeholder.setBackgroundColor(context.getColor(R.color.colorAccent))
        caption.setTextColor(context.getColor(R.color.colorWhite))
        username.visibility = View.GONE
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.LEFT
        userPhoto.visibility = View.VISIBLE
        time.gravity = Gravity.LEFT
        placeholder.setBackgroundColor(context.getColor(R.color.colorWhite))
        caption.setTextColor(context.getColor(R.color.colorReceiverMessage))
    }

    private fun setImageOrGif(message: Message){
        val mediaContent = message.body as MediaContent
        if(message.messageType == MessageType.IMAGE){
            image.setImageBitmap(mediaContent.content as Bitmap)
        }else{
            Glide.with(image.context)
                .asGif()
                .centerCrop()
                .load(mediaContent.content as String)
                .into(image)
        }

        image.setOnClickListener {
            image.context.toast("Image pressed, expanding...")
        }

        if(mediaContent.caption.isNotEmpty()){
            setCaption(mediaContent.caption)
        }
    }

    private fun setCaption(caption: String){
        this.caption.text = caption
        this.caption.visibility = View.VISIBLE
    }

    private fun setTime(time: Long){
        this.time.text = TimeUtil.setTimeFromTimeStamp(time)
    }
    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}