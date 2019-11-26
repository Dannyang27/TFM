package com.example.tfm.viewHolder

import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.activity.ImageDisplayActivity
import com.example.tfm.data.DataRepository.currentUserEmail
import com.example.tfm.enum.MediaSource
import com.example.tfm.enum.MessageType
import com.example.tfm.model.Message
import com.example.tfm.util.setMessageCheckIfSeen
import com.example.tfm.util.setTime
import com.example.tfm.util.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.displayMetrics

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){

    @BindView(R.id.media_layout) lateinit var layout: RelativeLayout
    @BindView(R.id.media_placeholder) lateinit var placeholder: LinearLayout
    @BindView(R.id.media_image) lateinit var media: ImageView
    @BindView(R.id.media_time) lateinit var time: TextView

    init{
        ButterKnife.bind(this, view)
    }

    fun initImageViewHolder(message: Message){
        if(message.senderName == currentUserEmail){
            setSenderViewHolder()
        }else {
            setReceiverViewHolder()
        }

        setImageOrGif(message)
        setTime(time, message.timestamp)
        setMessageCheckIfSeen(time, message.senderName == currentUserEmail, message.isSent)
    }

    private fun setSenderViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(60), getDpValue(10), getDpValue(15), getDpValue(10))
        layout.gravity = Gravity.END
        time.gravity = Gravity.END
        placeholder.setBackgroundColor(context.getColor(R.color.colorAccent))
    }

    private fun setReceiverViewHolder(){
        val context = layout.context
        layout.setPadding(getDpValue(15), getDpValue(10), getDpValue(60), getDpValue(10))
        layout.gravity = Gravity.START
        time.gravity = Gravity.START
        placeholder.setBackgroundColor(context.getColor(R.color.colorWhite))
    }

    private fun setImageOrGif(message: Message){
        CoroutineScope(Dispatchers.IO).launch {
            if(MessageType.fromInt(message.messageType) == MessageType.IMAGE){
                val bitmap = message.body?.fieldOne?.toBitmap()

                withContext(Dispatchers.Main){
                    Glide.with(layout.context)
                        .load(bitmap)
                        .override(getDpValue(200), getDpValue(200))
                        .into(media)

                    media.setOnClickListener {
                        ImageDisplayActivity.launchBitmap(layout.context, bitmap, MediaSource.GALLERY, media)
                    }
                }

            }else{
                val url = message.body?.fieldOne

                withContext(Dispatchers.Main){
                    Glide.with(layout.context)
                        .asGif()
                        .centerCrop()
                        .load(url)
                        .override(getDpValue(200), getDpValue(200))
                        .into(media)

                    media.setOnClickListener {
                        ImageDisplayActivity.launchGif(layout.context, Uri.parse(url), MediaSource.GIF, media)
                    }
                }

            }
        }
    }

    private fun getDpValue(dpValue: Int): Int = (dpValue * layout.context.displayMetrics.density).toInt()
}