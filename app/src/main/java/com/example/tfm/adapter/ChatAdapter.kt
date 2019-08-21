package com.example.tfm.adapter

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.enum.MessageType
import com.example.tfm.enum.Sender
import com.example.tfm.model.MediaContent
import com.example.tfm.model.Message
import com.example.tfm.viewHolder.*
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

class ChatAdapter(private val messages : MutableList<Message>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val context = context

    override fun getItemViewType(position: Int) =  when(messages[position].messageType){
        MessageType.MESSAGE -> MessageType.MESSAGE.value
        MessageType.IMAGE, MessageType.GIF -> MessageType.IMAGE.value
        MessageType.LOCATION -> MessageType.LOCATION.value
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?

        return when(MessageType.fromInt(viewType)){
            MessageType.MESSAGE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_message, parent, false)
                return MessageViewHolder(view)
            }

            MessageType.IMAGE, MessageType.GIF ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_image, parent, false)
                return ImageViewHolder(view)
            }

            MessageType.LOCATION -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_location, parent, false)
                return LocationViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when(holder){
            is MessageViewHolder -> {
                if(message.sender == Sender.OWN){
                    setSenderMessageHolder(holder)
                }

                holder.text.text = message.body as String
                holder.time.text = setTimeFromTimeStamp(123123213)
            }

            is ImageViewHolder ->  {
                if(message.sender == Sender.OTHER){
                   setReceiverImageHolder(holder)
                }

                val mediaContent = message.body as MediaContent
                if(message.messageType == MessageType.IMAGE){
                    holder.image.setImageBitmap(mediaContent.content as Bitmap)
                }else{
                    Glide.with(context)
                        .asGif()
                        .centerCrop()
                        .load(mediaContent.content as String)
                        .into(holder.image)
                }

                holder.time.text = setTimeFromTimeStamp(123123213)
                holder.image.setOnClickListener {
                    context.toast("Image pressed, expanding...")
                }

                if(mediaContent.caption.isNotEmpty()){
                    holder.caption.text = mediaContent.caption
                    holder.caption.visibility = View.VISIBLE
                }
            }

            is LocationViewHolder -> {
                val address = message.body as Address
                holder.initAndUpdateMap(context, LatLng(address.latitude, address.longitude))

                if(message.sender == Sender.OTHER){
                    setReceiverLocationHolder(holder)
                }
                holder.place.text = address.getAddressLine(0)
                holder.time.text = setTimeFromTimeStamp(123123213)
            }
        }
    }

    override fun getItemCount() = messages.size
    private fun getDpValue( dpValue: Int): Int = (dpValue * context.displayMetrics.density).toInt()
    private fun setSenderMessageHolder(holder: MessageViewHolder){
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(getDpValue(60), 0, 0, 0)
        holder.layout.layoutParams = layoutParams
        holder.layout.gravity = Gravity.RIGHT
        holder.placeholder.background = context.getDrawable(R.drawable.sender_message)
        holder.layout.setPadding(0,getDpValue(10),getDpValue(15), getDpValue(10))
        holder.text.setTextColor(context.getColor(R.color.colorWhite))
        holder.time.gravity = Gravity.RIGHT
        holder.time.setTextColor(context.getColor(R.color.imageSenderBackground))
    }

    private fun setReceiverImageHolder(holder: ImageViewHolder){
        holder.layout.setPadding(getDpValue(10), getDpValue(10), getDpValue(60), getDpValue(10))
        holder.layout.gravity = Gravity.LEFT
        holder.time.gravity = Gravity.LEFT
        holder.placeholder.setBackgroundColor(context.getColor(R.color.colorWhite))
        holder.caption.setTextColor(context.getColor(R.color.colorReceiverMessage))
        holder.time.setTextColor(context.getColor(R.color.imageSenderBackground))
    }

    private fun setReceiverLocationHolder(holder: LocationViewHolder){
        holder.locationLayout.gravity = Gravity.LEFT
        holder.locationLayout.setPadding(getDpValue(15),0,0,0)
        holder.time.gravity = Gravity.LEFT
    }

    private fun setTimeFromTimeStamp( timeInMillis: Long): String{
        //TODO add actual time from timestamp which will be a long Type
        return "11:11"
    }
}