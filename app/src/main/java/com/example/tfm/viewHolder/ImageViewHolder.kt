package com.example.tfm.viewHolder

import android.net.Uri
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
import com.example.tfm.activity.ImageDisplayActivity
import com.example.tfm.activity.ImageToolActivity
import com.example.tfm.data.DataRepository
import com.example.tfm.enum.MediaSource
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
        layout.gravity = Gravity.END
        userPhoto.visibility = View.GONE
        time.gravity = Gravity.END
        placeholder.setBackgroundColor(context.getColor(R.color.colorAccent))
        username.visibility = View.GONE
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.START
        userPhoto.visibility = View.VISIBLE
        time.gravity = Gravity.START
        placeholder.setBackgroundColor(context.getColor(R.color.colorWhite))
    }

    private fun setImageOrGif(message: Message){
        if(MessageType.fromInt(message.messageType) == MessageType.IMAGE){
            val bitmap = message.body?.fieldOne?.toBitmap()
            Glide.with(layout.context)
                .load(bitmap)
                .into(media)

            media.setOnClickListener {
                ImageDisplayActivity.launchBitmap(layout.context, bitmap, MediaSource.GALLERY)
            }

        }else{
            val url = message.body?.fieldOne
            Glide.with(layout.context)
                .asGif()
                .centerCrop()
                .load(url)
                .into(media)

            media.setOnClickListener {
                ImageDisplayActivity.launchGif(layout.context, Uri.parse(url), MediaSource.GIF)
            }
        }
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}